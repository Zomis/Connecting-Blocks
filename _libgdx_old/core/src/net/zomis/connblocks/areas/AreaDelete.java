package net.zomis.connblocks.areas;

import net.zomis.connblocks.*;

public class AreaDelete implements AreaStrategy {

	@Override
	public void execute(BlockArea area) {
		for (BlockTile bt : area.getAreas()) {
			Block block = bt.getBlock();
			if (block == null) 
				continue;
			
			ConnectingBlocks conn = block.getConnection();
			conn.removeBlock(bt);
		}
	}

}
