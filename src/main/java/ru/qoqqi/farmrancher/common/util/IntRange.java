package ru.qoqqi.farmrancher.common.util;

import net.minecraft.util.RandomSource;

import java.util.function.IntPredicate;

public class IntRange implements IntPredicate {

	public final int min;

	public final int max;

	private IntRange(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean test(int value) {
		return value >= min && value <= max;
	}

	public int getRandomValue(RandomSource random) {
		return random.nextInt(max - min + 1) + min;
	}

	public int getLowerAverage() {
		return (min + max) / 2;
	}

	public int getUpperAverage() {
		return (min + max + 1) / 2;
	}

	public static IntRange of(int min, int max) {
		return new IntRange(min, max);
	}

	public static IntRange ofMax(int max) {
		return new IntRange(0, max);
	}
}
