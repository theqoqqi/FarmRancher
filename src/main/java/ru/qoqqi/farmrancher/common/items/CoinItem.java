package ru.qoqqi.farmrancher.common.items;

import com.google.common.math.IntMath;

import net.minecraft.world.item.Item;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CoinItem extends Item {

	public static final int EXCHANGE_RATE = 64;

	private static final Map<Tier, CoinItem> BY_TIERS = new HashMap<>();

	public final Tier tier;

	public CoinItem(Tier tier, Properties pProperties) {
		super(pProperties);

		this.tier = tier;

		BY_TIERS.put(tier, this);
	}

	public static CoinItem byTier(Tier tier) {
		return BY_TIERS.get(tier);
	}

	public static Stream<CoinItem> fromLowToHigh() {
		return Arrays.stream(Tier.values()).map(CoinItem::byTier);
	}

	public enum Tier {
		COPPER(),
		SILVER(),
		GOLDEN();

		public final int value;

		Tier() {
			value = IntMath.pow(EXCHANGE_RATE, ordinal());
		}

		public Tier up() {
			return getByOrdinal(ordinal() + 1);
		}

		public Tier down() {
			return getByOrdinal(ordinal() - 1);
		}

		private static Tier getByOrdinal(int ordinal) {
			var values = values();

			return ordinal >= 0 && ordinal < values.length
					? values[ordinal]
					: null;
		}
	}
}
