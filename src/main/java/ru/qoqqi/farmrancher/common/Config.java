package ru.qoqqi.farmrancher.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import ru.qoqqi.farmrancher.FarmRancher;

@Mod.EventBusSubscriber(modid = FarmRancher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	public static final ForgeConfigSpec SPEC = BUILDER.build();

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event) {

	}
}
