package moriyashiine.quark.common.registry;

import moriyashiine.quark.common.Quark;
import moriyashiine.quark.common.item.PickarangItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

public class ModItems {
	public static final Item GRAVISAND = new BlockItem(ModBlocks.GRAVISAND, new FabricItemSettings().group(CreativeModeTab.TAB_REDSTONE));

	public static final Item PICKARANG = new PickarangItem(new FabricItemSettings().group(CreativeModeTab.TAB_TOOLS).durability(Tiers.DIAMOND.getUses()), false);
	public static final Item FLAMERANG = new PickarangItem(new FabricItemSettings().group(CreativeModeTab.TAB_TOOLS).durability(Tiers.NETHERITE.getUses()), true);

	public static final Item TORETOISE_SPAWN_EGG = new SpawnEggItem(ModEntityTypes.TORETOISE, 0x55413B, 0x383237, new FabricItemSettings().group(CreativeModeTab.TAB_MISC));

	public static void init() {
		Registry.register(Registry.ITEM, new ResourceLocation(Quark.MOD_ID, "gravisand"), GRAVISAND);
		Registry.register(Registry.ITEM, new ResourceLocation(Quark.MOD_ID, "pickarang"), PICKARANG);
		Registry.register(Registry.ITEM, new ResourceLocation(Quark.MOD_ID, "flamerang"), FLAMERANG);
		Registry.register(Registry.ITEM, new ResourceLocation(Quark.MOD_ID, "toretoise_spawn_egg"), TORETOISE_SPAWN_EGG);
	}
}
