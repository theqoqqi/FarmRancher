package ru.qoqqi.farmrancher.common.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;

import ru.qoqqi.farmrancher.common.plants.types.IPlantType;

public class Plant {

	public final IPlantType type;

	public final float growthSpeed;

	public final PlantDropTable dropTable;

	public Plant(
			IPlantType type,
			float growthSpeed,
			PlantDropTable dropTable
	) {
		this.type = type;
		this.growthSpeed = growthSpeed;
		this.dropTable = dropTable;
	}

	public float getGrowthSpeed(BlockGetter level, BlockPos pos) {
		return type.getGrowthSpeed(level, pos) * growthSpeed;
	}
}
