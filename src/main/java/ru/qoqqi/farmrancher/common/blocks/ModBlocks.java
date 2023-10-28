package ru.qoqqi.farmrancher.common.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

import ru.qoqqi.farmrancher.FarmRancher;
import ru.qoqqi.farmrancher.common.gardens.GardenType;
import ru.qoqqi.farmrancher.common.gardens.GardenTypes;
import ru.qoqqi.farmrancher.common.items.ModItems;

public class ModBlocks {

	public static final DeferredRegister<Block> BLOCKS
			= DeferredRegister.create(ForgeRegistries.BLOCKS, FarmRancher.MOD_ID);

	@SuppressWarnings("unused")
	public static final RegistryObject<Block> WOODEN_GARDEN = registerGarden("wooden_garden", GardenTypes.WOODEN_GARDEN);

	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}

	@SuppressWarnings("SameParameterValue")
	private static RegistryObject<Block> registerGarden(String name, GardenType gardenType) {
		var properties = BlockBehaviour.Properties.of()
				.strength(1.0f, 5.0f)
				.sound(SoundType.WOOD)
				.mapColor(MapColor.WOOD);

		return register(name, () -> new GardenBlock(properties, gardenType));
	}

	private static <T extends Block> RegistryObject<T> register(
			String name, Supplier<T> blockSupplier) {

		return register(name, blockSupplier, new Item.Properties());
	}

	private static <T extends Block> RegistryObject<T> register(
			String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties) {

		return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties), true);
	}

	@SuppressWarnings("SameParameterValue")
	private static <T extends Block> RegistryObject<T> register(
			String name, Supplier<T> blockSupplier,
			Function<T, ? extends BlockItem> blockItemFactory, boolean hasItemBlock) {

		final String actualName = name.toLowerCase(Locale.ROOT);
		final RegistryObject<T> block = BLOCKS.register(actualName, blockSupplier);

		if (hasItemBlock) {
			ModItems.ITEMS.register(actualName, () -> blockItemFactory.apply(block.get()));
		}

		return block;
	}
}
