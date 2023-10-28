package ru.qoqqi.farmrancher.common.plants;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlantDropTable {

	public final Item seedItem;

	public final float seedCount;

	public final Item fruitItem;

	public final float fruitCount;

	public PlantDropTable(Item seedItem, float seedsToDrop) {
		this(seedItem, seedsToDrop, null, 0);
	}

	public PlantDropTable(Item seedItem, float seedsToDrop, Item fruitItem, float fruitsToDrop) {
		this.seedItem = seedItem;
		this.seedCount = seedsToDrop;
		this.fruitItem = fruitItem;
		this.fruitCount = fruitsToDrop;
	}

	public List<ItemStack> getRandomDrops(RandomSource random, float profitability) {
		return getRandomDrops(random, profitability, profitability);
	}

	public List<ItemStack> getRandomDrops(RandomSource random, float seedFactor, float fruitFactor) {
		var list = new ArrayList<ItemStack>();

		if (seedItem != null && seedCount > 0) {
			addRandomDrops(list, random, seedItem, seedCount * seedFactor);
		}

		if (fruitItem != null && fruitCount > 0) {
			addRandomDrops(list, random, fruitItem, fruitCount * fruitFactor);
		}

		return list;
	}

	private static void addRandomDrops(List<ItemStack> list, RandomSource random, Item item, float averageCount) {
		var count = getRandomInt(random, averageCount);
		var itemStack = new ItemStack(item, count);

		list.add(itemStack);
	}

	private static int getRandomInt(RandomSource random, float average) {
		var min = (int) Math.floor(average);
		var max = min + 1;
		var chance = average - min;

		return random.nextFloat() < chance ? max : min;
	}
}
