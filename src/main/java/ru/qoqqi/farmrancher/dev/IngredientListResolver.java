package ru.qoqqi.farmrancher.dev;

import com.mojang.logging.LogUtils;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import ru.qoqqi.farmrancher.common.trading.PriceCalculator;

public class IngredientListResolver {

	private static final Logger LOGGER = LogUtils.getLogger();

	private final Map<Item, Double> simpleIngredientPrices;

	private final RecipeManager recipeManager;

	private final RegistryAccess registryAccess;

	private final Map<Recipe<?>, Optional<List<ItemStack>>> recipePrices = new HashMap<>();

	private final Comparator<List<ItemStack>> comparator;

	public IngredientListResolver(ServerLevel level, Map<Item, Double> simpleIngredientPrices, PriceCalculator priceCalculator) {
		this.simpleIngredientPrices = simpleIngredientPrices;
		this.recipeManager = level.getRecipeManager();
		this.registryAccess = level.registryAccess();
		this.comparator = Comparator.comparingDouble(list -> {
			return list.stream()
					.map(priceCalculator::getPrice)
					.mapToDouble(optionalDouble -> optionalDouble.orElse(Double.MAX_VALUE))
					.sum();
		});
	}

	public Optional<List<ItemStack>> getIngredientList(ItemStack stack) {
//		LOGGER.info("getPrice: {}", stack);

		if (simpleIngredientPrices.containsKey(stack.getItem())) {
			return Optional.of(List.of(stack));
		}

		var recipes = getRecipes(stack.getItem());

		return recipes.map(this::getRecipePrice).min(comparator);
	}

	private Stream<Recipe<?>> getRecipes(Item item) {
//		LOGGER.info("getRecipes: {}", item);

		return recipeManager.getRecipes()
				.stream()
				.filter(recipe -> isRecipeOf(item, recipe))
				.filter(recipe -> !isCyclicRecipe(recipe));
	}

	private boolean isCyclicRecipe(Recipe<?> recipe) {
//		LOGGER.info("isCyclicRecipe: {}", recipe.getId());

		return isRequiredToCraftItself(recipe) || isRequiredToCalculateSelfPrice(recipe);
	}

	private boolean isRequiredToCraftItself(Recipe<?> recipe) {
		var resultStack = recipe.getResultItem(registryAccess);

		return recipe.getIngredients()
				.stream()
				.map(Ingredient::getItems)
				.flatMap(Arrays::stream)
				.anyMatch(stack -> ItemStack.isSameItem(stack, resultStack));
	}

	private boolean isRequiredToCalculateSelfPrice(Recipe<?> recipe) {
		return recipePrices.containsKey(recipe)
				&& recipePrices.get(recipe).isEmpty();
	}

	private boolean isRecipeOf(Item item, Recipe<?> recipe) {
		return recipe.getResultItem(registryAccess).getItem() == item;
	}

	private List<ItemStack> getRecipePrice(Recipe<?> recipe) {
//		LOGGER.info("getRecipePrice: {}", recipe.getId());

		if (recipePrices.containsKey(recipe) && recipePrices.get(recipe).isPresent()) {
			return recipePrices.get(recipe).get();
		}

		recipePrices.put(recipe, Optional.empty());
//		LOGGER.info("PUT empty for: {}", recipe.getId());

		var price = recipe.getIngredients()
				.stream()
				.map(Ingredient::getItems)
				.map(this::getCheapestStack)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.reduce(new ArrayList<>(), this::reduce);

		recipePrices.put(recipe, Optional.of(price));
//		LOGGER.info("PUT {} for: {}", Optional.of(price), recipe.getId());

		return price;
	}

	private List<ItemStack> reduce(List<ItemStack> accumulated, List<ItemStack> stacksToAdd) {
		stacksToAdd.forEach(stack -> addStack(accumulated, stack));
		return accumulated;
	}

	private void addStack(List<ItemStack> stacks, ItemStack stack) {
		for (int i = 0; i < stacks.size(); i++) {
			var existing = stacks.get(i);

			if (ItemStack.isSameItem(existing, stack)) {
				int newCount = existing.getCount() + stack.getCount();
				var replacement = existing.copyWithCount(newCount);

				stacks.set(i, replacement);

				return;
			}
		}

		stacks.add(stack);
	}

	private Optional<List<ItemStack>> getCheapestStack(ItemStack[] itemStacks) {
		return Arrays.stream(itemStacks)
				.map(this::getIngredientList)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.min(comparator);
	}
}
