package net.zomis.connblocks;


public interface MoveStrategy {

	boolean canMove(BlockTile tile, Block block, MoveOrder order);
}
