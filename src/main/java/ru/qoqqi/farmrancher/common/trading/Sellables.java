package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ru.qoqqi.farmrancher.common.trading.util.PriceCalculator;
import ru.qoqqi.farmrancher.common.trading.util.PriceRange;
import ru.qoqqi.farmrancher.common.trading.util.Sellable;
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
			put(ModRecipeTypes.CUTTING.get(), 1.0);
		}
	};

	public static final List<Sellable> FRUITS = new ArrayList<>();

	public static final List<Sellable> BUFFET = new ArrayList<>();

	public static final List<Sellable> CAFETERIA = new ArrayList<>();

	public static final List<Sellable> CONFECTIONERY = new ArrayList<>();

	public static final List<Sellable> RESTAURANT = new ArrayList<>();

	public static final List<Sellable> ALL = new ArrayList<>();

	static {
		register(
				FRUITS,
				1.0,
				Items.WHEAT,
				Items.CARROT,
				Items.POTATO,
				Items.BEETROOT,
				Items.PUMPKIN,
				Items.MELON,
				ModItems.CABBAGE.get(),
				ModItems.TOMATO.get(),
				ModItems.ONION.get(),
				ModItems.RICE.get(),

				Items.RED_MUSHROOM,
				Items.BROWN_MUSHROOM,
				Items.KELP,
				Items.APPLE,
				Items.SUGAR,
				Items.EGG,
				Items.COCOA_BEANS,
				ModItems.MILK_BOTTLE.get(),

				Items.CHICKEN,
				Items.BEEF,
				Items.PORKCHOP,
				Items.RABBIT,
				Items.MUTTON,
				Items.TROPICAL_FISH,
				Items.COD,
				Items.SALMON
		);

		register(
				BUFFET,
				1.25,
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

		register(
				CAFETERIA,
				1.6,

				// Farmer's Delight
				ModItems.MIXED_SALAD.get(),
				ModItems.BARBECUE_STICK.get(),
				ModItems.EGG_SANDWICH.get(),
				ModItems.CHICKEN_SANDWICH.get(),
				ModItems.HAMBURGER.get(),
				ModItems.BACON_SANDWICH.get(),
				ModItems.MUTTON_WRAP.get(),
				ModItems.DUMPLINGS.get(),
				ModItems.STUFFED_POTATO.get(),
				ModItems.CABBAGE_ROLLS.get(),
				ModItems.SALMON_ROLL.get(),
				ModItems.COD_ROLL.get(),
				ModItems.KELP_ROLL.get(),
				ModItems.APPLE_CIDER.get()
		);

		register(
				CONFECTIONERY,
				2.0,

				Items.COOKIE,
				Items.CAKE,

				// Farmer's Delight
				ModItems.APPLE_PIE.get(),
				ModItems.SWEET_BERRY_CHEESECAKE.get(),
				ModItems.CHOCOLATE_PIE.get(),
				ModItems.SWEET_BERRY_COOKIE.get(),
				ModItems.HONEY_COOKIE.get(),
				ModItems.MELON_POPSICLE.get(),
				ModItems.FRUIT_SALAD.get(),
				ModItems.GLOW_BERRY_CUSTARD.get()
		);

		register(
				RESTAURANT,
				2.5,

				// Farmer's Delight
				ModItems.BACON_AND_EGGS.get(),
				ModItems.PASTA_WITH_MEATBALLS.get(),
				ModItems.PASTA_WITH_MUTTON_CHOP.get(),
				ModItems.MUSHROOM_RICE.get(),
				ModItems.ROASTED_MUTTON_CHOPS.get(),
				ModItems.VEGETABLE_NOODLES.get(),
				ModItems.STEAK_AND_POTATOES.get(),
				ModItems.RATATOUILLE.get(),
				ModItems.SQUID_INK_PASTA.get(),
				ModItems.GRILLED_SALMON.get(),
				ModItems.MELON_JUICE.get()
		);

		ALL.addAll(FRUITS);
		ALL.addAll(BUFFET);
		ALL.addAll(CAFETERIA);
		ALL.addAll(CONFECTIONERY);
		ALL.addAll(RESTAURANT);

		// Блочная еда, она не стакается. мб кусочками?
//				ModItems.ROAST_CHICKEN_BLOCK.get(),
//				ModItems.STUFFED_PUMPKIN_BLOCK.get(),
//				ModItems.HONEY_GLAZED_HAM_BLOCK.get(),
//				ModItems.SHEPHERDS_PIE_BLOCK.get(),
//				ModItems.RICE_ROLL_MEDLEY_BLOCK.get(),
	}

	public static Optional<Sellable> get(Item item) {
		return ALL.stream().filter(sellable -> sellable.item == item).findFirst();
	}

	private static void register(List<Sellable> registry, double priceBonus, Item... items) {
		for (Item item : items) {
			var sellable = new Sellable(item, level -> getPriceRange(item, level, priceBonus));

			registry.add(sellable);
		}
	}

	@NotNull
	private static PriceRange getPriceRange(Item item, ServerLevel level, double priceBonus) {
		var priceCalculator = getPriceCalculator(level);
		var price = priceCalculator.getPrice(item);

		if (price.isEmpty() || Double.isInfinite(price.getAsDouble())) {
			return PriceRange.invalid();
		}

		var priceWithBonus = price.getAsDouble() * priceBonus;
		var minPrice = priceWithBonus * (1 - PRICE_RANGE_DEVIATION);
		var maxPrice = priceWithBonus * (1 + PRICE_RANGE_DEVIATION);

		return PriceRange.of(minPrice, maxPrice);
	}

	private static PriceCalculator getPriceCalculator(ServerLevel level) {
		return PriceCalculator.getInstance(level, SimpleIngredients.PRICES, RECIPE_PRICE_BONUSES);
	}
}
