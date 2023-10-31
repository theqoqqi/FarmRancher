package ru.qoqqi.farmrancher.common.plants.types;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CropPlantType implements IPlantType {

	public final CropBlock block;

	public CropPlantType(CropBlock block) {
		this.block = block;
	}

	@Override
	public CropBlock getPlantAt(BlockGetter level, BlockPos pos) {
		var blockState = level.getBlockState(pos);
		var block = blockState.getBlock();

		return block instanceof CropBlock cropBlock ? cropBlock : null;
	}

	@Override
	public int getAge(BlockState state) {
		return ((CropBlock) state.getBlock()).getAge(state);
	}

	@Override
	public int getMaxAge() {
		return block.getMaxAge();
	}

	@Override
	public float getProgressToGrow() {
		return 1f / block.getMaxAge();
	}

	@Override
	public float getGrowthSpeed(BlockGetter level, BlockPos pos) {
		var maxVanillaGrowthSpeed = 10f;
		var vanillaGrowthSpeed = CropBlock.getGrowthSpeed(block, level, pos);
		var alignmentMultiplier = getGrowthAlignmentMultiplier(level, pos);

		return (vanillaGrowthSpeed / maxVanillaGrowthSpeed) * alignmentMultiplier;
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

				multiplier *= 1 + aheadBy;
			}
		}

		return multiplier;
	}

	@Override
	public void growCrops(Level level, BlockPos pos) {
		var block = getPlantAt(level, pos);

		if (block == null) {
			return;
		}

		if (isMaxAge(level, pos)) {
			return;
		}

		var age = getAge(level, pos);
		var newState = block.getStateForAge(age + 1);

		level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
	}
}
