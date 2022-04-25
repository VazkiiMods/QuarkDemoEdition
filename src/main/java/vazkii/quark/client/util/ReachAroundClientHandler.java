package vazkii.quark.client.util;

import com.mojang.blaze3d.platform.Window;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import vazkii.quark.common.util.ReacharoundHandler;

@Environment(EnvType.CLIENT)
public class ReachAroundClientHandler {
	private static ReacharoundHandler.ReacharoundTarget currentTarget;
	private static int ticksDisplayed = 0;

	@Environment(EnvType.CLIENT)
	public static void init() {
		ClientTickEvents.END_WORLD_TICK.register(level -> {
			currentTarget = null;
			Player player = Minecraft.getInstance().player;
			if (player != null) {
				currentTarget = ReacharoundHandler.getPlayerReacharoundTarget(player);
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
				String text = currentTarget.direction().getAxis() == Direction.Axis.Y ? "[  ]" : "<  >";
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
}
