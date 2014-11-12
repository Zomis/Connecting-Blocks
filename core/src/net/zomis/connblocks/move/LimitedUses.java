package net.zomis.connblocks.move;

import net.zomis.connblocks.Block;
import net.zomis.connblocks.BlockTile;
import net.zomis.connblocks.MoveOrder;
import net.zomis.connblocks.MoveStrategy;

public class LimitedUses implements MoveStrategy {

	LimitedUses() {}
	
	private int limit;
	
	private MoveStrategy strategy;
	
	public LimitedUses(MoveStrategy strategy, int limit) {
		this.limit = limit;
		this.strategy = strategy;
	}
	
	
	@Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
		this.limit--;
		if (this.limit <= 0) {
			if (tile.getMoveStrategyFrom() == this)
				tile.setMoveStrategyFrom(null);
			if (tile.getMoveStrategyTo() == this)
				tile.setMoveStrategyTo(null);
		}
		
		return this.strategy.canMove(tile, block, order);
	}

	
	
}
