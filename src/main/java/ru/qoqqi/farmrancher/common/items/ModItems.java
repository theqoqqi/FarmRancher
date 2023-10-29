package ru.qoqqi.farmrancher.common.items;

import com.google.common.collect.Sets;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.function.Supplier;

import ru.qoqqi.farmrancher.FarmRancher;

public class ModItems {

	public static Set<RegistryObject<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

	public static final DeferredRegister<Item> ITEMS
			= DeferredRegister.create(ForgeRegistries.ITEMS, FarmRancher.MOD_ID);

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

	public static void registerBlockItem(String actualName, Supplier<? extends Block> blockSupplier) {
		var properties = new Item.Properties();

		register(actualName, () -> new BlockItem(blockSupplier.get(), properties));
	}

	public static void register(String actualName, Supplier<Item> itemSupplier) {
		var item = ITEMS.register(actualName, itemSupplier);

		CREATIVE_TAB_ITEMS.add(item);
	}
}
