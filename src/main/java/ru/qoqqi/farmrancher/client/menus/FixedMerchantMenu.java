package ru.qoqqi.farmrancher.client.menus;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class FixedMerchantMenu extends MerchantMenu {

	private static final Logger LOGGER = LogUtils.getLogger();

	private int selectedOffer;

	public FixedMerchantMenu(int containerId, Inventory playerInventory, Merchant merchant) {
		super(containerId, playerInventory, merchant);
	}

	@NotNull
	@Override
	public ItemStack quickMoveStack(@NotNull Player player, int index) {
		var itemStack = super.quickMoveStack(player, index);

		if (index == RESULT_SLOT) {
			Minecraft.getInstance().tell(this::reselectOffer);
		}

		return itemStack;
	}

	private void reselectOffer() {
		setSelectionHint(selectedOffer);
		tryMoveItems(selectedOffer);
	}

	@Override
	public void setSelectionHint(int offerIndex) {
		super.setSelectionHint(offerIndex);

		selectedOffer = offerIndex;
	}

	@Override
	public void playTradeSound() {
		// В оригинале метод делает каст (Entity) trader, из-за которого невозможно использовать
		// MerchantMenu с торговцами (Merchant), не наследующими Entity.

		if (trader instanceof BlockEntity blockEntity) {
			var level = blockEntity.getLevel();

			if (level != null && !level.isClientSide()) {
				var pos = blockEntity.getBlockPos();
				var sound = trader.getNotifyTradeSound();

				level.playLocalSound(pos, sound, SoundSource.NEUTRAL, 1.0F, 1.0F, false);
			}

			return;
		}

		if (!(trader instanceof Entity)) {
			return;
		}

		super.playTradeSound();
	}
}
