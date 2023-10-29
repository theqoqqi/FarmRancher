package ru.qoqqi.farmrancher.common.blocks.entities;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;

import ru.qoqqi.farmrancher.FarmRancher;
import ru.qoqqi.farmrancher.common.blocks.ModBlocks;

public class ModBlockEntityTypes {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES
			= DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FarmRancher.MOD_ID);

	public static final RegistryObject<BlockEntityType<GardenBlockEntity>> GARDEN = register(
			"garden",
			GardenBlockEntity::new,
			ModBlocks.WOODEN_GARDEN,
			ModBlocks.IRON_GARDEN,
			ModBlocks.GOLDEN_GARDEN,
			ModBlocks.DIAMOND_GARDEN,
			ModBlocks.NETHERITE_GARDEN
	);

	public static final RegistryObject<BlockEntityType<TradingBlockEntity>> MERCHANT = register(
			"merchant",
			TradingBlockEntity::new,
			ModBlocks.EXCHANGER
	);

	@SuppressWarnings("SameParameterValue")
	@SafeVarargs
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(
			String name,
			BlockEntityType.BlockEntitySupplier<T> supplier,
			RegistryObject<Block>... validBlocks
	) {
		return BLOCK_ENTITY_TYPES.register(name, () -> {
			Block[] blocks = Arrays.stream(validBlocks).map(RegistryObject::get).toArray(Block[]::new);

			//noinspection ConstantConditions
			return BlockEntityType.Builder.of(supplier, blocks).build(null);
		});
	}

	public static void register(IEventBus eventBus) {
		BLOCK_ENTITY_TYPES.register(eventBus);
	}
}
