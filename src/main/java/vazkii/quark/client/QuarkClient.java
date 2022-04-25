package vazkii.quark.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import vazkii.quark.client.model.entity.living.ToretoiseEntityModel;
import vazkii.quark.client.render.entity.PickarangRenderer;
import vazkii.quark.client.render.entity.ToretoiseEntityRenderer;
import vazkii.quark.client.util.ReachAroundClientHandler;
import vazkii.quark.common.registry.ModEntityTypes;

@Environment(EnvType.CLIENT)
public class QuarkClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntityTypes.GRAVISAND, FallingBlockRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.PICKARANG, PickarangRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.TORETOISE, ToretoiseEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(ToretoiseEntityModel.TORETOISE_MODEL_LAYER, ToretoiseEntityModel::getLayerDefinition);
		ReachAroundClientHandler.init();
	}
}
