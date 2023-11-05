package ru.qoqqi.farmrancher.common.gardens;

import com.mojang.logging.LogUtils;

import org.slf4j.Logger;

public class GardenTypes {

	private static final Logger LOGGER = LogUtils.getLogger();

	public static final GardenType WOODEN_GARDEN = new GardenType(2, 0.8f, 0.9f);

	public static final GardenType COPPER_GARDEN = new GardenType(3, 1f, 1f);

	public static final GardenType IRON_GARDEN = new GardenType(4, 1.2f, 1.1f);

	public static final GardenType GOLDEN_GARDEN = new GardenType(5, 1.4f, 1.2f);

	public static final GardenType DIAMOND_GARDEN = new GardenType(6, 1.6f, 1.3f);

	public static final GardenType NETHERITE_GARDEN = new GardenType(7, 1.8f, 1.4f);
}
