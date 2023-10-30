package ru.qoqqi.farmrancher.common.dishes;

import net.minecraft.world.item.Item;

import ru.qoqqi.farmrancher.common.trading.ISellable;
import ru.qoqqi.farmrancher.common.trading.Price;
import ru.qoqqi.farmrancher.common.util.IntRange;

public class Dish implements ISellable {

	public final Item item;

	public final IntRange stackPrice;

	public Dish(Item item, IntRange stackPrice) {
		this.item = item;
		this.stackPrice = stackPrice;
	}

	@Override
	public Item getItem() {
		return item;
	}

	@Override
	public Price getInitialPrice() {
		return new Price(stackPrice.getLowerAverage());
	}
}
