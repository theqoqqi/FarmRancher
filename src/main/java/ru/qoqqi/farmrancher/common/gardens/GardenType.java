package ru.qoqqi.farmrancher.common.gardens;

public class GardenType {

	public final int areaRadius;

	public final float growthSpeed;

	public final float profitability;

	public GardenType(int areaRadius, float growthSpeed, float profitability) {
		this.areaRadius = areaRadius;
		this.growthSpeed = growthSpeed;
		this.profitability = profitability;
	}
}
