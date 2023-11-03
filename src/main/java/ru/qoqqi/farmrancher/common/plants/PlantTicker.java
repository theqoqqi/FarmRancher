package ru.qoqqi.farmrancher.common.plants;

import com.mojang.logging.LogUtils;

import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.slf4j.Logger;

import java.util.Objects;
import java.util.Random;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import ru.qoqqi.farmrancher.FarmRancher;

public class PlantTicker {

	private static final Logger LOGGER = LogUtils.getLogger();

	private static final float MAX_RANDOM_DEVIATION = 0.05f;

	private static final float WILD_GROW_CHANCE = 0.01f;

	private static final Int2FloatMap deviations = new Int2FloatOpenHashMap();

	public static void tick(ServerLevel level, BlockPos blockPos, BlockState blockState, float growthSpeed) {
		var plant = Plants.get(blockState);

		if (plant == null) {
			return;
		}

		var combinedGrowthSpeed = getGrowthSpeed(level, blockPos, plant, growthSpeed);
		var progressToGrow = plant.type.getProgressToGrow(level, blockPos);

		modifyProgress(level, blockPos, combinedGrowthSpeed);

		if (hasProgress(level, blockPos, progressToGrow)) {
			plant.type.growCrops(level, blockPos);

			if (plant.type.isMaxAge(level, blockPos)) {
				removeProgress(level, blockPos);
			} else {
				modifyProgress(level, blockPos, -progressToGrow);
			}
		}
	}

	private static float getGrowthSpeed(ServerLevel level, BlockPos blockPos, Plant plant, float multiplier) {
		var plantGrowthSpeed = plant.getGrowthSpeed(level, blockPos);
		var randomMultiplier = getDeviation(level, blockPos);

		return plantGrowthSpeed * randomMultiplier * multiplier;
	}

	private static float getDeviation(ServerLevel level, BlockPos blockPos) {
		var dayNumber = level.getGameTime() / SharedConstants.TICKS_PER_GAME_DAY;
		var positionHashCode = blockPos.hashCode();
		var seed = Objects.hash(positionHashCode, dayNumber);

		return deviations.computeIfAbsent(seed, PlantTicker::getRandomDeviation);
	}

	private static float getRandomDeviation(int seed) {
		var random = new Random(seed);

		// Skip first value to increase entropy.
		// Otherwise, the values will greatly depend on nearby seeds.
		random.nextInt();

		return random.nextFloat(1 - MAX_RANDOM_DEVIATION, 1 + MAX_RANDOM_DEVIATION);
	}

	private static boolean hasProgress(ServerLevel level, BlockPos blockPos, float required) {
		var growData = PlantGrowSavedData.getInstance(level);
		var progress = growData.getProgress(blockPos);

		return progress >= required;
	}

	private static void removeProgress(ServerLevel level, BlockPos blockPos) {
		var growData = PlantGrowSavedData.getInstance(level);

		growData.removeProgress(blockPos);
	}

	private static void modifyProgress(ServerLevel level, BlockPos blockPos, float modifyBy) {
		var growData = PlantGrowSavedData.getInstance(level);
		var currentProgress = growData.getProgress(blockPos);
		var newProgress = currentProgress + modifyBy;

		growData.setProgress(blockPos, newProgress);
	}

	@Mod.EventBusSubscriber(modid = FarmRancher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class EventHandler {

		@SubscribeEvent
		public static void onBreakBlock(BlockEvent.BreakEvent event) {
			var level = event.getLevel();

			if (level.isClientSide()) {
				return;
			}

			removeProgress((ServerLevel) level, event.getPos());
		}

		@SubscribeEvent
		public static void onCropGrow(final BlockEvent.CropGrowEvent.Pre event) {
			if (!Plants.isPlant(event.getState().getBlock())) {
				return;
			}

			var randomValue = event.getLevel().getRandom().nextFloat();
			var shouldGrow = randomValue < WILD_GROW_CHANCE;

			if (!shouldGrow) {
				event.setResult(Event.Result.DENY);
			}
		}
	}
}
