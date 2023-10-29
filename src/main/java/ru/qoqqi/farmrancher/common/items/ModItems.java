package ru.qoqqi.farmrancher.common.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import ru.qoqqi.farmrancher.FarmRancher;

public class ModItems {

	public static final DeferredRegister<Item> ITEMS
			= DeferredRegister.create(ForgeRegistries.ITEMS, FarmRancher.MOD_ID);

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

	public static void registerBlockItem(String actualName, Supplier<? extends Block> blockSupplier) {
		var properties = new Item.Properties();

		ITEMS.register(actualName, () -> new BlockItem(blockSupplier.get(), properties));
	}
}
