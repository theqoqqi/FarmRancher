package ru.qoqqi.farmrancher.common.plants;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ru.qoqqi.farmrancher.common.plants.types.CropPlantType;
import ru.qoqqi.farmrancher.common.plants.types.RicePlantType;
import ru.qoqqi.farmrancher.common.plants.types.StemPlantType;
import vectorwing.farmersdelight.common.block.RiceBlock;
import vectorwing.farmersdelight.common.block.RicePaniclesBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

public class Plants {

	private static final Logger LOGGER = LogUtils.getLogger();

	private static final Map<Block, Plant> plants = new HashMap<>();

	static {
		registerCropPlant(
				Blocks.WHEAT,
				secondsToGrowSpeed(10),
				new PlantDropTable(Items.WHEAT_SEEDS, 1.1f, Items.WHEAT, 1.5f)
		);
		registerCropPlant(
				Blocks.CARROTS,
				secondsToGrowSpeed(20),
				new PlantDropTable(Items.CARROT, 1.5f)
		);
		registerCropPlant(
				Blocks.POTATOES,
				secondsToGrowSpeed(20),
				new PlantDropTable(Items.POTATO, 1.5f, Items.POISONOUS_POTATO, 0.02f)
		);
		registerCropPlant(
				Blocks.BEETROOTS,
				secondsToGrowSpeed(30),
				new PlantDropTable(Items.BEETROOT_SEEDS, 1.2f, Items.BEETROOT, 1.5f)
		);

		registerStemPlant(
				Blocks.PUMPKIN_STEM,
				secondsToGrowSpeed(30),
				secondsToGrowSpeed(3),
				new PlantDropTable(Items.PUMPKIN_SEEDS, 1.2f)
		);
		registerStemPlant(
				Blocks.MELON_STEM,
				secondsToGrowSpeed(30),
				secondsToGrowSpeed(3),
				new PlantDropTable(Items.MELON_SEEDS, 1.2f)
		);

		// Farmer's Delight
		registerCropPlant(
				ModBlocks.CABBAGE_CROP.get(),
				secondsToGrowSpeed(5),
				new PlantDropTable(ModItems.CABBAGE_SEEDS.get(), 1.2f, ModItems.CABBAGE.get(), 1.5f)
		);
		registerCropPlant(
				ModBlocks.TOMATO_CROP.get(),
				secondsToGrowSpeed(5),
				new PlantDropTable(ModItems.TOMATO_SEEDS.get(), 1.5f, ModItems.TOMATO.get(), 1.5f)
		);
//		registerCropPlant(
//				ModBlocks.BUDDING_TOMATO_CROP.get(), // Это не CropBlock
//				secondsToGrowSpeed(5),
//				new PlantDropTable(ModItems.CABBAGE_SEEDS.get(), 1.2f, ModItems.CABBAGE.get(), 1.5f)
//		);
		registerCropPlant(
				ModBlocks.ONION_CROP.get(),
				secondsToGrowSpeed(5),
				new PlantDropTable(ModItems.ONION.get(), 1.5f)
		);
		registerRicePlant(
				ModBlocks.RICE_CROP.get(),
				ModBlocks.RICE_CROP_PANICLES.get(),
				secondsToGrowSpeed(5),
				new PlantDropTable(ModItems.RICE.get(), 1f)
		);
		registerCropPlant(
				ModBlocks.RICE_CROP_PANICLES.get(),
				secondsToGrowSpeed(5),
				new PlantDropTable(ModItems.RICE.get(), 1.5f)
		);
	}

	private static void registerCropPlant(
			Block block,
			float growthSpeed,
			PlantDropTable plantDropTable
	) {
		var type = new CropPlantType((CropBlock) block);
		var plant = new Plant(type, growthSpeed, plantDropTable);

		plants.put(type.block, plant);
	}

	private static void registerRicePlant(
			Block block,
			Block paniclesBlock,
			float growthSpeed,
			PlantDropTable plantDropTable
	) {
		var type = new RicePlantType((RiceBlock) block, (RicePaniclesBlock) paniclesBlock);
		var plant = new Plant(type, growthSpeed, plantDropTable);

		plants.put(type.block, plant);
	}

	private static void registerStemPlant(
			Block block,
			float stemGrowthSpeed,
			float fruitGrowthSpeed,
			PlantDropTable plantDropTable
	) {
		var progressToSpawnFruit = stemGrowthSpeed / fruitGrowthSpeed;
		var type = new StemPlantType((StemBlock) block, progressToSpawnFruit);
		var plant = new Plant(type, stemGrowthSpeed, plantDropTable);

		plants.put(type.block, plant);
		plants.put(type.block.getFruit().getAttachedStem(), plant);
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
