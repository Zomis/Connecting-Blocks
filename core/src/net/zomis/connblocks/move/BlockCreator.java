package net.zomis.connblocks.move;

import net.zomis.connblocks.*;

public class BlockCreator implements MoveStrategy {

	@Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
		ConnectingBlocks conn = block.getConnection();
		conn.addBlock(tile);
		conn.checkDisconnections();
		return false;
	}

}
