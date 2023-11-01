package ru.qoqqi.farmrancher.common.plants.types;

import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CropPlantType extends AgedPlantType<CropBlock> {

	public CropPlantType(CropBlock block) {
		super(block);
	}

	@Override
	public int getAge(BlockState state) {
		return block.getAge(state);
	}

	@Override
	public int getMaxAge() {
		return block.getMaxAge();
	}

	@Override
	protected BlockState getStateForAge(CropBlock block, int age) {
		return block.getStateForAge(age);
	}
}
