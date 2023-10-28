package ru.qoqqi.farmrancher.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.qoqqi.farmrancher.common.blocks.entities.GardenBlockEntity;
import ru.qoqqi.farmrancher.common.blocks.entities.ModBlockEntityTypes;
import ru.qoqqi.farmrancher.common.gardens.GardenType;

public class GardenBlock extends BaseEntityBlock {

	public final GardenType gardenType;

	public GardenBlock(Properties properties, GardenType gardenType) {
		super(properties);

		this.gardenType = gardenType;
	}

	@SuppressWarnings("deprecation")
	@NotNull
	public RenderShape getRenderShape(@NotNull BlockState pState) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new GardenBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
		if (type != ModBlockEntityTypes.GARDEN.get()) {
			return null;
		}

		return (pLevel, pPos, pState, pBlockEntity) -> ((GardenBlockEntity) pBlockEntity).tick();
	}
}
