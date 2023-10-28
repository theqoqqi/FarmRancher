package ru.qoqqi.farmrancher.common.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import ru.qoqqi.farmrancher.FarmRancher;
import ru.qoqqi.farmrancher.common.level.data.PlantGrowSavedData;

public class PlantTicker {

	public static void tick(ServerLevel level, BlockPos blockPos, BlockState blockState, float growthSpeed) {
		var plant = Plants.get(blockState);

		if (plant == null) {
			return;
		}

		var plantGrowthSpeed = plant.getGrowthSpeed(level, blockPos);
		var combinedGrowthSpeed = plantGrowthSpeed * growthSpeed;
		var progressToGrow = plant.type.getProgressToGrow();

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
			if (Plants.isPlant(event.getState().getBlock())) {
				event.setResult(Event.Result.DENY);
			}
		}
	}
}
