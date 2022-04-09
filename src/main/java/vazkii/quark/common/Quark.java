package vazkii.quark.common;

import vazkii.quark.common.registry.ModBlocks;
import vazkii.quark.common.registry.ModEntityTypes;
import vazkii.quark.common.registry.ModItems;
import vazkii.quark.common.registry.ModSoundEvents;
import vazkii.quark.common.util.ReacharoundHandler;
import net.fabricmc.api.ModInitializer;

public class Quark implements ModInitializer {
	public static final String MOD_ID = "quarkdemo";

	@Override
	public void onInitialize() {
		ModSoundEvents.init();
		ModEntityTypes.init();
		ModBlocks.init();
		ModItems.init();
		ReacharoundHandler.initCommon();
	}
}
