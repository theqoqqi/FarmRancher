package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

import vectorwing.farmersdelight.common.registry.ModItems;

public class SimpleIngredients {

	public static final Map<Item, Double> PRICES = new HashMap<>() {
		{
			put(Items.WHEAT, per64Items(3));
			put(Items.CARROT, per64Items(5));
			put(Items.POTATO, per64Items(5));
			put(Items.BEETROOT, per64Items(6));
			put(Items.PUMPKIN, per64Items(20));
			put(ModItems.PUMPKIN_SLICE.get(), get(Items.PUMPKIN) / 4);
			put(Items.MELON, per64Items(32));
			put(Items.MELON_SLICE, get(Items.MELON) / 9);
			put(ModItems.CABBAGE.get(), per64Items(5));
			put(ModItems.CABBAGE_LEAF.get(), get(ModItems.CABBAGE.get()) / 2);
			put(ModItems.TOMATO.get(), per64Items(6));
			put(ModItems.ONION.get(), per64Items(4));
			put(ModItems.RICE.get(), per64Items(3));

			put(Items.RED_MUSHROOM, per64Items(8));
			put(Items.BROWN_MUSHROOM, per64Items(5));
			put(Items.KELP, per64Items(5));
			put(Items.APPLE, per64Items(5));
			put(Items.SUGAR, per64Items(2));
			put(Items.EGG, per16Items(2));
			put(Items.INK_SAC, per64Items(10));
			put(Items.COCOA_BEANS, per64Items(4));
			put(Items.BONE, per64Items(8));
			put(Items.SWEET_BERRIES, per64Items(4));
			put(Items.GLOW_BERRIES, per64Items(12));
			put(Items.HONEY_BOTTLE, per16Items(2.5));
			put(ModItems.MILK_BOTTLE.get(), per16Items(0.75));

			put(Items.CHICKEN, per64Items(5));
			put(ModItems.CHICKEN_CUTS.get(), get(Items.CHICKEN) / 2);
			put(Items.BEEF, per64Items(5));
			put(ModItems.MINCED_BEEF.get(), get(Items.BEEF) / 2);
			put(Items.PORKCHOP, per64Items(5));
			put(ModItems.BACON.get(), get(Items.PORKCHOP) / 2);
			put(ModItems.HAM.get(), get(Items.PORKCHOP) * 2);
			put(Items.RABBIT, per64Items(8));
			put(Items.MUTTON, per64Items(5));
			put(ModItems.MUTTON_CHOPS.get(), get(Items.MUTTON) / 2);
			put(Items.TROPICAL_FISH, per64Items(10));
			put(Items.COD, per64Items(8));
			put(ModItems.COD_SLICE.get(), get(Items.COD) / 2);
			put(Items.SALMON, per64Items(8));
			put(ModItems.SALMON_SLICE.get(), get(Items.SALMON) / 2);

			put(Items.BOWL, 0.0);
			put(Items.BUCKET, 0.0);
			put(Items.GLASS_BOTTLE, 0.0);
		}
	};

	private static double per64Items(double price) {
		return price / 64;
	}

	private static double per16Items(double price) {
		return price / 16;
	}
}
