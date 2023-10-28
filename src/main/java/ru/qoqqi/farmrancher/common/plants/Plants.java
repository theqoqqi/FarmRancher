package ru.qoqqi.farmrancher.common.plants;

import com.mojang.logging.LogUtils;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import ru.qoqqi.farmrancher.common.plants.types.CropPlantType;

public class Plants {

	private static final Logger LOGGER = LogUtils.getLogger();

	private static final Map<Block, Plant> plants = new HashMap<>();

	static {
		registerCropPlant(Blocks.WHEAT, secondsToGrowSpeed(10));
		registerCropPlant(Blocks.CARROTS, secondsToGrowSpeed(20));
		registerCropPlant(Blocks.POTATOES, secondsToGrowSpeed(20));
		registerCropPlant(Blocks.BEETROOTS, secondsToGrowSpeed(30));
	}

	private static void registerCropPlant(Block block, float growthSpeed) {
		var type = new CropPlantType((CropBlock) block);
		var plant = new Plant(type, growthSpeed);

		plants.put(type.block, plant);
	}

	private static float minutesToGrowSpeed(float minutes) {
		return secondsToGrowSpeed(minutes * 60);
	}

	private static float secondsToGrowSpeed(float seconds) {
		var ticks = seconds * 20;

		return 1 / ticks;
	}

	public static boolean isPlant(BlockState blockState) {
		return isPlant(blockState.getBlock());
	}

	public static boolean isPlant(Block block) {
		return plants.containsKey(block);
	}

	public static Plant get(BlockState blockState) {
		return get(blockState.getBlock());
	}

	public static Plant get(Block block) {
		return plants.get(block);
	}
}
