package ru.qoqqi.farmrancher.dev;

import com.mojang.logging.LogUtils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.slf4j.Logger;

import java.util.List;

import ru.qoqqi.farmrancher.FarmRancher;
import ru.qoqqi.farmrancher.common.trading.util.PriceCalculator;
import ru.qoqqi.farmrancher.common.trading.util.Sellable;
import ru.qoqqi.farmrancher.common.trading.Sellables;
import ru.qoqqi.farmrancher.common.trading.SimpleIngredients;

@Mod.EventBusSubscriber(modid = FarmRancher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Start {

	private static final Logger LOGGER = LogUtils.getLogger();

	@SubscribeEvent
	public static void onEntityJoinLevel(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}

		var level = (ServerLevel) player.level();
		var calculator = new PriceCalculator(
				level,
				SimpleIngredients.PRICES,
				Sellables.RECIPE_PRICE_BONUSES,
				Sellables.UNIQUE_INGREDIENTS_BONUS
		);
		var resolver = new IngredientListResolver(level, SimpleIngredients.PRICES, calculator);

		logPrices("FRUITS", Sellables.FRUITS, calculator, resolver);
		logPrices("BUFFET", Sellables.BUFFET, calculator, resolver);
		logPrices("CAFETERIA", Sellables.CAFETERIA, calculator, resolver);
		logPrices("CONFECTIONERY", Sellables.CONFECTIONERY, calculator, resolver);
		logPrices("RESTAURANT", Sellables.RESTAURANT, calculator, resolver);
	}

	private static void logPrices(String title, List<Sellable> sellables, PriceCalculator calculator, IngredientListResolver resolver) {
		LOGGER.info("{}:", title);
		sellables.forEach(sellable -> {
			logPrice(sellable.item, calculator, resolver);
		});
	}

	private static void logPrice(Item item, PriceCalculator calculator, IngredientListResolver resolver) {
		var itemStack = new ItemStack(item);
		var price = calculator.getPrice(itemStack);
		var doublePrice = resolver.getIngredientList(itemStack);

		LOGGER.info("CALCULATED PRICE: {} -> {} {}", doublePrice, item, price);
	}
}
