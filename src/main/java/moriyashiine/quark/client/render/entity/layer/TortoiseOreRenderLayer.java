package moriyashiine.quark.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.quark.client.model.entity.living.ToretoiseEntityModel;
import moriyashiine.quark.common.Quark;
import moriyashiine.quark.common.entity.Toretoise;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class TortoiseOreRenderLayer extends RenderLayer<Toretoise, ToretoiseEntityModel> {
	private static ResourceLocation[] TEXTURES;

	public TortoiseOreRenderLayer(RenderLayerParent<Toretoise, ToretoiseEntityModel> context) {
		super(context);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, Toretoise entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (TEXTURES == null) {
			TEXTURES = new ResourceLocation[Toretoise.ORE_TYPES];
			for (int i = 0; i < Toretoise.ORE_TYPES; i++) {
				TEXTURES[i] = new ResourceLocation(Quark.MOD_ID, "textures/entity/living/toretoise/ore_" + (i + 1) + ".png");
			}
		}
		int ore = entity.getOreType();
		if (ore > 0 && ore < Toretoise.ORE_TYPES) {
			renderColoredCutoutModel(getParentModel(), TEXTURES[ore - 1], poseStack, multiBufferSource, light, entity, 1, 1, 1);
		}
	}
}
