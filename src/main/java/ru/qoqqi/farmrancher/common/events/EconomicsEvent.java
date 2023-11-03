package ru.qoqqi.farmrancher.common.events;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

import ru.qoqqi.farmrancher.common.trading.util.Sellable;

public class EconomicsEvent extends LevelEvent {

	public EconomicsEvent(Level level) {
		super(level);
	}

	public static class PriceEvent extends EconomicsEvent {

		private final Sellable sellable;

		private final double price;

		public PriceEvent(Level level, Sellable sellable, double price) {
			super(level);

			this.sellable = sellable;
			this.price = price;
		}

		public Sellable getSellable() {
			return sellable;
		}

		public double getPrice() {
			return price;
		}
	}

	public static class PriceAdded extends PriceEvent {

		public PriceAdded(Level level, Sellable sellable, double price) {
			super(level, sellable, price);
		}
	}

	public static class PriceUpdated extends PriceEvent {

		public PriceUpdated(Level level, Sellable sellable, double price) {
			super(level, sellable, price);
		}
	}

	public static class PricesUpdated extends EconomicsEvent {

		public PricesUpdated(Level level) {
			super(level);
		}
	}
}
