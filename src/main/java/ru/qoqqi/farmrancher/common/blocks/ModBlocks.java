package ru.qoqqi.farmrancher.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.function.Supplier;

import ru.qoqqi.farmrancher.FarmRancher;
import ru.qoqqi.farmrancher.common.gardens.GardenType;
import ru.qoqqi.farmrancher.common.gardens.GardenTypes;
import ru.qoqqi.farmrancher.common.items.ModItems;
import ru.qoqqi.farmrancher.common.trading.OfferListFactory;
import ru.qoqqi.farmrancher.common.trading.Trades;

public class ModBlocks {

	public static final DeferredRegister<Block> BLOCKS
			= DeferredRegister.create(ForgeRegistries.BLOCKS, FarmRancher.MOD_ID);

	@SuppressWarnings("unused")
	public static final RegistryObject<Block> WOODEN_GARDEN = registerGarden("wooden_garden", GardenTypes.WOODEN_GARDEN);

	@SuppressWarnings("unused")
	public static final RegistryObject<Block> IRON_GARDEN = registerGarden("iron_garden", GardenTypes.IRON_GARDEN);

	@SuppressWarnings("unused")
	public static final RegistryObject<Block> GOLDEN_GARDEN = registerGarden("golden_garden", GardenTypes.GOLDEN_GARDEN);

	@SuppressWarnings("unused")
	public static final RegistryObject<Block> DIAMOND_GARDEN = registerGarden("diamond_garden", GardenTypes.DIAMOND_GARDEN);

	@SuppressWarnings("unused")
	public static final RegistryObject<Block> NETHERITE_GARDEN = registerGarden("netherite_garden", GardenTypes.NETHERITE_GARDEN);

	@SuppressWarnings("unused")
	public static final RegistryObject<Block> EXCHANGER = registerTradingBlock("exchanger", Trades.EXCHANGER);

	@SuppressWarnings("unused")
	public static final RegistryObject<Block> MARKET = registerTradingBlock("market", Trades.MARKET);

	@SuppressWarnings("unused")
	public static final RegistryObject<Block> CAFETERIA = registerTradingBlock("cafeteria", Trades.CAFETERIA);

	@SuppressWarnings("unused")
	public static final RegistryObject<Block> RESTAURANT = registerTradingBlock("restaurant", Trades.RESTAURANT);

	@SuppressWarnings("unused")
	public static final RegistryObject<Block> WORKSHOP = registerTradingBlock("workshop", Trades.WORKSHOP);

	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}

	@SuppressWarnings("SameParameterValue")
	private static RegistryObject<Block> registerTradingBlock(
			String name,
			OfferListFactory offerListFactory
	) {
		var properties = BlockBehaviour.Properties.of()
				.strength(1.0f, 5.0f)
				.sound(SoundType.WOOD)
				.mapColor(MapColor.WOOD);

		return register(name, () -> new TradingBlock(offerListFactory, properties));
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

		return register(name, blockSupplier, true);
	}

	@SuppressWarnings("SameParameterValue")
	private static <T extends Block> RegistryObject<T> register(
			String name, Supplier<T> blockSupplier,
			boolean hasItemBlock) {

		final String actualName = name.toLowerCase(Locale.ROOT);
		final RegistryObject<T> block = BLOCKS.register(actualName, blockSupplier);

		if (hasItemBlock) {
			ModItems.registerBlockItem(actualName, block);
		}

		return block;
	}
}
