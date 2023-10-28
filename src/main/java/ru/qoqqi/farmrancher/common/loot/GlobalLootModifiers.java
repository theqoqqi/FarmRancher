package ru.qoqqi.farmrancher.common.loot;

import com.mojang.serialization.Codec;

import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import ru.qoqqi.farmrancher.FarmRancher;

public class GlobalLootModifiers {

	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLMS =
			DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, FarmRancher.MOD_ID);

	public static void register(IEventBus eventBus) {
		GLMS.register(eventBus);
	}
}
