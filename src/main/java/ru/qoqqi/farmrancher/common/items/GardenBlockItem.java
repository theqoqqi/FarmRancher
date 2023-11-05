package ru.qoqqi.farmrancher.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import ru.qoqqi.farmrancher.common.blocks.GardenBlock;
import ru.qoqqi.farmrancher.common.gardens.GardenType;

public class GardenBlockItem extends BlockItem {

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

		tooltip.add(getGardenInfo(gardenBlock.gardenType).withStyle(ChatFormatting.GRAY));
	}

	private static MutableComponent getGardenInfo(GardenType gardenType) {
		var areaSize = gardenType.areaRadius * 2 + 1;
		var growthSpeed = Math.round(gardenType.growthSpeed * 100);
		var profitability = Math.round(gardenType.profitability * 100);

		return Component.translatable(
				"block.farm_rancher.gardens.desc",
				areaSize,
				growthSpeed,
				profitability
		);
	}
}
