package ru.qoqqi.farmrancher.common.plants.types;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AgedPlantType<T extends Block> implements IPlantType {

	private static final float GROWTH_ALIGNMENT_FACTOR = 0.05f;

	public final T block;

	public AgedPlantType(T block) {
		this.block = block;
	}

	@Override
	public boolean hasPlantAt(BlockGetter level, BlockPos pos) {
		var blockState = level.getBlockState(pos);

		return block == blockState.getBlock();
	}

	@Override
	public float getProgressToGrow(BlockGetter level, BlockPos pos) {
		return 1f / getMaxAge();
	}

	@Override
	public float getGrowthSpeed(BlockGetter level, BlockPos pos) {
		var vanillaGrowthMultiplier = getVanillaGrowthMultiplier(level, pos);
		var alignmentMultiplier = getGrowthAlignmentMultiplier(level, pos);

		return vanillaGrowthMultiplier * alignmentMultiplier;
	}

	protected float getVanillaGrowthMultiplier(BlockGetter level, BlockPos pos) {
		var maxVanillaGrowthSpeed = 10f;
		var vanillaGrowthSpeed = CropBlock.getGrowthSpeed(block, level, pos);

		return vanillaGrowthSpeed / maxVanillaGrowthSpeed;
	}

	private float getGrowthAlignmentMultiplier(BlockGetter level, BlockPos pos) {
		var ownAge = getNormalizedAge(level, pos);
		var multiplier = 1f;

		if (ownAge == 0) {
			return multiplier;
		}

		for (var x = -1; x <= 1; x++) {
			for (var z = -1; z <= 1; z++) {
				var otherPos = pos.offset(x, 0, z);

				if (!hasPlantAt(level, otherPos)) {
					continue;
				}

				var otherAge = getNormalizedAge(level, otherPos);
				var aheadBy = otherAge - ownAge;

				multiplier *= 1 + aheadBy * GROWTH_ALIGNMENT_FACTOR;
			}
		}

		return multiplier;
	}

	@Override
	public void growCrops(Level level, BlockPos pos) {
		if (!hasPlantAt(level, pos)) {
			return;
		}

		if (isMaxAge(level, pos)) {
			return;
		}

		var age = getAge(level, pos);
		var newState = getStateForAge(block, age + 1);

		level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
	}

	protected abstract BlockState getStateForAge(T block, int age);
}
