package ru.qoqqi.farmrancher.common.fruits;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ru.qoqqi.farmrancher.common.util.IntRange;
import vectorwing.farmersdelight.common.registry.ModItems;

public class Fruits {

	private static final Map<Item, Fruit> fruits = new HashMap<>();

	static {
		registerFruit(
				Items.WHEAT,
				IntRange.of(1, 5)
		);
		registerFruit(
				Items.CARROT,
				IntRange.of(2, 8)
		);
		registerFruit(
				Items.POTATO,
				IntRange.of(2, 8)
		);
		registerFruit(
				Items.BEETROOT,
				IntRange.of(3, 9)
		);

		// Farmer's Delight
		registerFruit(
				ModItems.CABBAGE.get(),
				IntRange.of(10, 20)
		);
		registerFruit(
				ModItems.TOMATO.get(),
				IntRange.of(10, 20)
		);
		registerFruit(
				ModItems.ONION.get(),
				IntRange.of(10, 20)
		);
		registerFruit(
				ModItems.RICE.get(),
				IntRange.of(10, 20)
		);
	}

	private static void registerFruit(Item item, IntRange stackPrice) {
		var fruit = new Fruit(item, stackPrice);
		fruits.put(item, fruit);
	}

	public static boolean isFruit(Item item) {
		return fruits.containsKey(item);
	}

	public static Fruit get(Item item) {
		return fruits.get(item);
	}

	public static Collection<Fruit> getAll() {
		return fruits.values();
	}
}
