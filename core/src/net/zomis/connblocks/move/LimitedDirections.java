package net.zomis.connblocks.move;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.zomis.connblocks.Direction4;
import net.zomis.connblocks.Block;
import net.zomis.connblocks.BlockTile;
import net.zomis.connblocks.MoveOrder;
import net.zomis.connblocks.MoveStrategy;

public class LimitedDirections implements MoveStrategy {

	@JsonProperty
	private Direction4[]	forbidden;

	LimitedDirections() {}
	
	public Direction4[] getForbidden() {
		return forbidden;
	}
	
	public LimitedDirections(Direction4... forbidden) {
		this.forbidden = forbidden;
	}

	@Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
		for (Direction4 dir : forbidden)
			if (order.getDirection().equals(dir))
				return false;
		return true;
	}
	
}
