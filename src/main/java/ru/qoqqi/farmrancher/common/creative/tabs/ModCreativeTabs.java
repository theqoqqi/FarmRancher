package ru.qoqqi.farmrancher.common.creative.tabs;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import ru.qoqqi.farmrancher.FarmRancher;
import ru.qoqqi.farmrancher.common.items.ModItems;

public class ModCreativeTabs {

	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FarmRancher.MOD_ID);

	public static final RegistryObject<CreativeModeTab> TAB_FARM_RANCHER = CREATIVE_TABS.register(FarmRancher.MOD_ID,
			() -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.farm_rancher"))
					.icon(() -> new ItemStack(Items.WHEAT))
					.displayItems((parameters, output) -> ModItems.CREATIVE_TAB_ITEMS.forEach((item) -> output.accept(item.get())))
					.build());

	public static void register(IEventBus eventBus) {
		CREATIVE_TABS.register(eventBus);
	}
}
