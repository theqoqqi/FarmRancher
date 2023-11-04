package ru.qoqqi.farmrancher.common.trading.util;

import com.mojang.logging.LogUtils;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import org.slf4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PriceCalculator {

	private static final Logger LOGGER = LogUtils.getLogger();

	private static final Map<ServerLevel, PriceCalculator> instances = new HashMap<>();

	private final Map<Item, Double> simpleIngredientPrices;

	private final RecipeManager recipeManager;

	private final RegistryAccess registryAccess;

	private final Map<Recipe<?>, OptionalDouble> recipePrices = new HashMap<>();

	private final Map<RecipeType<?>, Double> recipePriceBonuses;

	public PriceCalculator(
			ServerLevel level,
			Map<Item, Double> simpleIngredientPrices,
			Map<RecipeType<?>, Double> recipePriceBonuses
	) {
		this.simpleIngredientPrices = simpleIngredientPrices;
		this.recipePriceBonuses = recipePriceBonuses;
		this.recipeManager = level.getRecipeManager();
		this.registryAccess = level.registryAccess();
	}

	public OptionalDouble getPrice(ItemStack stack) {
		var price = getPrice(stack.getItem());

		if (price.isEmpty()) {
			return price;
		}

		return OptionalDouble.of(price.getAsDouble() * stack.getCount());
	}

	public OptionalDouble getPrice(Item item) {
//		LOGGER.info("getPrice: {}", item);

		if (simpleIngredientPrices.containsKey(item)) {
			return OptionalDouble.of(simpleIngredientPrices.get(item));
		}

		var recipes = getRecipes(item);

		return recipes.mapToDouble(this::getRecipePrice).min();
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

		return recipePrices.containsKey(recipe)
				&& recipePrices.get(recipe).isEmpty();
	}

	private boolean isRecipeOf(Item item, Recipe<?> recipe) {
		return recipe.getResultItem(registryAccess).getItem() == item;
	}

	private double getRecipePrice(Recipe<?> recipe) {
//		LOGGER.info("getRecipePrice: {}", recipe.getId());

		if (recipePrices.containsKey(recipe) && recipePrices.get(recipe).isPresent()) {
			return recipePrices.get(recipe).getAsDouble();
		}

		recipePrices.put(recipe, OptionalDouble.empty());

		var resultStack = recipe.getResultItem(registryAccess);
		var priceOfIngredients = recipe.getIngredients()
				.stream()
				.map(Ingredient::getItems)
				.filter(itemStacks -> itemStacks.length > 0)
				.map(this::getCheapestStack)
				.mapToDouble(value -> value.orElse(Double.MAX_VALUE))
				.sum();
		var priceOfSingleResult = priceOfIngredients / resultStack.getCount();
		var recipePriceBonus = getRecipePriceBonus(recipe);
		var priceWithBonuses = priceOfSingleResult * recipePriceBonus;

//		logRecipePrice(recipe, priceWithBonuses, priceOfIngredients, priceOfSingleResult, recipePriceBonus, resultStack);

		recipePrices.put(recipe, OptionalDouble.of(priceWithBonuses));

		return priceWithBonuses;
	}

	private void logRecipePrice(
			Recipe<?> recipe,
			double priceWithBonuses,
			double priceOfIngredients,
			double priceOfSingleResult,
			double recipePriceBonus,
			ItemStack resultStack
	) {
		LOGGER.info(
				"PUT {} ({} / {} * {} * {}) for: {} ({}, {})",
				priceWithBonuses,
				priceOfIngredients,
				priceOfSingleResult,
				recipePriceBonus,
				resultStack.getCount(),
				resultStack,
				recipe.getIngredients()
						.stream()
						.map(Ingredient::getItems)
						.filter(itemStacks -> itemStacks.length > 0)
						.collect(Collectors.toList()),
				recipe.getIngredients()
						.stream()
						.map(Ingredient::getItems)
						.filter(itemStacks -> itemStacks.length > 0)
						.map(this::getCheapestStack)
						.map(value -> value.orElse(Double.MAX_VALUE))
						.collect(Collectors.toList())
		);
	}

	private double getRecipePriceBonus(Recipe<?> recipe) {
		return recipePriceBonuses.getOrDefault(recipe.getType(), 1.0);
	}

	private OptionalDouble getCheapestStack(ItemStack[] itemStacks) {
		return Arrays.stream(itemStacks)
				.map(ItemStack::getItem)
				.map(this::getPrice)
				.filter(OptionalDouble::isPresent)
				.mapToDouble(OptionalDouble::getAsDouble)
				.min();
	}

	public static PriceCalculator getInstance(
			ServerLevel forLevel,
			Map<Item, Double> simpleIngredientPrices,
			Map<RecipeType<?>, Double> recipePriceBonuses
	) {
		return instances.computeIfAbsent(forLevel, level -> {
			return new PriceCalculator(level, simpleIngredientPrices, recipePriceBonuses);
		});
	}
}
