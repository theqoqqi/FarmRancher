package ru.qoqqi.farmrancher.common.trading;

import com.mojang.logging.LogUtils;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.slf4j.Logger;

import ru.qoqqi.farmrancher.FarmRancher;
import ru.qoqqi.farmrancher.common.blocks.entities.TradingBlockEntity;
import ru.qoqqi.farmrancher.common.events.TradeWithBlockEntityEvent;

public class Economics {

	private static final Logger LOGGER = LogUtils.getLogger();

	@SuppressWarnings("FieldCanBeLocal")
	private final double REDUCTION_PER_64_ITEMS = 0.8;

	private final ServerLevel level;

	private final ItemPricesSavedData data;

	private Economics(ServerLevel level) {
		this.level = level;
		this.data = ItemPricesSavedData.getInstance(level);
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

			data.setPrice(sellable.item, (float) average);
		}

		return data.getPrice(sellable.item);
	}

	public void reducePriceForSoldItem(Sellable sellable, int soldCount) {
		var priceRange = sellable.getPrice(level);
		var reductionFactor = getReductionFactor(soldCount);
		var currentPrice = data.getPrice(sellable.item);
		var reducedPrice = priceRange.clamp(currentPrice * reductionFactor);

		data.setPrice(sellable.item, (float) reducedPrice);
	}

	private double getReductionFactor(int itemCount) {
		return Math.pow(REDUCTION_PER_64_ITEMS, itemCount / 64.0);
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
			var soldStack = offer.getBaseCostA();
			var item = soldStack.getItem();
			var optionalSellable = Sellables.get(item);

			optionalSellable.ifPresent(sellable -> {
				economics.reducePriceForSoldItem(sellable, soldStack.getCount());
			});
		}
	}
}
