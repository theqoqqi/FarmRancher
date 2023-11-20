package ru.qoqqi.farmrancher.common.blocks.entities;

import com.google.common.collect.ComparisonChain;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import ru.qoqqi.farmrancher.FarmRancher;
import ru.qoqqi.farmrancher.common.blocks.GardenBlock;
import ru.qoqqi.farmrancher.common.gardens.GardenType;
import ru.qoqqi.farmrancher.common.plants.PlantTicker;

public class GardenBlockEntity extends BlockEntity {

	private final GardenType gardenType;

	private final BoundingBox area;

	private static final Logger LOGGER = LogUtils.getLogger();

	public GardenBlockEntity(BlockPos pos, BlockState blockState) {
		this(ModBlockEntityTypes.GARDEN.get(), pos, blockState);
	}

	@Override
	public void onLoad() {
		super.onLoad();

		InstanceCache.onLoad(this);
	}

	public GardenBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
		super(blockEntityType, pos, blockState);

		gardenType = getGardenType();
		area = getArea(pos, gardenType.areaRadius);
	}

	public void tick() {
		if (level == null || level.isClientSide) {
			return;
		}

		var serverLevel = (ServerLevel) level;
		var growthSpeed = gardenType.growthSpeed;
		var positions = getPositionsToApplyTick();

		positions.forEach(blockPos -> {
			var blockState = level.getBlockState(blockPos);

			PlantTicker.tick(serverLevel, blockPos, blockState, growthSpeed);
		});
	}

	private Stream<BlockPos> getPositionsToApplyTick() {
		return BlockPos.betweenClosedStream(area)
				.filter(this::isPreferredAt);
	}

	private boolean isPreferredAt(BlockPos pos) {
		if (level == null) {
			return false;
		}

		var preferredGarden = getPreferredGarden(level, pos, GardenBlockEntity::compareGrowthSpeed);

		return this == preferredGarden;
	}

	public GardenType getGardenType() {
		var block = (GardenBlock) getBlockState().getBlock();

		return block.gardenType;
	}

	@NotNull
	private static BoundingBox getArea(BlockPos pos, int radius) {
		var from = pos.offset(-radius, -1, -radius);
		var to = pos.offset(radius, 1, radius);

		return BoundingBox.fromCorners(from, to);
	}

	private boolean isInArea(BlockPos pos) {
		return area.isInside(pos);
	}

	public static GardenBlockEntity getPreferredByProfitability(Level level, BlockPos pos) {
		return getPreferredGarden(level, pos, GardenBlockEntity::compareProfitability);
	}

	public static GardenBlockEntity getPreferredGarden(
			Level level,
			BlockPos pos,
			Comparator<? super GardenBlockEntity> compareBy
	) {
		return InstanceCache
				.getAllBlockEntities(level)
				.filter(blockEntity -> blockEntity.isInArea(pos))
				.max(compareBy)
				.orElse(null);
	}

	private static int compareGrowthSpeed(GardenBlockEntity first, GardenBlockEntity second) {
		return compare(first, second, chain -> chain.compare(
				first.gardenType.growthSpeed,
				second.gardenType.growthSpeed
		));
	}

	private static int compareProfitability(GardenBlockEntity first, GardenBlockEntity second) {
		return compare(first, second, chain -> chain.compare(
				first.gardenType.profitability,
				second.gardenType.profitability
		));
	}

	private static int compare(
			GardenBlockEntity first,
			GardenBlockEntity second,
			Function<ComparisonChain, ComparisonChain> comparer
	) {
		return comparer
				.apply(ComparisonChain.start())
				.compare(first.getBlockPos().asLong(), second.getBlockPos().asLong())
				.result();
	}

	@Mod.EventBusSubscriber(modid = FarmRancher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	private static class InstanceCache {

		private static final Map<LevelAccessor, Set<GardenBlockEntity>> byLevels = new HashMap<>();

		public static void onLoad(GardenBlockEntity blockEntity) {
			getByLevel(blockEntity.level).add(blockEntity);
		}

		@SubscribeEvent
		public static void onBreak(BlockEvent.BreakEvent event) {
			var level = event.getLevel();
			var blockState = event.getState();
			var pos = event.getPos();
			var blockEntity = getBlockEntity(level, pos, blockState);

			if (blockEntity == null) {
				return;
			}

			getByLevel(level).remove(blockEntity);
		}

		@NotNull
		private static Stream<GardenBlockEntity> getAllBlockEntities(LevelAccessor level) {
			return getByLevel(level).stream().filter(blockEntity -> !blockEntity.isRemoved());
		}

		@NotNull
		private static Set<GardenBlockEntity> getByLevel(LevelAccessor level) {
			return byLevels.computeIfAbsent(level, l -> new HashSet<>());
		}

		@Nullable
		private static GardenBlockEntity getBlockEntity(LevelAccessor level, BlockPos pos, BlockState blockState) {
			var removedBlock = blockState.getBlock();

			if (!(removedBlock instanceof GardenBlock)) {
				return null;
			}

			var blockEntity = level.getBlockEntity(pos);

			if (!(blockEntity instanceof GardenBlockEntity gardenBlockEntity)) {
				return null;
			}

			return gardenBlockEntity;
		}
	}
}
