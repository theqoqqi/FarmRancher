package ru.qoqqi.farmrancher.common.blocks.entities;

import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nullable;

import ru.qoqqi.farmrancher.client.menus.FixedMerchantMenu;
import ru.qoqqi.farmrancher.common.events.TradeWithBlockEntityEvent;

public abstract class BaseMerchantBlockEntity extends BlockEntity implements Merchant {

	private static final Logger LOGGER = LogUtils.getLogger();

	protected MerchantOffers offers;

	@Nullable
	private Player tradingPlayer;

	public BaseMerchantBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
		super(pType, pPos, pBlockState);
	}

	@Override
	public void setTradingPlayer(@Nullable Player tradingPlayer) {
		this.tradingPlayer = tradingPlayer;
	}

	@Nullable
	@Override
	public Player getTradingPlayer() {
		return tradingPlayer;
	}

	@NotNull
	@Override
	public MerchantOffers getOffers() {
		if (offers == null) {
			offers = createOffers();
		}

		return offers;
	}

	protected abstract MerchantOffers createOffers();

	protected abstract void updateOffers();

	@Override
	public void overrideOffers(@NotNull MerchantOffers offers) {
		// Empty
	}

	@Override
	public void notifyTrade(@NotNull MerchantOffer offer) {
		rewardTradeExperience(offer);
		MinecraftForge.EVENT_BUS.post(new TradeWithBlockEntityEvent(tradingPlayer, offer, this));
	}

	protected void rewardTradeExperience(MerchantOffer offer) {
		if (level == null || tradingPlayer == null || !offer.shouldRewardExp()) {
			return;
		}

		var experience = offer.getXp();
		var x = tradingPlayer.getX();
		var y = tradingPlayer.getY();
		var z = tradingPlayer.getZ();

		level.addFreshEntity(new ExperienceOrb(level, x, y, z, experience));
	}

	@Override
	public void notifyTradeUpdated(@NotNull ItemStack stack) {
		// Empty
	}

	@Override
	public int getVillagerXp() {
		return 0;
	}

	@Override
	public void overrideXp(int pXp) {
		// Empty
	}

	@Override
	public boolean showProgressBar() {
		return false;
	}

	@NotNull
	@Override
	public SoundEvent getNotifyTradeSound() {
		return SoundEvents.EMPTY;
	}

	@Override
	public void openTradingScreen(Player player, @NotNull Component displayName, int level) {
		// Метод полностью копирует поведение оригинала, за одним исключением:
		// Вместо MerchantMenu используется FixedMerchantMenu.
		// см. подробности в комментарии в FixedMerchantMenu.playTradeSound().

		var menuProvider = new SimpleMenuProvider((containerId, playerInventory, p) -> {
			return new FixedMerchantMenu(containerId, playerInventory, this);
		}, displayName);

		var containerId = player.openMenu(menuProvider);

		if (containerId.isPresent()) {
			var merchantoffers = getOffers();

			if (!merchantoffers.isEmpty()) {
				player.sendMerchantOffers(
						containerId.getAsInt(),
						merchantoffers,
						level,
						getVillagerXp(),
						showProgressBar(),
						canRestock()
				);
			}
		}
	}

	@Override
	public boolean isClientSide() {
		return level == null || level.isClientSide;
	}

	protected void resendOffersToTradingPlayer() {
		var offers = getOffers();
		var player = getTradingPlayer();

		if (player == null || offers.isEmpty()) {
			return;
		}

		var level = 0;

		player.sendMerchantOffers(
				player.containerMenu.containerId,
				offers,
				level,
				getVillagerXp(),
				showProgressBar(),
				canRestock()
		);
	}
}
