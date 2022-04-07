package moriyashiine.quark.common.registry;

import moriyashiine.quark.common.Quark;
import moriyashiine.quark.common.block.GravisandBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ModBlocks {
	public static final Block GRAVISAND = new GravisandBlock(FabricBlockSettings.copyOf(Blocks.SAND));

	public static void init() {
		Registry.register(Registry.BLOCK, new ResourceLocation(Quark.MOD_ID, "gravisand"), GRAVISAND);
	}
}
