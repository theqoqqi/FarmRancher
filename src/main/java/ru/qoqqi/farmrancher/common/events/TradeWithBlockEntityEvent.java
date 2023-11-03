package ru.qoqqi.farmrancher.common.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.player.PlayerEvent;

import ru.qoqqi.farmrancher.common.blocks.entities.BaseMerchantBlockEntity;

public class TradeWithBlockEntityEvent extends PlayerEvent {

	private final MerchantOffer offer;

	private final BaseMerchantBlockEntity blockEntity;

	public TradeWithBlockEntityEvent(Player player, MerchantOffer offer, BaseMerchantBlockEntity blockEntity) {
		super(player);
		this.offer = offer;
		this.blockEntity = blockEntity;
	}

	public MerchantOffer getMerchantOffer() {
		return offer;
	}

	public BaseMerchantBlockEntity getBlockEntity() {
		return blockEntity;
	}
}
