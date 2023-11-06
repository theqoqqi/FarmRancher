package ru.qoqqi.farmrancher.common.blocks.entities;

import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import ru.qoqqi.farmrancher.common.blocks.TradingBlock;
import ru.qoqqi.farmrancher.common.events.EconomicsEvent;

public class TradingBlockEntity extends BaseMerchantBlockEntity implements Merchant {

	private static final Logger LOGGER = LogUtils.getLogger();

	public TradingBlockEntity(BlockPos pos, BlockState blockState) {
		this(ModBlockEntityTypes.MERCHANT.get(), pos, blockState);
	}

	public TradingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
		super(blockEntityType, pos, blockState);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onBreak(BlockEvent.BreakEvent event) {
		if (!getBlockPos().equals(event.getPos())) {
			return;
		}

		MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void onPriceUpdated(EconomicsEvent.PriceUpdated event) {
		if (hasOfferWith(event.getSellable().getItem())) {
			onPricesUpdated();
		}
	}

	@SubscribeEvent
	public void onPricesUpdated(EconomicsEvent.PricesUpdated event) {
		onPricesUpdated();
	}

	@SubscribeEvent
	public void onPlayerBoughtAncientSeed(EconomicsEvent.PlayerBoughtAncientSeed event) {
		if (offers == null) {
			return;
		}

		onPricesUpdated();
	}

	private void onPricesUpdated() {
		if (isClientSide()) {
			return;
		}

		updateOffers();
		resendOffersToTradingPlayer();
	}

	private boolean hasOfferWith(Item item) {
		if (offers == null) {
			return false;
		}

		return offers.stream().anyMatch(offer -> offer.getBaseCostA().getItem() == item);
	}

	@NotNull
	public InteractionResult onBlockActivated(Player player) {

		if (level == null) {
			return InteractionResult.PASS;
		}

		if (isTrading()) {
			return InteractionResult.PASS;
		}

		var name = getBlockState().getBlock().getName();

		setTradingPlayer(player);
		openTradingScreen(player, name, 0);

		return InteractionResult.CONSUME;
	}

	@Override
	protected MerchantOffers createOffers() {
		var block = (TradingBlock) getBlockState().getBlock();

		return block.getOfferListFactory().createOffers(this);
	}

	@Override
	protected void updateOffers() {
		offers = createOffers();
	}
}
