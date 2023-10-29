package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

import ru.qoqqi.farmrancher.common.items.CoinItem;

public class Price {

	private final int value;

	public Price(CoinItem.Tier coinTier, int count) {
		this.value = coinTier.value * count;
	}

	public Price(int copper, int silver, int golden) {
		this.value = copper
				+ silver * CoinItem.Tier.SILVER.value
				+ golden * CoinItem.Tier.GOLDEN.value;
	}

	public Price(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public int getCountOf(CoinItem.Tier coinTier) {
		return (value / coinTier.value) % CoinItem.EXCHANGE_RATE;
	}

	public ItemStack asSingleStack() {
		return getStacks(1).findFirst().orElse(ItemStack.EMPTY);
	}

	@SuppressWarnings("SameParameterValue")
	private Stream<ItemStack> getStacks(int maxStacks) {
		return CoinItem.fromHighToLow()
				.filter(item -> getCountOf(item.tier) > 0)
				.map(item -> new ItemStack(item, getCountOf(item.tier)))
				.limit(maxStacks);
	}
}
