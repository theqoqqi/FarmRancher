package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import ru.qoqqi.farmrancher.common.items.CoinItem;
import ru.qoqqi.farmrancher.common.items.ModItems;
import ru.qoqqi.farmrancher.common.trading.util.Price;

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
		addAncientSeedOffer();
		addTierBlueprintOffers();
	}

	public void addAncientSeedOffer() {
		var price = getAncientSeedPrice();
		var stacks = price.asTwoStacks();

		offers.add(new MerchantOffer(
				stacks.get(0),
				stacks.get(1),
				new ItemStack(ModItems.ANCIENT_SEED.get()),
				INFINITE_MAX_USES,
				0,
				1f
		));
	}

	private Price getAncientSeedPrice() {
		return new Price(CoinItem.Tier.SILVER, 1);
	}

	private void addTierBlueprintOffers() {
		addOffer(
				ModItems.GOLDEN_TIER_BLUEPRINT.get(),
				new Price(CoinItem.Tier.COPPER, 1)
		);
		addOffer(
				ModItems.STONE_TIER_BLUEPRINT.get(),
				new Price(CoinItem.Tier.COPPER, 8)
		);
		addOffer(
				ModItems.IRON_TIER_BLUEPRINT.get(),
				new Price(CoinItem.Tier.SILVER, 1)
		);
		addOffer(
				ModItems.DIAMOND_TIER_BLUEPRINT.get(),
				new Price(CoinItem.Tier.SILVER, 8)
		);
		addOffer(
				Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
				new Price(CoinItem.Tier.GOLDEN, 1)
		);
	}

	public void addOffer(Item tierBlueprintItem, Price price) {
		offers.add(new MerchantOffer(
				price.asSingleStack(),
				new ItemStack(tierBlueprintItem),
				INFINITE_MAX_USES,
				0,
				1f
		));
	}
}
