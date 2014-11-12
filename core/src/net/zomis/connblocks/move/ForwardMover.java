package net.zomis.connblocks.move;

import net.zomis.connblocks.Block;
import net.zomis.connblocks.BlockTile;
import net.zomis.connblocks.MoveOrder;
import net.zomis.connblocks.MoveStrategy;
import net.zomis.connblocks.postmove.PostMoveOrder;

public class ForwardMover implements MoveStrategy {

	@Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
		PostMoveOrder post = new PostMoveOrder(block.getConnection(), order.getDirection());
		if (!order.hasPostMoveAction(post))
			order.addPostMoveAction(post);
		return true;
	}

}
