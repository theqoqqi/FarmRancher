package ru.qoqqi.farmrancher;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import org.slf4j.Logger;

import ru.qoqqi.farmrancher.common.Config;

@Mod(FarmRancher.MOD_ID)
public class FarmRancher {

	public static final String MOD_ID = "farm_rancher";

	private static final Logger LOGGER = LogUtils.getLogger();

	public FarmRancher() {
		MinecraftForge.EVENT_BUS.register(this);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}
}
