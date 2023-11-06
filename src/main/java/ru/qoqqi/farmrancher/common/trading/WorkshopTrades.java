package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import ru.qoqqi.farmrancher.common.blocks.entities.TradingBlockEntity;
import ru.qoqqi.farmrancher.common.items.CoinItem;
import ru.qoqqi.farmrancher.common.items.ModItems;
import ru.qoqqi.farmrancher.common.trading.economics.Economics;
import ru.qoqqi.farmrancher.common.trading.util.Price;

class WorkshopTrades {

	private static final WorkshopTrades INSTANCE = new WorkshopTrades();

	private static final Price INITIAL_ANCIENT_SEED_PRICE = new Price(8);

	private static final int INFINITE_MAX_USES = 999;

	private MerchantOffers offers;

	private ServerLevel level;

	public static MerchantOffers createOffers(TradingBlockEntity blockEntity) {
		INSTANCE.offers = new MerchantOffers();
		INSTANCE.level = (ServerLevel) blockEntity.getLevel();
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
		var experience = 100 + getPriceExperience(price);

		offers.add(new MerchantOffer(
				stacks.get(0),
				stacks.get(1),
				new ItemStack(ModItems.ANCIENT_SEED.get()),
				INFINITE_MAX_USES,
				experience,
				1f
		));
	}

	private Price getAncientSeedPrice() {
		var economics = Economics.getInstance(level);
		var boughtAncientSeeds = economics.getBoughtAncientSeeds();
		var price = (boughtAncientSeeds + 1) * INITIAL_ANCIENT_SEED_PRICE.getValue();
		var maxPrice = CoinItem.Tier.getValueOf(0, 63, 64);

		return new Price(Math.min(price, maxPrice));
	}

	private void addTierBlueprintOffers() {
		addOffer(
				ModItems.GOLDEN_TIER_BLUEPRINT.get(),
				new Price(CoinItem.Tier.COPPER, 1)
		);
		addOffer(
				ModItems.STONE_TIER_BLUEPRINT.get(),
				new Price(CoinItem.Tier.COPPER, 1)
		);
		addOffer(
				ModItems.COPPER_TIER_BLUEPRINT.get(),
				new Price(CoinItem.Tier.COPPER, 1)
		);
		addOffer(
				ModItems.IRON_TIER_BLUEPRINT.get(),
				new Price(CoinItem.Tier.COPPER, 8)
		);
		addOffer(
				ModItems.DIAMOND_TIER_BLUEPRINT.get(),
				new Price(CoinItem.Tier.SILVER, 1)
		);
		addOffer(
				Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
				new Price(CoinItem.Tier.SILVER, 8)
		);
	}

	public void addOffer(Item tierBlueprintItem, Price price) {
		offers.add(new MerchantOffer(
				price.asSingleStack(),
				new ItemStack(tierBlueprintItem),
				INFINITE_MAX_USES,
				getPriceExperience(price),
				1f
		));
	}

	public static int getPriceExperience(Price price) {
		return (int) Math.sqrt(price.getValue());
	}
}
