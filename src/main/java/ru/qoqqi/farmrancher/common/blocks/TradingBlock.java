package ru.qoqqi.farmrancher.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import ru.qoqqi.farmrancher.common.blocks.entities.TradingBlockEntity;

public class TradingBlock extends BaseEntityBlock {

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	private final Consumer<MerchantOffers> offerListFiller;

	public TradingBlock(Consumer<MerchantOffers> offerListFiller, Properties properties) {
		super(properties);

		this.offerListFiller = offerListFiller;

		registerDefaultState(
				stateDefinition.any()
						.setValue(FACING, Direction.NORTH)
		);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Nonnull
	public InteractionResult use(
			@Nonnull BlockState state,
			@Nonnull Level level,
			@Nonnull BlockPos pos,
			@Nonnull Player player,
			@Nonnull InteractionHand handIn,
			@Nonnull BlockHitResult hit
	) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}

		var blockEntity = level.getBlockEntity(pos);

		if (blockEntity == null) {
			return InteractionResult.PASS;
		}

		return ((TradingBlockEntity) blockEntity).onBlockActivated(player);
	}

	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
	}

	@SuppressWarnings("deprecation")
	@NotNull
	public BlockState rotate(BlockState pState, Rotation pRotation) {
		return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	@NotNull
	public BlockState mirror(BlockState pState, Mirror pMirror) {
		return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING);
	}

	@SuppressWarnings("deprecation")
	@NotNull
	public RenderShape getRenderShape(@NotNull BlockState pState) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new TradingBlockEntity(pos, state);
	}

	public void addOffers(MerchantOffers offers) {
		offerListFiller.accept(offers);
	}
}
