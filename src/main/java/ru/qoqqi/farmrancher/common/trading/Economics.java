package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.server.level.ServerLevel;

public class Economics {

	private final ServerLevel level;

	private final ItemPricesSavedData data;

	private Economics(ServerLevel level) {
		this.level = level;
		this.data = ItemPricesSavedData.getInstance(level);
	}

	public Price getStackPrice(Sellable sellable) {
		//noinspection deprecation
		var maxStackSize = sellable.item.getMaxStackSize();
		var price = getPrice(sellable);

		return new Price((int) Math.round(price * maxStackSize));
	}

	private double getPrice(Sellable sellable) {
		if (!data.hasPrice(sellable.item)) {
			var priceRange = sellable.getPrice(level);
			var average = priceRange.getAverage();

			data.setPrice(sellable.item, (float) average);
		}

		return data.getPrice(sellable.item);
	}

	public static Economics getInstance(ServerLevel level) {
		return new Economics(level);
	}
}
