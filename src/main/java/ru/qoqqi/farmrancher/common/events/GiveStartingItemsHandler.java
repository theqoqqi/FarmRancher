package ru.qoqqi.farmrancher.common.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.jetbrains.annotations.NotNull;

import ru.qoqqi.farmrancher.FarmRancher;
import ru.qoqqi.farmrancher.common.items.ModItems;

@Mod.EventBusSubscriber(modid = FarmRancher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GiveStartingItemsHandler {

	@SubscribeEvent
	public static void onEntityJoinLevel(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}

		if (!shouldGive(player)) {
			return;
		}

		var nbt = getPersistedNbt(player);

		if (nbt.getBoolean("gotMineRancherStartingItems")) {
			return;
		}

		nbt.putBoolean("gotMineRancherStartingItems", true);

		var itemStack = new ItemStack(ModItems.ANCIENT_SEED.get());

		player.getInventory().add(itemStack);
	}

	private static boolean shouldGive(ServerPlayer player) {
		var server = player.getServer();

		if (server == null) {
			return false;
		}

		return !server.getWorldData().worldGenOptions().generateBonusChest();
	}

	@NotNull
	private static CompoundTag getPersistedNbt(ServerPlayer player) {
		var persistentData = player.getPersistentData();

		if (!persistentData.contains(Player.PERSISTED_NBT_TAG)) {
			persistentData.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
		}

		return persistentData.getCompound(Player.PERSISTED_NBT_TAG);
	}
}
