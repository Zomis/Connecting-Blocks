package net.zomis.connblocks.move;

import net.zomis.connblocks.*;

public class BlockBreaker implements MoveStrategy {

	@Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
		order.addPostMoveAction(new BlockBreakAction(block));
		return true;
	}

	private static class BlockBreakAction implements PostMoveAction {

		private Block	block;

		public BlockBreakAction(Block block) {
			this.block = block;
		}

		@Override
		public void postMove(MoveOrder order) {
			ConnectingBlocks conn = block.getConnection();
			conn.removeBlock(block.getTile());
			conn.checkDisconnections();
		}
		
	}
	
}
