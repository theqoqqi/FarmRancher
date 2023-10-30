package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.world.item.Item;

public interface ISellable {

	Item getItem();

	Price getInitialPrice();
}
