package vazkii.quark.common.util;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import com.mojang.blaze3d.platform.Window;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

public class ReacharoundHandler {
	private static ReacharoundTarget currentTarget;
	private static int ticksDisplayed = 0;

	public static void initCommon() {
		UseItemCallback.EVENT.register((player, level, hand) -> {
			ItemStack stack = player.getItemInHand(hand);
			ReacharoundTarget target = getPlayerReacharoundTarget(player);
			if (target != null && hand == target.hand && player.mayUseItemAt(target.pos, target.direction, stack) && stack.useOn(new UseOnContext(player, hand, new BlockHitResult(new Vec3(0.5, 1, 0.5), target.direction, target.pos, false))) != InteractionResult.PASS) {
				return InteractionResultHolder.success(stack);
			}
			return InteractionResultHolder.pass(stack);
		});
	}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		ClientTickEvents.END_WORLD_TICK.register(level -> {
			currentTarget = null;
			Player player = Minecraft.getInstance().player;
			if (player != null) {
				currentTarget = getPlayerReacharoundTarget(player);
			}
			if (currentTarget != null) {
				if (ticksDisplayed < 5) {
					ticksDisplayed++;
				}
			} else {
				ticksDisplayed = 0;
			}
		});
		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			Minecraft client = Minecraft.getInstance();
			if (client.options.hideGui) {
				return;
			}
			Player player = client.player;
			if (player != null && currentTarget != null) {
				Window window = client.getWindow();
				String text = currentTarget.direction.getAxis() == Direction.Axis.Y ? "[  ]" : "<  >";
				matrixStack.pushPose();
				matrixStack.translate(window.getGuiScaledWidth() / 2F, window.getGuiScaledHeight() / 2F - 4, 0);
				float scale = (float) Math.pow(Math.min(5, ticksDisplayed + tickDelta) / 5F, 2);
				matrixStack.scale(scale, 1, 1);
				matrixStack.translate(-client.font.width(text) / 2f, 0, 0);
				client.font.draw(matrixStack, text, 0, 0, 0xffffff | ((int) (255 * scale)) << 24);
				matrixStack.popPose();
			}
		});
	}

	private static ReacharoundTarget getPlayerReacharoundTarget(Player player) {
		InteractionHand hand = null;
		if (player.getMainHandItem().getItem() instanceof BlockItem) {
			hand = InteractionHand.MAIN_HAND;
		} else if (player.getOffhandItem().getItem() instanceof BlockItem) {
			hand = InteractionHand.OFF_HAND;
		}
		if (hand == null) {
			return null;
		}
		Level level = player.level;
		Pair<Vec3, Vec3> params = getEntityParams(player);
		Vec3 rayPos = params.getLeft();
		Vec3 ray = params.getRight().scale(ReachEntityAttributes.getReachDistance(player, 5));
		HitResult hitResult = rayTrace(player, level, rayPos, ray, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE);
		if (hitResult.getType() == HitResult.Type.MISS) {
			ReacharoundTarget target = getPlayerVerticalReacharoundTarget(player, hand, level, rayPos, ray);
			if (target != null) {
				return target;
			}
			target = getPlayerHorizontalReacharoundTarget(player, hand, level, rayPos, ray);
			return target;
		}
		return null;
	}

	private static ReacharoundTarget getPlayerVerticalReacharoundTarget(Player player, InteractionHand hand, Level level, Vec3 rayPos, Vec3 ray) {
		if (player.getXRot() < 0) {
			return null;
		}
		rayPos = rayPos.add(0, 0.5, 0);
		HitResult hitResult = rayTrace(player, level, rayPos, ray, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE);
		if (hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHitResult) {
			BlockPos pos = blockHitResult.getBlockPos().below();
			if (player.getY() - pos.getY() > 1 && level.getBlockState(pos).getMaterial().isReplaceable()) {
				return new ReacharoundTarget(pos, Direction.DOWN, hand);
			}
		}
		return null;
	}

	private static ReacharoundTarget getPlayerHorizontalReacharoundTarget(Player player, InteractionHand hand, Level level, Vec3 rayPos, Vec3 ray) {
		Direction direction = Direction.fromYRot(player.getYRot());
		rayPos = rayPos.subtract(direction.getStepX() * 0.5, 0, direction.getStepZ() * 0.5);
		HitResult hitResult = rayTrace(player, level, rayPos, ray, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE);
		if (hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHitResult) {
			BlockPos pos = blockHitResult.getBlockPos().relative(direction);
			if (level.getBlockState(pos).getMaterial().isReplaceable()) {
				return new ReacharoundTarget(pos, direction.getOpposite(), hand);
			}
		}
		return null;
	}

	public static HitResult rayTrace(Entity entity, Level level, Vec3 startPos, Vec3 ray, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {
		return level.clip(new ClipContext(startPos, startPos.add(ray), blockMode, fluidMode, entity));
	}

	public static Pair<Vec3, Vec3> getEntityParams(Entity player) {
		double y = player.getY();
		if (player instanceof Player) {
			y += player.getEyeHeight(player.getPose());
		}
		Vec3 rayPos = new Vec3(player.getX(), y, player.getZ());
		float zYaw = -Mth.cos(player.getYRot() * (float) Math.PI / 180);
		float xYaw = Mth.sin(player.getYRot() * (float) Math.PI / 180);
		float pitchMod = -Mth.cos(player.getXRot() * (float) Math.PI / 180);
		float azimuth = -Mth.sin(player.getXRot() * (float) Math.PI / 180);
		float xLen = xYaw * pitchMod;
		float yLen = zYaw * pitchMod;
		Vec3 ray = new Vec3(xLen, azimuth, yLen);
		return Pair.of(rayPos, ray);
	}

	private record ReacharoundTarget(BlockPos pos, Direction direction, InteractionHand hand) {
	}
}
