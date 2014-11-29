package net.zomis.connblocks.move;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.zomis.connblocks.Direction4;
import net.zomis.connblocks.IntPoint;
import net.zomis.connblocks.*;

public class ConnectModifier implements MoveStrategy {

	@JsonIgnore
	@ZomisNoEdit
	private final IntPoint noMove = new IntPoint(0, 0);
	
	@Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
		Direction4 dir = order.getDirection().getOpposite();
		while (block != null) {
			order.setBlockDelta(block, noMove);
			block = block.getNeighborUsingStrategy(dir).getBlock();
		}
		return true;
	}

}
