package ru.qoqqi.farmrancher.common.trading.economics;

import com.mojang.logging.LogUtils;

import net.minecraft.SharedConstants;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.slf4j.Logger;

import ru.qoqqi.farmrancher.FarmRancher;
import ru.qoqqi.farmrancher.common.blocks.entities.TradingBlockEntity;
import ru.qoqqi.farmrancher.common.events.EconomicsEvent;
import ru.qoqqi.farmrancher.common.events.TradeWithBlockEntityEvent;
import ru.qoqqi.farmrancher.common.items.ModItems;
import ru.qoqqi.farmrancher.common.trading.Sellables;
import ru.qoqqi.farmrancher.common.trading.util.PriceRange;
import ru.qoqqi.farmrancher.common.trading.util.Sellable;

public class Economics {

	private static final Logger LOGGER = LogUtils.getLogger();

	@SuppressWarnings("FieldCanBeLocal")
	private final double REDUCTION_PER_STACK = 0.8;

	private static final double HIGH_PRICE_THRESHOLD = 0.5;

	private static final double HIGH_PRICE_LERP_FACTOR = 0.05;

	private static final int PRICE_INCREASE_RARITY_FACTOR = 5;

	private static final int PRICE_INCREASE_MULTIPLIER = 2;

	private static final int PRICE_UPDATE_INTERVAL = SharedConstants.TICKS_PER_GAME_DAY;

	private final ServerLevel level;

	private final EconomicsSavedData data;

	private Economics(ServerLevel level) {
		this.level = level;
		this.data = EconomicsSavedData.getInstance(level);
	}

	public boolean isInDemand(Sellable sellable) {
		var stackPrice = getStackPrice(sellable);

		return stackPrice >= 1 && Double.isFinite(stackPrice);
	}

	public double getStackPrice(Sellable sellable) {
		//noinspection deprecation
		var maxStackSize = sellable.item.getMaxStackSize();
		var price = getPrice(sellable);

		return price * maxStackSize;
	}

	private double getPrice(Sellable sellable) {
		if (!data.hasPrice(sellable.item)) {
			var priceRange = sellable.getPrice(level);
			var average = priceRange.getAverage();

			data.setPrice(sellable.item, average);
			MinecraftForge.EVENT_BUS.post(new EconomicsEvent.PriceAdded(level, sellable, average));
		}

		return data.getPrice(sellable.item);
	}

	public void reducePriceForSoldItem(Sellable sellable, int soldCount) {
		var priceRange = sellable.getPrice(level);
		var reductionFactor = getReductionFactor(sellable, soldCount);
		var currentPrice = data.getPrice(sellable.item);
		var reducedPrice = priceRange.clamp(currentPrice * reductionFactor);

		data.setPrice(sellable.item, reducedPrice);
		MinecraftForge.EVENT_BUS.post(new EconomicsEvent.PriceUpdated(level, sellable, reducedPrice));
	}

	private double getReductionFactor(Sellable sellable, int itemCount) {
		//noinspection deprecation
		var maxStackSize = sellable.item.getMaxStackSize();

		return Math.pow(REDUCTION_PER_STACK, (float) itemCount / maxStackSize);
	}

	public void updatePrices() {
		Sellables.ALL.forEach(sellable -> {
			var newPrice = getUpdatedPrice(sellable);

			data.setPrice(sellable.item, newPrice);
		});

		MinecraftForge.EVENT_BUS.post(new EconomicsEvent.PricesUpdated(level));
	}

	private double getUpdatedPrice(Sellable sellable) {
		var price = getPrice(sellable);
		var priceRange = sellable.getPrice(level);

		price = decreasePrice(price, priceRange);
		price = increasePrice(price, priceRange);

		return price;
	}

	private static double decreasePrice(double price, PriceRange priceRange) {
		var highPrice = priceRange.lerp(HIGH_PRICE_THRESHOLD);

		if (price <= highPrice) {
			return price;
		}

		return Mth.lerp(HIGH_PRICE_LERP_FACTOR, price, priceRange.min);
	}

	private static double increasePrice(double price, PriceRange priceRange) {
		var factor = getRandomPriceIncreaseFactor();

		return Mth.lerp(factor, price, priceRange.max);
	}

	private static double getRandomPriceIncreaseFactor() {
		var factor = 1.0;

		for (var i = 0; i < PRICE_INCREASE_RARITY_FACTOR; i++) {
			factor *= Math.random();
		}

		return Math.min(1, factor * PRICE_INCREASE_MULTIPLIER);
	}

	public int getBoughtAncientSeeds() {
		return data.getBoughtAncientSeeds();
	}

	private void increaseBoughtAncientSeeds() {
		data.setBoughtAncientSeeds(data.getBoughtAncientSeeds() + 1);
		MinecraftForge.EVENT_BUS.post(new EconomicsEvent.PlayerBoughtAncientSeed(level));
	}

	public static Economics getInstance(ServerLevel level) {
		return new Economics(level);
	}

	@Mod.EventBusSubscriber(modid = FarmRancher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class EventHandler {

		@SubscribeEvent
		public static void onTrade(TradeWithBlockEntityEvent event) {
			var blockEntity = event.getBlockEntity();

			if (!(blockEntity instanceof TradingBlockEntity)) {
				return;
			}

			var level = blockEntity.getLevel();

			if (!(level instanceof ServerLevel serverLevel)) {
				return;
			}

			var economics = Economics.getInstance(serverLevel);
			var offer = event.getMerchantOffer();

			handleSellable(offer, economics);
			handleAncientSeed(offer, economics);
		}

		private static void handleSellable(MerchantOffer offer, Economics economics) {
			var soldStack = offer.getBaseCostA();
			var item = soldStack.getItem();
			var optionalSellable = Sellables.get(item);

			optionalSellable.ifPresent(sellable -> {
				economics.reducePriceForSoldItem(sellable, soldStack.getCount());
			});
		}

		private static void handleAncientSeed(MerchantOffer offer, Economics economics) {
			var boughtStack = offer.getResult();
			var item = boughtStack.getItem();

			if (item == ModItems.ANCIENT_SEED.get()) {
				economics.increaseBoughtAncientSeeds();
			}
		}

		@SubscribeEvent
		public static void onLevelTick(TickEvent.LevelTickEvent event) {
			var isValidEvent = !event.level.isClientSide
					&& event.phase == TickEvent.Phase.START
					&& event.level.dimension() == Level.OVERWORLD;

			if (!isValidEvent) {
				return;
			}

			var gameTime = event.level.getGameTime();
			var shouldUpdate = gameTime % PRICE_UPDATE_INTERVAL == 0;

			if (!shouldUpdate) {
				return;
			}

			var economics = Economics.getInstance((ServerLevel) event.level);

			economics.updatePrices();
		}
	}
}
