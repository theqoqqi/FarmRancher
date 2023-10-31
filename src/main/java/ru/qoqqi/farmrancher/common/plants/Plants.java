package ru.qoqqi.farmrancher.common.plants;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ru.qoqqi.farmrancher.common.plants.types.CropPlantType;

public class Plants {

	private static final Logger LOGGER = LogUtils.getLogger();

	private static final Map<Block, Plant> plants = new HashMap<>();

	static {
		registerCropPlant(
				Blocks.WHEAT,
				Items.WHEAT,
				secondsToGrowSpeed(10),
				new PlantDropTable(Items.WHEAT_SEEDS, 1.1f, Items.WHEAT, 1.5f)
		);
		registerCropPlant(
				Blocks.CARROTS,
				Items.CARROT,
				secondsToGrowSpeed(20),
				new PlantDropTable(Items.CARROT, 1.5f)
		);
		registerCropPlant(
				Blocks.POTATOES,
				Items.POTATO,
				secondsToGrowSpeed(20),
				new PlantDropTable(Items.POTATO, 1.5f, Items.POISONOUS_POTATO, 0.02f)
		);
		registerCropPlant(
				Blocks.BEETROOTS,
				Items.BEETROOT,
				secondsToGrowSpeed(30),
				new PlantDropTable(Items.BEETROOT_SEEDS, 1.2f, Items.BEETROOT, 1.5f)
		);
	}

	private static void registerCropPlant(
			Block block,
			Item fruitItem,
			float growthSpeed,
			PlantDropTable plantDropTable
	) {
		var type = new CropPlantType((CropBlock) block);
		var plant = new Plant(type, fruitItem, growthSpeed, plantDropTable);

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

	public static Collection<Plant> getAll() {
		return plants.values();
	}
}
