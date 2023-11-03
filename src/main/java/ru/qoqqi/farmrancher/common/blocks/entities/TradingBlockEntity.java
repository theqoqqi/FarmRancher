package ru.qoqqi.farmrancher.common.blocks.entities;

import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import ru.qoqqi.farmrancher.common.blocks.TradingBlock;

public class TradingBlockEntity extends BaseMerchantBlockEntity implements Merchant {

	private static final Logger LOGGER = LogUtils.getLogger();

	public TradingBlockEntity(BlockPos pos, BlockState blockState) {
		this(ModBlockEntityTypes.MERCHANT.get(), pos, blockState);
	}

	public TradingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
		super(blockEntityType, pos, blockState);
	}

	@NotNull
	public InteractionResult onBlockActivated(Player player) {

		if (level == null) {
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
