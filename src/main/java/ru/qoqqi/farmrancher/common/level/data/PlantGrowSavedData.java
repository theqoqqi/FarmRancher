package ru.qoqqi.farmrancher.common.level.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;

public class PlantGrowSavedData extends SavedData {

	public static final String NAME = "PlantGrow";

	private final Object2FloatMap<BlockPos> progresses = new Object2FloatOpenHashMap<>();

	public PlantGrowSavedData() {
		super();
	}

	public PlantGrowSavedData(@Nonnull CompoundTag nbt) {
		super();
		read(nbt);
	}

	public void read(@Nonnull CompoundTag nbt) {
		readProgresses(nbt);
	}

	private void readProgresses(@Nonnull CompoundTag nbt) {
		var list = nbt.getList("Progresses", Tag.TAG_COMPOUND);

		progresses.clear();

		for (int i = 0; i < list.size(); i++) {
			var entry = list.getCompound(i);
			var blockPos = BlockPos.of(entry.getLong("Pos"));
			var progress = entry.getFloat("Progress");

			progresses.put(blockPos, progress);
		}
	}

	@Nonnull
	@Override
	public CompoundTag save(@Nonnull CompoundTag nbt) {
		writeProgresses(nbt);

		return nbt;
	}

	private void writeProgresses(@Nonnull CompoundTag nbt) {
		var list = new ListTag();

		progresses.forEach((blockPos, progress) -> {
			var entry = new CompoundTag();

			entry.putLong("Pos", blockPos.asLong());
			entry.putFloat("Progress", progress);

			list.add(entry);
		});

		nbt.put("Progresses", list);
	}

	public boolean hasProgress(BlockPos blockPos) {
		return progresses.containsKey(blockPos);
	}

	public float getProgress(BlockPos blockPos) {
		return progresses.getFloat(blockPos);
	}

	public void removeProgress(BlockPos blockPos) {
		progresses.removeFloat(blockPos);
		setDirty();
	}

	public void setProgress(BlockPos blockPos, float progress) {
		progresses.put(blockPos.immutable(), progress);
		setDirty();
	}

	public static PlantGrowSavedData getInstance(ServerLevel level) {
		return level.getDataStorage().computeIfAbsent(
				PlantGrowSavedData::new,
				PlantGrowSavedData::new,
				NAME
		);
	}
}
