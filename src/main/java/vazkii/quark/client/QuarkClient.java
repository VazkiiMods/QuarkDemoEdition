package vazkii.quark.client;

import vazkii.quark.client.model.entity.living.ToretoiseEntityModel;
import vazkii.quark.client.render.entity.ToretoiseEntityRenderer;
import vazkii.quark.common.registry.ModEntityTypes;
import vazkii.quark.common.util.ReacharoundHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;

@Environment(EnvType.CLIENT)
public class QuarkClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntityTypes.GRAVISAND, FallingBlockRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.TORETOISE, ToretoiseEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(ToretoiseEntityModel.TORETOISE_MODEL_LAYER, ToretoiseEntityModel::getLayerDefinition);
		ReacharoundHandler.initClient();
	}
}
