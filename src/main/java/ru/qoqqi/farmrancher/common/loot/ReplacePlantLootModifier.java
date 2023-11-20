package ru.qoqqi.farmrancher.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import ru.qoqqi.farmrancher.common.blocks.entities.GardenBlockEntity;
import ru.qoqqi.farmrancher.common.plants.Plants;

public class ReplacePlantLootModifier extends LootModifier {

	public static Codec<ReplacePlantLootModifier> CODEC = RecordCodecBuilder.create(
			instance -> instance
					.group(
							LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(lm -> lm.conditions)
					)
					.apply(instance, ReplacePlantLootModifier::new)
	);

	protected ReplacePlantLootModifier(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Nonnull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (shouldReplaceLoot(context)) {
			generatedLoot.clear();

			addPlantLoot(generatedLoot, context);
		}

		return generatedLoot;
	}

	private static void addPlantLoot(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		var blockState = context.getParamOrNull(LootContextParams.BLOCK_STATE);
		var position = context.getParamOrNull(LootContextParams.ORIGIN);

		if (blockState == null || position == null) {
			return;
		}

		var plant = Plants.get(blockState);

		if (!plant.type.isMaxAge(blockState)) {
			return;
		}

		var serverLevel = context.getLevel();
		var blockPos = BlockPos.containing(position);
		var garden = GardenBlockEntity.getPreferredByProfitability(serverLevel, blockPos);

		if (garden == null) {
			return;
		}

		var gardenType = garden.getGardenType();
		var profitability = gardenType.profitability;
		var drops = plant.dropTable.getRandomDrops(serverLevel.random, profitability);

		generatedLoot.addAll(drops);
	}

	private static boolean shouldReplaceLoot(LootContext context) {
		var blockState = context.getParamOrNull(LootContextParams.BLOCK_STATE);
		var position = context.getParamOrNull(LootContextParams.ORIGIN);

		if (blockState == null || position == null) {
			return false;
		}

		var plant = Plants.get(blockState);

		if (plant == null) {
			return false;
		}

		return plant.type.isMaxAge(blockState);
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
}
