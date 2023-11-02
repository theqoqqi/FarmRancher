package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import ru.qoqqi.farmrancher.common.items.ModItems;

class WorkshopTrades {

	private static final WorkshopTrades INSTANCE = new WorkshopTrades();

	private static final int INFINITE_MAX_USES = 999;

	private MerchantOffers offers;

	public static MerchantOffers createOffers() {
		INSTANCE.offers = new MerchantOffers();
		INSTANCE.addOffers();

		return INSTANCE.offers;
	}

	public void addOffers() {
		addOffer(ModItems.SILVER_COIN.get(), 1, ModItems.ANCIENT_SEED.get(), 1);
		addOffer(ModItems.COPPER_COIN.get(), 1, ModItems.GOLDEN_TIER_BLUEPRINT.get(), 1);
		addOffer(ModItems.COPPER_COIN.get(), 8, ModItems.STONE_TIER_BLUEPRINT.get(), 1);
		addOffer(ModItems.SILVER_COIN.get(), 1, ModItems.IRON_TIER_BLUEPRINT.get(), 1);
		addOffer(ModItems.SILVER_COIN.get(), 8, ModItems.DIAMOND_TIER_BLUEPRINT.get(), 1);
		addOffer(ModItems.GOLDEN_COIN.get(), 1, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1);
	}

	public void addOffer(Item from, int fromCount, Item to, int toCount) {
		offers.add(new MerchantOffer(
				new ItemStack(from, fromCount),
				new ItemStack(to, toCount),
				INFINITE_MAX_USES,
				0,
				1f
		));
	}
}
