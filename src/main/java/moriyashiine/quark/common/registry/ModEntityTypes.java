package moriyashiine.quark.common.registry;

import moriyashiine.quark.common.Quark;
import moriyashiine.quark.common.entity.Gravisand;
import moriyashiine.quark.common.entity.Toretoise;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public class ModEntityTypes {
	public static final EntityType<Gravisand> GRAVISAND = FabricEntityTypeBuilder.<Gravisand>create(MobCategory.MISC, Gravisand::new).dimensions(EntityType.FALLING_BLOCK.getDimensions()).trackRangeChunks(10).trackedUpdateRate(20).build();

	public static final EntityType<Toretoise> TORETOISE = FabricEntityTypeBuilder.create(MobCategory.MONSTER, Toretoise::new).dimensions(EntityDimensions.fixed(2, 1)).trackRangeChunks(8).fireImmune().build();

	public static void init() {
		Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Quark.MOD_ID, "gravisand"), GRAVISAND);
		FabricDefaultAttributeRegistry.register(TORETOISE, Toretoise.createToretoiseAttributes());
		Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Quark.MOD_ID, "toretoise"), TORETOISE);
		BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), MobCategory.MONSTER, TORETOISE, 120, 2, 4);
		SpawnRestrictionAccessor.callRegister(TORETOISE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Toretoise::canSpawn);
	}
}
