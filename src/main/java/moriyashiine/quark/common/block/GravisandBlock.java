package moriyashiine.quark.common.block;

import moriyashiine.quark.common.entity.Gravisand;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class GravisandBlock extends Block {
	public GravisandBlock(Properties settings) {
		super(settings);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return 15;
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean notify) {
		checkRedstone(level, pos);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		checkRedstone(level, pos);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		if (tryFall(level, pos, Direction.DOWN) || tryFall(level, pos, Direction.UP)) {
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
			for (Direction direction : Direction.values()) {
				if (level.getBlockState(mutable.setWithOffset(pos, direction)).getBlock() == this) {
					level.scheduleTick(mutable, this, 2);
				}
			}
		}
	}

	private boolean tryFall(Level level, BlockPos pos, Direction direction) {
		if (level.isInWorldBounds(pos) && FallingBlock.isFree(level.getBlockState(pos.relative(direction)))) {
			Gravisand entity = new Gravisand(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, direction == Direction.UP);
			level.setBlock(pos, level.getFluidState(pos).createLegacyBlock(), Block.UPDATE_ALL);
			level.addFreshEntity(entity);
			return true;
		}
		return false;
	}

	private void checkRedstone(Level level, BlockPos pos) {
		if (level.hasNeighborSignal(pos)) {
			level.scheduleTick(pos, this, 2);
		}
	}
}
