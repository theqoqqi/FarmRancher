package ru.qoqqi.farmrancher.client.menus;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FixedMerchantMenu extends MerchantMenu {

	public FixedMerchantMenu(int containerId, Inventory playerInventory, Merchant merchant) {
		super(containerId, playerInventory, merchant);
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
