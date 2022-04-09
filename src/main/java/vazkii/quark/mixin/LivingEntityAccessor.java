package vazkii.quark.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
	@Accessor("attackStrengthTicker")
	int quark_getAttackStrengthTicker();

	@Accessor("attackStrengthTicker")
	void quark_setAttackStrengthTicker(int v);
}
