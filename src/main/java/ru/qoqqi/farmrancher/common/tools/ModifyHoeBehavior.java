package ru.qoqqi.farmrancher.common.tools;

import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import ru.qoqqi.farmrancher.FarmRancher;

@Mod.EventBusSubscriber(modid = FarmRancher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModifyHoeBehavior {

	private static final Logger LOGGER = LogUtils.getLogger();

	private static final Map<Tier, TierProps> tiers = new HashMap<>();

	static {
		tiers.put(Tiers.WOOD, new TierProps(5, 0));
		tiers.put(Tiers.STONE, new TierProps(4, 0));
		tiers.put(Tiers.IRON, new TierProps(3, 0));
		tiers.put(Tiers.GOLD, new TierProps(2, 0));
		tiers.put(Tiers.DIAMOND, new TierProps(2, 0));
		tiers.put(Tiers.NETHERITE, new TierProps(1, 1));
	}

	@SubscribeEvent
	public static void onUse(BlockEvent.BlockToolModificationEvent event) {

		if (event.getToolAction() != ToolActions.HOE_TILL) {
			return;
		}

		var player = event.getPlayer();

		if (player == null) {
			return;
		}

		if (player.isCreative()) {
			return;
		}

		//noinspection deprecation
		var tillables = HoeItem.TILLABLES;
		var blockState = event.getState();
		var block = blockState.getBlock();

		if (!tillables.containsKey(block)) {
			return;
		}

		var level = event.getLevel();
		var blockPos = event.getPos();
		var itemStack = event.getHeldItemStack();
		var item = (HoeItem) itemStack.getItem();
		var tier = item.getTier();
		var tierProps = tiers.get(tier);

		var succeeded = tierProps.useSucceeded(itemStack);

		if (!succeeded) {
			event.setFinalState(null);

			var hand = event.getContext().getHand();

			fakeUse(level, player, blockPos, itemStack, hand);

			return;
		}

		if (player.isSecondaryUseActive()) {
			// Даем игроку делать одиночные вскапывания, используя Shift.
			return;
		}

		tierProps.getPositionsToApply(blockPos).forEachOrdered(pos -> {
			if (tillables.containsKey(level.getBlockState(pos).getBlock())) {
				freeUse(level, player, pos, Blocks.FARMLAND.defaultBlockState());
			}
		});
	}

	private static void fakeUse(
			LevelAccessor level,
			Player player,
			BlockPos blockPos,
			ItemStack itemStack,
			InteractionHand hand
	) {
		level.playSound(player, blockPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);

		itemStack.hurtAndBreak(1, player, innerPlayer -> {
			innerPlayer.broadcastBreakEvent(hand);
		});
	}

	private static void freeUse(
			LevelAccessor level,
			Player player,
			BlockPos blockPos,
			BlockState blockState
	) {
		level.setBlock(blockPos, blockState, 11);
		level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, blockState));
	}

	private static class TierProps {

		private final int usesToSuccess;

		private final int radius;

		private TierProps(int usesToSuccess, int radius) {
			this.usesToSuccess = usesToSuccess;
			this.radius = radius;
		}

		private boolean useSucceeded(ItemStack itemStack) {
			var itemDamage = itemStack.getDamageValue();

			return (itemDamage + 1) % usesToSuccess == 0;
		}

		private Stream<BlockPos> getPositionsToApply(BlockPos center) {
			var area = BoundingBox.fromCorners(center, center).inflatedBy(radius);

			return BlockPos.betweenClosedStream(area)
					.filter(pos -> !Objects.equals(pos, center));
		}
	}
}
