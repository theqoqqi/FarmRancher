package ru.qoqqi.farmrancher;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

import ru.qoqqi.farmrancher.common.Config;
import ru.qoqqi.farmrancher.common.blocks.ModBlocks;
import ru.qoqqi.farmrancher.common.blocks.entities.ModBlockEntityTypes;
import ru.qoqqi.farmrancher.common.creative.tabs.ModCreativeTabs;
import ru.qoqqi.farmrancher.common.items.ModItems;
import ru.qoqqi.farmrancher.common.loot.GlobalLootModifiers;

@Mod(FarmRancher.MOD_ID)
public class FarmRancher {

	public static final String MOD_ID = "farm_rancher";

	private static final Logger LOGGER = LogUtils.getLogger();

	public FarmRancher() {
		var eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModBlocks.register(eventBus);
		ModBlockEntityTypes.register(eventBus);
		ModItems.register(eventBus);
		ModCreativeTabs.register(eventBus);

		GlobalLootModifiers.register(eventBus);

		MinecraftForge.EVENT_BUS.register(this);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}
}
