package ru.qoqqi.farmrancher.common.trading.util;

import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.qoqqi.farmrancher.common.items.CoinItem;

public class Price {

	private final int value;

	public Price(CoinItem.Tier coinTier, int count) {
		this(coinTier.value * count);
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

	public Price floor() {
		var priceStack = asSingleStack();
		var coinItem = (CoinItem) priceStack.getItem();

		return new Price(coinItem.tier, priceStack.getCount());
	}

	public ItemStack asSingleStack() {
		return getStacks(1).findFirst().orElse(ItemStack.EMPTY);
	}

	public List<ItemStack> asTwoStacks() {
		var stacks = getStacks(2).collect(Collectors.toList());

		while (stacks.size() < 2) {
			stacks.add(ItemStack.EMPTY);
		}

		return stacks;
	}

	@SuppressWarnings("SameParameterValue")
	private Stream<ItemStack> getStacks(int maxStacks) {
		return CoinItem.fromHighToLow()
				.filter(item -> getCountOf(item.tier) > 0)
				.map(item -> new ItemStack(item, getCountOf(item.tier)))
				.limit(maxStacks);
	}
}
