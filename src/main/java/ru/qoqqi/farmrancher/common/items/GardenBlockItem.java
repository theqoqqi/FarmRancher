package ru.qoqqi.farmrancher.common.items;

import com.mojang.logging.LogUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

import ru.qoqqi.farmrancher.common.blocks.GardenBlock;
import ru.qoqqi.farmrancher.common.gardens.GardenType;

public class GardenBlockItem extends BlockItem {

	private static final Logger LOGGER = LogUtils.getLogger();

	public GardenBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(
			@NotNull ItemStack stack,
			@Nullable Level level,
			@NotNull List<Component> tooltip,
			@NotNull TooltipFlag tooltipFlag
	) {
		var gardenBlock = (GardenBlock) getBlock();

		tooltip.addAll(getGardenInfo(gardenBlock.gardenType));
	}

	private static List<Component> getGardenInfo(GardenType gardenType) {
		var areaSize = gardenType.areaRadius * 2 + 1;
		var growthSpeed = Math.round(gardenType.growthSpeed * 100);
		var profitability = Math.round(gardenType.profitability * 100);

		return List.of(
				translatable(
						"block.farm_rancher.gardens.desc1",
						intLiteral(areaSize)
				),
				translatable(
						"block.farm_rancher.gardens.desc2",
						intLiteral(growthSpeed)
				),
				translatable(
						"block.farm_rancher.gardens.desc3",
						intLiteral(profitability)
				)
		);
	}

	private static Component translatable(String key, Object... args) {
		return Component.translatable(key, args)
				.withStyle(ChatFormatting.GRAY);
	}

	private static Component intLiteral(int value) {
		return Component.literal(String.valueOf(value))
				.withStyle(ChatFormatting.WHITE);
	}
}
