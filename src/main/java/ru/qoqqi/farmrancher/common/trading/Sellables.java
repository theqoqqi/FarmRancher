package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class Sellables {

	public static final float PRICE_RANGE_DEVIATION = 0.75f;

	public static final Map<RecipeType<?>, Double> RECIPE_PRICE_BONUSES = new HashMap<>() {
		{
			put(RecipeType.CRAFTING, 1.05);
			put(RecipeType.SMOKING, 1.1);
			put(RecipeType.CAMPFIRE_COOKING, 1.1);
			put(RecipeType.SMELTING, 1.1);
			put(ModRecipeTypes.COOKING.get(), 1.5);
			put(ModRecipeTypes.CUTTING.get(), 1.5);
		}
	};

	public static final Map<Item, Sellable> FRUITS = new HashMap<>();

	public static final Map<Item, Sellable> DISHES = new HashMap<>();

	static {
		register(
				FRUITS,
				Items.WHEAT,
				Items.CARROT,
				Items.POTATO,
				Items.BEETROOT,
				Items.PUMPKIN,
				Items.MELON,

				// Farmer's Delight
				ModItems.CABBAGE.get(),
				ModItems.TOMATO.get(),
				ModItems.ONION.get(),
				ModItems.RICE.get()
		);
	}

	static {
		register(
				DISHES,
				Items.BEETROOT_SOUP,
				Items.MUSHROOM_STEW,
				Items.RABBIT_STEW,

				// Farmer's Delight
				ModItems.COOKED_RICE.get(),
				ModItems.BONE_BROTH.get(),
				ModItems.BEEF_STEW.get(),
				ModItems.CHICKEN_SOUP.get(),
				ModItems.VEGETABLE_SOUP.get(),
				ModItems.FISH_STEW.get(),
				ModItems.FRIED_RICE.get(),
				ModItems.PUMPKIN_SOUP.get(),
				ModItems.BAKED_COD_STEW.get(),
				ModItems.NOODLE_SOUP.get()
		);
	}

	private static void register(Map<Item, Sellable> registry, Item... items) {
		for (Item item : items) {
			var sellable = new Sellable(item, level -> getPriceRange(item, level));

			registry.put(item, sellable);
		}
	}

	@NotNull
	private static PriceRange getPriceRange(Item item, ServerLevel level) {
		var priceCalculator = new PriceCalculator(level, SimpleIngredients.PRICES, RECIPE_PRICE_BONUSES);
		var price = priceCalculator.getPrice(item);

		if (price.isEmpty() || Double.isInfinite(price.getAsDouble())) {
			return PriceRange.invalid();
		}

		var minPrice = price.getAsDouble() * (1 - PRICE_RANGE_DEVIATION);
		var maxPrice = price.getAsDouble() * (1 + PRICE_RANGE_DEVIATION);

		return PriceRange.of(minPrice, maxPrice);
	}
}
