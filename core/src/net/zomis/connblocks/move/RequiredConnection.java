package net.zomis.connblocks.move;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.zomis.connblocks.Block;
import net.zomis.connblocks.BlockTile;
import net.zomis.connblocks.MoveOrder;
import net.zomis.connblocks.MoveStrategy;

public class RequiredConnection implements MoveStrategy {
	@JsonProperty
	private int min;
	@JsonProperty
	private int max;
	
	public int getMax() {
		return max;
	}
	public int getMin() {
		return min;
	}
	
	RequiredConnection() {
	}
	public RequiredConnection(int min, int max) {
		if (max < min)
			throw new IllegalArgumentException();
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
		int size = block.getConnection().getBlocksSize();
		return min <= size && size <= max;
	}
	
}
