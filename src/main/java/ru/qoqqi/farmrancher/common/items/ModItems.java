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

	public static final RegistryObject<Item> COPPER_COIN = registerCoin("copper_coin", CoinItem.Tier.COPPER);

	public static final RegistryObject<Item> SILVER_COIN = registerCoin("silver_coin", CoinItem.Tier.SILVER);

	public static final RegistryObject<Item> GOLDEN_COIN = registerCoin("golden_coin", CoinItem.Tier.GOLDEN);

	public static final RegistryObject<Item> ANCIENT_SEED = register("ancient_seed", new Item.Properties());

	public static final RegistryObject<Item> STONE_TIER_BLUEPRINT = register("stone_tier_blueprint", new Item.Properties());

	public static final RegistryObject<Item> IRON_TIER_BLUEPRINT = register("iron_tier_blueprint", new Item.Properties());

	public static final RegistryObject<Item> GOLDEN_TIER_BLUEPRINT = register("golden_tier_blueprint", new Item.Properties());

	public static final RegistryObject<Item> DIAMOND_TIER_BLUEPRINT = register("diamond_tier_blueprint", new Item.Properties());

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

	private static RegistryObject<Item> registerCoin(String name, CoinItem.Tier tier) {
		return register(name, () -> new CoinItem(tier, new Item.Properties()));
	}

	public static void registerBlockItem(String actualName, Supplier<? extends Block> blockSupplier) {
		var properties = new Item.Properties();

		register(actualName, () -> new BlockItem(blockSupplier.get(), properties));
	}

	public static RegistryObject<Item> register(String actualName, Item.Properties properties) {
		return register(actualName, () -> new Item(properties));
	}

	public static RegistryObject<Item> register(String actualName, Supplier<Item> itemSupplier) {
		var item = ITEMS.register(actualName, itemSupplier);

		CREATIVE_TAB_ITEMS.add(item);

		return item;
	}
}
