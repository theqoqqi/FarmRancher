package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;

public interface ISellable {

	Item getItem();

	Price getInitialPrice(ServerLevel level);
}
