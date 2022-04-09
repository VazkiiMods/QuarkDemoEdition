package vazkii.quark.client.render.entity;

import vazkii.quark.client.model.entity.living.ToretoiseEntityModel;
import vazkii.quark.client.render.entity.layer.TortoiseOreRenderLayer;
import vazkii.quark.common.Quark;
import vazkii.quark.common.entity.Toretoise;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ToretoiseEntityRenderer extends MobRenderer<Toretoise, ToretoiseEntityModel> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Quark.MOD_ID, "textures/entity/living/toretoise/base.png");

	public ToretoiseEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new ToretoiseEntityModel(context.bakeLayer(ToretoiseEntityModel.TORETOISE_MODEL_LAYER)), 1);
		addLayer(new TortoiseOreRenderLayer(this));
	}

	@Override
	public ResourceLocation getTextureLocation(Toretoise entity) {
		return TEXTURE;
	}
}
