package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.util.Mth;

import java.util.function.DoublePredicate;

public class PriceRange implements DoublePredicate {

	public final double min;

	public final double max;

	private PriceRange(double min, double max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean test(double value) {
		return value >= min && value <= max;
	}

	public double clamp(double value) {
		return Mth.clamp(value, min, max);
	}

	public boolean isValid() {
		return min > 0 && max >= min;
	}

	public double getAverage() {
		return (min + max) / 2;
	}

	public static PriceRange of(double min, double max) {
		return new PriceRange(min, max);
	}

	public static PriceRange invalid() {
		return new PriceRange(0, 0);
	}
}
