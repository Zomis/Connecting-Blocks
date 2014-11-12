package net.zomis.connblocks.areas;

import net.zomis.connblocks.*;

public class AreaMerge implements AreaStrategy {

	@Override
	public void execute(BlockArea area) {
		ConnectingBlocks targetConn = null;
		for (BlockTile bt : area.getAreas()) {
			Block block = bt.getBlock();
			if (block != null) {
				if (targetConn == null)
					targetConn = block.getConnection();
				else if (block.getConnection() != targetConn) {
					block.getConnection().removeBlock(bt);
					targetConn.addBlock(bt);
				}
			}
		}
		area.getMap().stateBasedEffects();
	}

}
