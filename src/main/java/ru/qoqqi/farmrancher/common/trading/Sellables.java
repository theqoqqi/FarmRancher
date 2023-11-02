package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

import ru.qoqqi.farmrancher.common.util.IntRange;
import vectorwing.farmersdelight.common.registry.ModItems;

public class Sellables {

	public static final Map<Item, Sellable> FRUITS = new HashMap<>();

	public static final Map<Item, Sellable> DISHES = new HashMap<>();

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
		registerFruit(
				Items.PUMPKIN,
				IntRange.of(10, 30)
		);
		registerFruit(
				Items.MELON,
				IntRange.of(16, 48)
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

	static {
		// В Farmer's Delight требует 3 свеклы вместо 6.
		// Свёкла стоит ~6 за 64, то есть за 6 штук 0.5625 монет.
		// Это и есть одно блюдо без бонусов к цене.
		registerDish(
				Items.BEETROOT_SOUP,
				IntRange.of(10, 20)
		);
		// В Farmer's Delight требует столько же
		// Если считать грибы по 0.05, то блюдо 0.1. хз вообще
		// Кстати, грибы в Farmer's Delight походу как-то иначе выращиваются, нужно посмотреть.
		registerDish(
				Items.MUSHROOM_STEW,
				IntRange.of(10, 20)
		);
		// В Farmer's Delight требует сырого кролика вместо жареного.
		// Морковь 0.078125, Картофель (сырой) 0.078125. Остальное хз.
		// Прикинем что кролик 0.1, гриб 0.05. Итого блюдо примерно 0.3 монеты
		registerDish(
				Items.RABBIT_STEW,
				IntRange.of(10, 20)
		);

		// Farmer's Delight (это далеко не все предметы, только супы и похлебки)
		registerDish(
				ModItems.COOKED_RICE.get(),
				IntRange.of(10, 20)
		);
		registerDish(
				ModItems.BONE_BROTH.get(),
				IntRange.of(10, 20)
		);
		registerDish(
				ModItems.BEEF_STEW.get(),
				IntRange.of(10, 20)
		);
		registerDish(
				ModItems.CHICKEN_SOUP.get(),
				IntRange.of(10, 20)
		);
		registerDish(
				ModItems.VEGETABLE_SOUP.get(),
				IntRange.of(10, 20)
		);
		registerDish(
				ModItems.FISH_STEW.get(),
				IntRange.of(10, 20)
		);
		registerDish(
				ModItems.FRIED_RICE.get(),
				IntRange.of(10, 20)
		);
		registerDish(
				ModItems.PUMPKIN_SOUP.get(),
				IntRange.of(10, 20)
		);
		registerDish(
				ModItems.BAKED_COD_STEW.get(),
				IntRange.of(10, 20)
		);
		registerDish(
				ModItems.NOODLE_SOUP.get(),
				IntRange.of(10, 20)
		);
	}

	private static void registerFruit(Item item, IntRange stackPrice) {
		var fruit = new Sellable(item, level -> stackPrice);

		FRUITS.put(item, fruit);
	}

	private static void registerDish(Item item, IntRange stackPrice) {
		var dish = new Sellable(item, level -> stackPrice);

		DISHES.put(item, dish);
	}
}
