package vazkii.quark.common.registry;

import vazkii.quark.common.Quark;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class QuarkSounds {
	public static final SoundEvent ENTITY_PICKARANG_THROW = register("entity.pickarang.throw");
	public static final SoundEvent ENTITY_PICKARANG_CLANK = register("entity.pickarang.clank");
	public static final SoundEvent ENTITY_PICKARANG_SPARK = register("entity.pickarang.spark");
	public static final SoundEvent ENTITY_PICKARANG_PICKUP = register("entity.pickarang.pickup");
	public static final SoundEvent ENTITY_TORETOISE_AMBIENT = register("entity.toretoise.ambient");
	public static final SoundEvent ENTITY_TORETOISE_HURT = register("entity.toretoise.hurt");
	public static final SoundEvent ENTITY_TORETOISE_DEATH = register("entity.toretoise.death");
	public static final SoundEvent ENTITY_TORETOISE_EAT = register("entity.toretoise.eat");
	public static final SoundEvent ENTITY_TORETOISE_EAT_SATIATED = register("entity.toretoise.eat_satiated");
	public static final SoundEvent ENTITY_TORETOISE_ANGRY = register("entity.toretoise.angry");
	public static final SoundEvent ENTITY_TORETOISE_HARVEST = register("entity.toretoise.harvest");
	public static final SoundEvent ENTITY_TORETOISE_REGROW = register("entity.toretoise.regrow");

	private static SoundEvent register(String id) {
		SoundEvent event = new SoundEvent(new ResourceLocation(Quark.MOD_ID, id));
		return Registry.register(Registry.SOUND_EVENT, event.getLocation(), event);
	}

	public static void init() {
	}
}
