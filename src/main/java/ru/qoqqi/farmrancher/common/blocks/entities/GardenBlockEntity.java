package ru.qoqqi.farmrancher.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GardenBlockEntity extends BlockEntity {

	public GardenBlockEntity(BlockPos pos, BlockState blockState) {
		this(ModBlockEntityTypes.GARDEN.get(), pos, blockState);
	}

	public GardenBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
		super(blockEntityType, pos, blockState);
	}

	public void tick() {

	}
}
