package ru.qoqqi.farmrancher.common.items;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import ru.qoqqi.farmrancher.FarmRancher;

public class ModItems {

	public static final DeferredRegister<Item> ITEMS
			= DeferredRegister.create(ForgeRegistries.ITEMS, FarmRancher.MOD_ID);

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
