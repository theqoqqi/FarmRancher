package ru.qoqqi.farmrancher.common.plants.types;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import vectorwing.farmersdelight.common.block.BuddingBushBlock;

public class BuddingBushPlantType extends AgedPlantType<BuddingBushBlock> {

	private final CropBlock buddedBlock;

	public BuddingBushPlantType(BuddingBushBlock block, CropBlock buddedBlock) {
		super(block);

		this.buddedBlock = buddedBlock;
	}

	@Override
	public int getAge(BlockState state) {
		return state.getValue(BuddingBushBlock.AGE);
	}

	@Override
	public int getMaxAge() {
		return block.getMaxAge();
	}

	@Override
	protected BlockState getStateForAge(BuddingBushBlock block, int age) {
		return block.getStateForAge(age);
	}

	@Override
	public void growCrops(Level level, BlockPos pos) {
		if (hasPlantAt(level, pos) && isMaxAge(level, pos)) {
			spawnBuddedBlock(level, pos);
			return;
		}

		super.growCrops(level, pos);
	}

	private void spawnBuddedBlock(Level level, BlockPos pos) {
		var state = buddedBlock.defaultBlockState();

		level.setBlockAndUpdate(pos, state);
	}
}
