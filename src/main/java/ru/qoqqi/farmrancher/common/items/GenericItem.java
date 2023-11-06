package ru.qoqqi.farmrancher.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GenericItem extends Item {

	public GenericItem(Properties properties) {
		super(properties);
	}
	@Override
	public void appendHoverText(
			@NotNull ItemStack stack,
			@Nullable Level level,
			@NotNull List<Component> tooltipComponents,
			@NotNull TooltipFlag isAdvanced
	) {
		tryAddInfo(tooltipComponents, "desc");
		tryAddInfo(tooltipComponents, "desc1");
		tryAddInfo(tooltipComponents, "desc2");
		tryAddInfo(tooltipComponents, "desc3");
	}

	private void tryAddInfo(@NotNull List<Component> tooltipComponents, String keyPart) {
		if (hasInfo(keyPart)) {
			tooltipComponents.add(getInfo(keyPart));
		}
	}

	private boolean hasInfo(String keyPart) {
		var key = getInfoKey(keyPart);

		return Language.getInstance().has(key);
	}

	private Component getInfo(String keyPart) {
		var key = getInfoKey(keyPart);

		return Component.translatable(key).withStyle(ChatFormatting.GRAY);
	}

	@NotNull
	private String getInfoKey(String keyPart) {
		return getDescriptionId() + "." + keyPart;
	}
}
