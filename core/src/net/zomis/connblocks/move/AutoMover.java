package net.zomis.connblocks.move;

import net.zomis.connblocks.*;
import net.zomis.connblocks.postmove.PostMoveOrder;

public class AutoMover implements MoveStrategy {

    private final Direction4 dir;

    public AutoMover(Direction4 dir) {
        this.dir = dir;
    }

    @Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
        PostMoveOrder postMove = new PostMoveOrder(block.getConnection(), dir);
        if (!order.hasPostMoveAction(postMove)) {
            order.addPostMoveAction(postMove);
        }
		return true;
	}

}
