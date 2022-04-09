package vazkii.quark.common.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PickarangItem extends Item {
	private final boolean netherite;

	public PickarangItem(Properties properties, boolean netherite) {
		super(properties);
		this.netherite = netherite;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(2, attacker, stackUser -> stackUser.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		return true;
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState state) {
		if (netherite) {
			return Items.NETHERITE_PICKAXE.isCorrectToolForDrops(state) || Items.NETHERITE_AXE.isCorrectToolForDrops(state) || Items.NETHERITE_SHOVEL.isCorrectToolForDrops(state);
		} else {
			return Items.DIAMOND_PICKAXE.isCorrectToolForDrops(state) || Items.DIAMOND_AXE.isCorrectToolForDrops(state) || Items.DIAMOND_SHOVEL.isCorrectToolForDrops(state);
		}
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity user) {
		if (state.getDestroySpeed(level, pos) != 0) {
			stack.hurtAndBreak(1, user, stackUser -> stackUser.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		}
		return true;
	}

//	@Override
//	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//		ItemStack itemstack = player.getItemInHand(hand);
//		player.setItemInHand(hand, ItemStack.EMPTY);
//		int eff = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, itemstack);
//		Vec3 pos = player.position();
//		level.playSound(null, pos.x, pos.y, pos.z, QuarkSounds.ENTITY_PICKARANG_THROW, SoundSource.NEUTRAL, 0.5F + eff * 0.14F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
//
//		if(!level.isClientSide) {
//			Inventory inventory = player.getInventory();
//			int slot = hand == InteractionHand.OFF_HAND ? inventory.getContainerSize() - 1 : inventory.selected;
//			Pickarang entity = new Pickarang(level, player);
//			entity.setThrowData(slot, itemstack, isNetherite);
//			entity.shoot(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F + eff * 0.325F, 0F);
//			level.addFreshEntity(entity);
//		}
//
//		if(!player.getAbilities().instabuild && (isNetherite ? PickarangModule.netheriteNoCooldown : PickarangModule.noCooldown)) {
//			int cooldown = 10 - eff;
//			if (cooldown > 0)
//				player.getCooldowns().addCooldown(this, cooldown);
//		}
//
//		player.awardStat(Stats.ITEM_USED.get(this));
//		return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
//	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create(super.getDefaultAttributeModifiers(slot));
		if (slot == EquipmentSlot.MAINHAND) {
			multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", netherite ? 3 : 2, AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.8, AttributeModifier.Operation.ADDITION));
		}
		return super.getDefaultAttributeModifiers(slot);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return 0;
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == (netherite ? Items.NETHERITE_INGOT : Items.DIAMOND);
	}

	@Override
	public int getEnchantmentValue() {
		return netherite ? Items.NETHERITE_PICKAXE.getEnchantmentValue() : Items.DIAMOND_PICKAXE.getEnchantmentValue();
	}
}
