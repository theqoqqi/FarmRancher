package ru.qoqqi.farmrancher.common.plants.types;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.state.BlockState;

public class StemPlantType extends AgedPlantType<StemBlock> {

	private final StemGrownBlock fruitBlock;

	private final float progressToSpawnFruit;

	public StemPlantType(StemBlock block, float progressToSpawnFruit) {
		super(block);

		this.fruitBlock = block.getFruit();
		this.progressToSpawnFruit = progressToSpawnFruit;
	}

	@Override
	public int getAge(BlockState state) {
		if (hasFruit(state)) {
			return StemBlock.MAX_AGE;
		}

		return state.getValue(StemBlock.AGE);
	}

	@Override
	public int getMaxAge() {
		return StemBlock.MAX_AGE;
	}

	@Override
	protected BlockState getStateForAge(StemBlock block, int age) {
		return block.defaultBlockState().setValue(StemBlock.AGE, age);
	}

	@Override
	public float getProgressToGrow(BlockGetter level, BlockPos pos) {
		if (isMaxAge(level, pos)) {
			return progressToSpawnFruit;
		}

		return super.getProgressToGrow(level, pos);
	}

	@Override
	public float getGrowthSpeed(BlockGetter level, BlockPos pos) {
		if (hasFruit(level, pos)) {
			return 0;
		}

		return super.getGrowthSpeed(level, pos);
	}

	private boolean hasFruit(BlockGetter level, BlockPos pos) {
		var state = level.getBlockState(pos);

		return hasFruit(state);
	}

	private boolean hasFruit(BlockState state) {
		return state.getBlock() == fruitBlock.getAttachedStem();
	}

	@Override
	public void growCrops(Level level, BlockPos pos) {
		if (hasPlantAt(level, pos) && isMaxAge(level, pos)) {
			spawnFruitBlockFor(level, pos);
			return;
		}

		super.growCrops(level, pos);
	}

	private void spawnFruitBlockFor(Level level, BlockPos stemPos) {
		var availableDirections = Direction.Plane.HORIZONTAL.stream()
				.filter(direction -> canSpawnOn(level, stemPos, direction))
				.toList();

		if (availableDirections.isEmpty()) {
			return;
		}

		var randomIndex = level.random.nextInt(availableDirections.size());
		var direction = availableDirections.get(randomIndex);

		spawnFruitBlock(level, stemPos, direction);
	}

	private void spawnFruitBlock(Level level, BlockPos stemPos, Direction direction) {
		var pos = stemPos.relative(direction);
		var newStemState = fruitBlock.getAttachedStem().defaultBlockState()
				.setValue(HorizontalDirectionalBlock.FACING, direction);

		level.setBlockAndUpdate(pos, fruitBlock.defaultBlockState());
		level.setBlockAndUpdate(stemPos, newStemState);
	}

	private boolean canSpawnOn(Level level, BlockPos stemPos, Direction direction) {
		var pos = stemPos.relative(direction);

		return canSpawnOn(level, pos);
	}

	private boolean canSpawnOn(Level level, BlockPos pos) {
		if (!level.isEmptyBlock(pos)) {
			return false;
		}

		var belowPos = pos.below();
		var blockState = level.getBlockState(belowPos);

		return blockState.canSustainPlant(level, belowPos, Direction.UP, fruitBlock)
				|| blockState.is(Blocks.FARMLAND)
				|| blockState.is(BlockTags.DIRT);
	}
}
