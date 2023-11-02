package ru.qoqqi.farmrancher.common.dishes;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ru.qoqqi.farmrancher.common.trading.Sellable;
import ru.qoqqi.farmrancher.common.util.IntRange;
import vectorwing.farmersdelight.common.registry.ModItems;

public class Dishes {

	private static final Map<Item, Sellable> dishes = new HashMap<>();

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

	private static void registerDish(Item item, IntRange stackPrice) {
		var dish = new Sellable(item, level -> stackPrice);

		dishes.put(item, dish);
	}

	public static boolean isDish(Item item) {
		return dishes.containsKey(item);
	}

	public static Sellable get(Item item) {
		return dishes.get(item);
	}

	public static Collection<Sellable> getAll() {
		return dishes.values();
	}
}
