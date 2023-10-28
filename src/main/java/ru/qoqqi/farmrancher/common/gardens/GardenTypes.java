package ru.qoqqi.farmrancher.common.gardens;

import com.mojang.logging.LogUtils;

import org.slf4j.Logger;

public class GardenTypes {

	private static final Logger LOGGER = LogUtils.getLogger();

	public static final GardenType WOODEN_GARDEN = new GardenType(2, 0.8f, 0.8f);

	public static final GardenType IRON_GARDEN = new GardenType(4, 1f, 1f);

	public static final GardenType GOLDEN_GARDEN = new GardenType(5, 1.2f, 1.1f);

	public static final GardenType DIAMOND_GARDEN = new GardenType(6, 1.5f, 1.25f);

	public static final GardenType NETHERITE_GARDEN = new GardenType(8, 2.0f, 1.5f);
}
