package net.zomis.connblocks.move;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.zomis.connblocks.Block;
import net.zomis.connblocks.BlockTile;
import net.zomis.connblocks.MoveOrder;
import net.zomis.connblocks.MoveStrategy;

public class CombinedMoveStrategy implements MoveStrategy {

	@JsonProperty
	private final MoveStrategy	a;
	@JsonProperty
	private final MoveStrategy	b;

	public CombinedMoveStrategy(MoveStrategy a, MoveStrategy b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
		return a.canMove(tile, block, order) && b.canMove(tile, block, order);
	}

    public MoveStrategy getA() {
        return a;
    }

    public MoveStrategy getB() {
        return b;
    }
}
