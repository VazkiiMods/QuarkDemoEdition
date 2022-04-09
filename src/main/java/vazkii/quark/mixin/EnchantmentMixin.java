package vazkii.quark.mixin;

import vazkii.quark.common.item.PickarangItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Shadow
	@Final
	public EnchantmentCategory category;

	@Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
	private void quarklite$pickarangEnchantments(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem() instanceof PickarangItem && category == EnchantmentCategory.DIGGER) {
			cir.setReturnValue(true);
		}
	}
}
