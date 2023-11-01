package ru.qoqqi.farmrancher.common.plants.types;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IPlantType {

	boolean hasPlantAt(BlockGetter level, BlockPos pos);

	default float getNormalizedAge(BlockGetter level, BlockPos pos) {
		return (float) getAge(level, pos) / getMaxAge();
	}

	default boolean isMaxAge(BlockGetter level, BlockPos pos) {
		return getAge(level, pos) == getMaxAge();
	}

	default boolean isMaxAge(BlockState state) {
		return getAge(state) == getMaxAge();
	}

	default int getAge(BlockGetter level, BlockPos pos) {
		if (!hasPlantAt(level, pos)) {
			return 0;
		}

		return getAge(level.getBlockState(pos));
	}

	int getAge(BlockState state);

	int getMaxAge();

	float getProgressToGrow();

	float getGrowthSpeed(BlockGetter level, BlockPos pos);

	void growCrops(Level level, BlockPos blockPos);
}
