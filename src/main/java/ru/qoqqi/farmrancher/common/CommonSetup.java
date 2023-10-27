package ru.qoqqi.farmrancher.common;

import com.mojang.logging.LogUtils;

import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import org.slf4j.Logger;

import ru.qoqqi.farmrancher.FarmRancher;

@Mod.EventBusSubscriber(modid = FarmRancher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {

	private static final Logger LOGGER = LogUtils.getLogger();

	@SubscribeEvent
	public static void init(final FMLCommonSetupEvent event) {
		LOGGER.info("Common setup...");
	}

	@SubscribeEvent
	public void onServerStarting(final ServerStartingEvent event) {
		LOGGER.info("Starting server...");
	}
}
