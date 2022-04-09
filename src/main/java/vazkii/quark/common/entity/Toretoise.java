package vazkii.quark.common.entity;

import vazkii.quark.common.registry.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class Toretoise extends Animal {
	public static final EntityDataAccessor<Integer> ORE_TYPE = SynchedEntityData.defineId(Toretoise.class, EntityDataSerializers.INT);
	public static int ORE_TYPES = 5;

	public int rideTime = 0;
	private boolean isTamed = false;
	private int eatCooldown = 0;
	public int angryTicks = 0;

	private LivingEntity lastAggressor;

	public Toretoise(EntityType<? extends Animal> entityType, Level level) {
		super(entityType, level);
		maxUpStep = 1;
		setPathfindingMalus(BlockPathTypes.WATER, 1);
	}

	public static AttributeSupplier.Builder createToretoiseAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 60)
				.add(Attributes.MOVEMENT_SPEED, 0.08)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1);
	}

	public static boolean canSpawn(EntityType<? extends Toretoise> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, Random random) {
		if (level.getDifficulty() != Difficulty.PEACEFUL && pos.getY() <= 0) {
			if (level.getBrightness(LightLayer.SKY, pos) > random.nextInt(32)) {
				return false;
			} else if ((level.getLevelData().isThundering() ? level.getMaxLocalRawBrightness(pos, 10) : level.getMaxLocalRawBrightness(pos)) == 0) {
				if (spawnType == MobSpawnType.SPAWNER) {
					return true;
				}
				BlockState state = level.getBlockState(pos.below());
				return state.getMaterial() == Material.STONE || state.isValidSpawn(level, pos.below(), type);
			}
		}
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		setBoundingBox(getBoundingBox().setMaxY(getBoundingBox().minY + (getOreType() == 0 ? 1 : 1.4)));
		if (getVehicle() != null) {
			rideTime++;
		} else {
			rideTime = 0;
		}
		if (eatCooldown > 0) {
			eatCooldown--;
		}
		if (angryTicks > 0 && isAlive()) {
			angryTicks--;
			if (isOnGround()) {
				int dangerRange = 3;
				double x = getX() + getBbWidth() / 2;
				double y = getY();
				double z = getZ() + getBbWidth() / 2;
				if (level instanceof ServerLevel serverLevel) {
					if (angryTicks == 3) {
						playSound(ModSoundEvents.ENTITY_TORETOISE_ANGRY, 1, 0.2F);
					} else if (angryTicks == 0) {
						serverLevel.sendParticles(ParticleTypes.CLOUD, x, y, z, 200, dangerRange, 0.5, dangerRange, 0);
					}
				}
				if (angryTicks == 0) {
					AABB hurtAabb = new AABB(x - dangerRange, y - 1, z - dangerRange, x + dangerRange, y + 1, z + dangerRange);
					List<LivingEntity> hurtMeDaddy = level.getEntitiesOfClass(LivingEntity.class, hurtAabb, e -> !(e instanceof Toretoise));
					LivingEntity aggressor = lastAggressor == null ? this : lastAggressor;
					DamageSource damageSource = DamageSource.mobAttack(aggressor);
					for (LivingEntity entity : hurtMeDaddy) {
						DamageSource useSource = damageSource;
						if (entity == aggressor) {
							useSource = DamageSource.mobAttack(this);
						}
						entity.hurt(useSource, 4 + level.getDifficulty().ordinal());
					}
				}
			}
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof LivingEntity living) {
			ItemStack stack = living.getMainHandItem();
			int ore = getOreType();
			if (ore != 0 && stack.getItem() instanceof PickaxeItem) {
				if (level instanceof ServerLevel serverLevel) {
					LootContext.Builder lootBuilder = new LootContext.Builder(serverLevel).withParameter(LootContextParams.TOOL, stack);
					if (living instanceof Player player) {
						lootBuilder.withLuck(player.getLuck());
					}
					dropOre(ore, lootBuilder);
					stack.hurtAndBreak(1, living, stackUser -> stackUser.broadcastBreakEvent(InteractionHand.MAIN_HAND));
				}
				return false;
			}
			if (angryTicks == 0) {
				angryTicks = 20;
				lastAggressor = living;
			}
		}
		return super.hurt(source, amount);
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageable) {
		return null;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return ModSoundEvents.ENTITY_TORETOISE_AMBIENT;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return ModSoundEvents.ENTITY_TORETOISE_HURT;
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.ENTITY_TORETOISE_DEATH;
	}

	@Override
	public SoundEvent getEatingSound(ItemStack stack) {
		return null;
	}

	@Override
	public float getVoicePitch() {
		return (random.nextFloat() - random.nextFloat()) * 0.2F + 0.6F;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.getItem() == Items.GLOW_BERRIES;
	}

	@Override
	public boolean canBreed() {
		return getOreType() == 0 && eatCooldown == 0;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return !isTamed;
	}

	@Override
	protected int decreaseAirSupply(int air) {
		return air;
	}

	@Override
	public void setInLove(@Nullable Player player) {
		setInLoveTime(0);
	}

	@Override
	public void setInLoveTime(int ticks) {
		if (level.isClientSide) {
			return;
		}
		playSound(eatCooldown == 0 ? ModSoundEvents.ENTITY_TORETOISE_EAT : ModSoundEvents.ENTITY_TORETOISE_EAT_SATIATED, 0.5F + 0.5F * level.random.nextInt(2), (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1);
		heal(8);
		if (!isTamed) {
			isTamed = true;
			if (level instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(ParticleTypes.HEART, getX(), getY(), getZ(), 20, 0.5, 0.5, 0.5, 0);
			}
		} else if (eatCooldown == 0) {
			popOre(false);
		}
	}

	@Override
	protected void jumpFromGround() {
	}

	@Override
	public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
		return false;
	}

	@Override
	public boolean canBeLeashed(Player player) {
		return false;
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		popOre(true);
		return data;
	}

	@Override
	public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
		return level.getBlockState(blockPosition().below()).getMaterial() == Material.STONE;
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new BreedGoal(this, 1));
		goalSelector.addGoal(1, new TemptGoal(this, 1.25, Ingredient.of(Items.GLOW_BERRIES), false));
		goalSelector.addGoal(2, new FollowParentGoal(this, 1.25));
		goalSelector.addGoal(3, new RandomStrollGoal(this, 1));
		goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6));
		goalSelector.addGoal(5, new RandomLookAroundGoal(this));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		entityData.set(ORE_TYPE, tag.getInt("OreType"));
		isTamed = tag.getBoolean("Tamed");
		eatCooldown = tag.getInt("EatCooldown");
		angryTicks = tag.getInt("AngryTicks");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("OreType", entityData.get(ORE_TYPE));
		tag.putBoolean("Tamed", isTamed);
		tag.putInt("EatCooldown", eatCooldown);
		tag.putInt("AngryTicks", angryTicks);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ORE_TYPE, 0);
	}

	public int getOreType() {
		return entityData.get(ORE_TYPE);
	}

	private void dropOre(int ore, LootContext.Builder lootContext) {
		lootContext.withParameter(LootContextParams.ORIGIN, position());
		BlockState dropState = null;
		switch (ore) {
			case 1 -> dropState = Blocks.COAL_ORE.defaultBlockState();
			case 2 -> dropState = Blocks.IRON_ORE.defaultBlockState();
			case 3 -> dropState = Blocks.REDSTONE_ORE.defaultBlockState();
			case 4 -> dropState = Blocks.LAPIS_ORE.defaultBlockState();
			case 5 -> dropState = Blocks.COPPER_ORE.defaultBlockState();
		}
		if (dropState != null) {
			playSound(ModSoundEvents.ENTITY_TORETOISE_HARVEST, 1F, 0.6F);
			List<ItemStack> drops = dropState.getDrops(lootContext);
			for (ItemStack drop : drops) {
				spawnAtLocation(drop, 1.2F);
			}
		}
		entityData.set(ORE_TYPE, 0);
	}

	private void popOre(boolean natural) {
		if (getOreType() == 0 && (natural || level.random.nextInt(3) == 0)) {
			int ore = random.nextInt(ORE_TYPES) + 1;
			entityData.set(ORE_TYPE, ore);
			if (!natural) {
				eatCooldown = 1200;
				if (level instanceof ServerLevel serverLevel) {
					serverLevel.sendParticles(ParticleTypes.CLOUD, getX(), getY() + 0.5, getZ(), 100, 0.6, 0.6, 0.6, 0);
					playSound(ModSoundEvents.ENTITY_TORETOISE_REGROW, 10, 0.7F);
				}
			}
		}
	}
}
