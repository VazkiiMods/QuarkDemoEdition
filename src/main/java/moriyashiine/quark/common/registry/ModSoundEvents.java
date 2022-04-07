package moriyashiine.quark.common.registry;

import moriyashiine.quark.common.Quark;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents {
	public static final SoundEvent ENTITY_TORETOISE_AMBIENT = new SoundEvent(new ResourceLocation(Quark.MOD_ID, "entity.toretoise.ambient"));
	public static final SoundEvent ENTITY_TORETOISE_HURT = new SoundEvent(new ResourceLocation(Quark.MOD_ID, "entity.toretoise.hurt"));
	public static final SoundEvent ENTITY_TORETOISE_DEATH = new SoundEvent(new ResourceLocation(Quark.MOD_ID, "entity.toretoise.death"));
	public static final SoundEvent ENTITY_TORETOISE_EAT = new SoundEvent(new ResourceLocation(Quark.MOD_ID, "entity.toretoise.eat"));
	public static final SoundEvent ENTITY_TORETOISE_EAT_SATIATED = new SoundEvent(new ResourceLocation(Quark.MOD_ID, "entity.toretoise.eat_satiated"));
	public static final SoundEvent ENTITY_TORETOISE_ANGRY = new SoundEvent(new ResourceLocation(Quark.MOD_ID, "entity.toretoise.angry"));
	public static final SoundEvent ENTITY_TORETOISE_HARVEST = new SoundEvent(new ResourceLocation(Quark.MOD_ID, "entity.toretoise.harvest"));
	public static final SoundEvent ENTITY_TORETOISE_REGROW = new SoundEvent(new ResourceLocation(Quark.MOD_ID, "entity.toretoise.regrow"));

	public static void init() {
		Registry.register(Registry.SOUND_EVENT, ENTITY_TORETOISE_AMBIENT.getLocation(), ENTITY_TORETOISE_AMBIENT);
		Registry.register(Registry.SOUND_EVENT, ENTITY_TORETOISE_HURT.getLocation(), ENTITY_TORETOISE_HURT);
		Registry.register(Registry.SOUND_EVENT, ENTITY_TORETOISE_DEATH.getLocation(), ENTITY_TORETOISE_DEATH);
		Registry.register(Registry.SOUND_EVENT, ENTITY_TORETOISE_EAT.getLocation(), ENTITY_TORETOISE_EAT);
		Registry.register(Registry.SOUND_EVENT, ENTITY_TORETOISE_EAT_SATIATED.getLocation(), ENTITY_TORETOISE_EAT_SATIATED);
		Registry.register(Registry.SOUND_EVENT, ENTITY_TORETOISE_ANGRY.getLocation(), ENTITY_TORETOISE_ANGRY);
		Registry.register(Registry.SOUND_EVENT, ENTITY_TORETOISE_HARVEST.getLocation(), ENTITY_TORETOISE_HARVEST);
		Registry.register(Registry.SOUND_EVENT, ENTITY_TORETOISE_REGROW.getLocation(), ENTITY_TORETOISE_REGROW);
	}
}
