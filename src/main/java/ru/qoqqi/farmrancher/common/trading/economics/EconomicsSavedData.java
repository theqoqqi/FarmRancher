package ru.qoqqi.farmrancher.common.trading.economics;

import com.mojang.logging.LogUtils;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.saveddata.SavedData;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import ru.qoqqi.farmrancher.FarmRancher;

public class EconomicsSavedData extends SavedData {

	private static final Logger LOGGER = LogUtils.getLogger();

	public static final String NAME = FarmRancher.MOD_ID + "_Economics";

	private final Object2DoubleMap<Item> prices = new Object2DoubleOpenHashMap<>();

	private final ServerLevel level;

	public EconomicsSavedData(ServerLevel level) {
		super();

		this.level = level;
	}

	public EconomicsSavedData(ServerLevel level, @Nonnull CompoundTag nbt) {
		super();

		this.level = level;

		read(nbt);
	}

	public void read(@Nonnull CompoundTag nbt) {
		readPrices(nbt);
	}

	private void readPrices(@Nonnull CompoundTag nbt) {
		var list = nbt.getList("Prices", Tag.TAG_COMPOUND);

		prices.clear();

		var itemRegistry = getItemRegistry();

		for (int i = 0; i < list.size(); i++) {
			var entry = list.getCompound(i);
			var item = itemRegistry.get(new ResourceLocation(entry.getString("Item")));
			var price = entry.getDouble("Price");

			if (item != null) {
				prices.put(item, price);
			}
		}
	}

	@Nonnull
	@Override
	public CompoundTag save(@Nonnull CompoundTag nbt) {
		LOGGER.info("SAVE PRICES");
		writePrices(nbt);

		return nbt;
	}

	private void writePrices(@Nonnull CompoundTag nbt) {
		var list = new ListTag();

		var itemRegistry = getItemRegistry();

		prices.forEach((item, price) -> {
			var entry = new CompoundTag();
			var itemKey = itemRegistry.getKey(item);

			if (itemKey != null) {
				entry.putString("Item", itemKey.toString());
				entry.putDouble("Price", price);

				list.add(entry);
			}
		});

		nbt.put("Prices", list);
	}

	@NotNull
	private Registry<Item> getItemRegistry() {
		return level.registryAccess().registry(Registries.ITEM).orElseThrow();
	}

	public boolean hasPrice(Item item) {
		return prices.containsKey(item);
	}

	public double getPrice(Item item) {
		return prices.getDouble(item);
	}

	public void removePrice(Item item) {
		prices.removeDouble(item);
		setDirty();
	}

	public void setPrice(Item item, double price) {
		prices.put(item, price);
		setDirty();
	}

	public static EconomicsSavedData getInstance(ServerLevel level) {
		return level.getDataStorage().computeIfAbsent(
				nbt -> new EconomicsSavedData(level, nbt),
				() -> new EconomicsSavedData(level),
				NAME
		);
	}
}
