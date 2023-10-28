package ru.qoqqi.farmrancher.common.loot;

import com.mojang.serialization.Codec;

import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import ru.qoqqi.farmrancher.FarmRancher;

public class GlobalLootModifiers {

	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLMS =
			DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, FarmRancher.MOD_ID);

	public static final RegistryObject<Codec<ReplacePlantLootModifier>> REPLACE_PLANT_LOOT =
			GLMS.register("replace_plant_loot", () -> ReplacePlantLootModifier.CODEC);

	public static void register(IEventBus eventBus) {
		GLMS.register(eventBus);
	}
}
