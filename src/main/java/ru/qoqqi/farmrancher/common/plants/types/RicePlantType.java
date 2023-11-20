package ru.qoqqi.farmrancher.common.plants.types;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vectorwing.farmersdelight.common.block.RiceBlock;
import vectorwing.farmersdelight.common.block.RicePaniclesBlock;

public class RicePlantType extends AgedPlantType<RiceBlock> {

	private final RicePaniclesBlock paniclesBlock;

	public RicePlantType(RiceBlock block, RicePaniclesBlock paniclesBlock) {
		super(block);

		this.paniclesBlock = paniclesBlock;
	}

	@Override
	public int getAge(BlockState state) {
		return state.getValue(RiceBlock.AGE);
	}

	@Override
	public int getMaxAge() {
		return block.getMaxAge();
	}

	@Override
	protected BlockState getStateForAge(RiceBlock block, int age) {
		return block.withAge(age);
	}

	@Override
	public void growCrops(Level level, BlockPos pos) {
		if (hasPlantAt(level, pos) && isMaxAge(level, pos)) {
			spawnPaniclesFor(level, pos);
			return;
		}

		super.growCrops(level, pos);
	}

	private void spawnPaniclesFor(Level level, BlockPos pos) {
		var paniclesPos = pos.above();

		if (!canSpawnOn(level, paniclesPos)) {
			return;
		}

		spawnPanicles(level, paniclesPos);
	}

	private void spawnPanicles(Level level, BlockPos paniclesPos) {
		var state = paniclesBlock.defaultBlockState();

		level.setBlockAndUpdate(paniclesPos, state);
	}

	private boolean canSpawnOn(Level level, BlockPos pos) {
		if (!level.isEmptyBlock(pos)) {
			return false;
		}

		return paniclesBlock.defaultBlockState().canSurvive(level, pos);
	}

	@Override
	protected float getVanillaGrowthMultiplier(BlockGetter level, BlockPos pos) {
		return 1;
	}
}
