package net.zomis.connblocks;

public interface NeighborStrategy {
	BlockTile getBlockAt(BlockTile tile, Direction4 dir);
	BlockTile redirectOnUsedAsNieghbor(BlockTile target, BlockTile usedBy, Direction4 directionLooking);
}
