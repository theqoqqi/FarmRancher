package ru.qoqqi.farmrancher.client;

import com.mojang.logging.LogUtils;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import org.slf4j.Logger;

import ru.qoqqi.farmrancher.FarmRancher;

@Mod.EventBusSubscriber(modid = FarmRancher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

	private static final Logger LOGGER = LogUtils.getLogger();

	@SubscribeEvent
	public static void init(FMLClientSetupEvent event) {
		LOGGER.info("Client setup...");
	}
}
