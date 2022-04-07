package moriyashiine.quark.common.entity;

import moriyashiine.quark.common.registry.ModBlocks;
import moriyashiine.quark.common.registry.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Gravisand extends FallingBlockEntity {
	public static final EntityDataAccessor<Boolean> FALL_UPWARDS = SynchedEntityData.defineId(Gravisand.class, EntityDataSerializers.BOOLEAN);

	public Gravisand(EntityType<? extends FallingBlockEntity> entityType, Level level) {
		super(entityType, level);
	}

	public Gravisand(Level level, double x, double y, double z, boolean fallUpwards) {
		this(ModEntityTypes.GRAVISAND, level);
		blockState = ModBlocks.GRAVISAND.defaultBlockState();
		blocksBuilding = true;
		setPos(x, y, z);
		setDeltaMovement(Vec3.ZERO);
		xo = x;
		yo = y;
		zo = z;
		setStartPos(blockPosition());
		entityData.set(FALL_UPWARDS, fallUpwards);
	}

	@Override
	public void tick() {
		super.tick();
		BlockPos pos = blockPosition();
		if (!level.isClientSide && shouldFallUpwards() && !isRemoved() && !level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()) {
			Block block = blockState.getBlock();
			BlockState state = level.getBlockState(pos);
			setDeltaMovement(getDeltaMovement().multiply(0.7, 0.5, 0.7));
			boolean canReplace = state.canBeReplaced(new DirectionalPlaceContext(level, pos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
			boolean canPlace = blockState.canSurvive(level, pos) && !FallingBlock.isFree(level.getBlockState(pos.above()));
			if (canReplace && canPlace) {
				if (level.setBlock(pos, blockState, Block.UPDATE_ALL)) {
					((ServerLevel) level).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket(pos, level.getBlockState(pos)));
					discard();
				} else if (dropItem && level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
					discard();
					callOnBrokenAfterFall(block, pos);
					spawnAtLocation(block);
				}
			} else {
				discard();
				if (dropItem && level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
					callOnBrokenAfterFall(block, pos);
					spawnAtLocation(block);
				}
			}
		}
	}

	@Override
	public void move(MoverType type, Vec3 vec3) {
		if (type == MoverType.SELF && shouldFallUpwards()) {
			vec3 = vec3.multiply(1, -1, 1);
		}
		super.move(type, vec3);
	}

	@Override
	public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		entityData.set(FALL_UPWARDS, tag.getBoolean("FallUpwards"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("FallUpwards", entityData.get(FALL_UPWARDS));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(FALL_UPWARDS, false);
	}

	private boolean shouldFallUpwards() {
		return entityData.get(FALL_UPWARDS);
	}
}
