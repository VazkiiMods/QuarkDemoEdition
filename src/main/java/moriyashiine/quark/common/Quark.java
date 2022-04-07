package moriyashiine.quark.common;

import moriyashiine.quark.common.registry.ModBlocks;
import moriyashiine.quark.common.registry.ModEntityTypes;
import moriyashiine.quark.common.registry.ModItems;
import moriyashiine.quark.common.registry.ModSoundEvents;
import moriyashiine.quark.common.util.ReacharoundHandler;
import net.fabricmc.api.ModInitializer;

public class Quark implements ModInitializer {
	public static final String MOD_ID = "quark";

	@Override
	public void onInitialize() {
		ModSoundEvents.init();
		ModEntityTypes.init();
		ModBlocks.init();
		ModItems.init();
		ReacharoundHandler.initCommon();
	}
}
