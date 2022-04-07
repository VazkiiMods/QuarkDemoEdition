package moriyashiine.quark.client;

import moriyashiine.quark.client.model.entity.living.ToretoiseEntityModel;
import moriyashiine.quark.client.render.entity.ToretoiseEntityRenderer;
import moriyashiine.quark.common.registry.ModEntityTypes;
import moriyashiine.quark.common.util.ReacharoundHandler;
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
