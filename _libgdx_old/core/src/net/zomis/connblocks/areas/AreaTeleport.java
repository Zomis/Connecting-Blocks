package net.zomis.connblocks.areas;

import net.zomis.connblocks.IntPoint;
import net.zomis.connblocks.*;

public class AreaTeleport implements AreaStrategy {

	private BlockArea source;
	private BlockArea destination;
	
	public AreaTeleport(BlockArea source, BlockArea destination) {
		this.source = source;
		this.destination = destination;
	}
	
	
	@Override
	public void execute(BlockArea area) {
		if (source.occupiedSize() == 0)
			return;
		if (destination.occupiedSize() > 0)
			return;
		
		IntPoint tlSource = findTopLeft(source);
		IntPoint tlDest = findTopLeft(destination);
		
		for (BlockTile bt : source.getAreas()) {
			Block block = bt.getBlock();
			if (block != null) {
				BlockMap map = block.getMap();
				BlockTile dest = map.pos(tlDest.getX() + (block.getX() - tlSource.getX()), 
						 tlDest.getY() + (block.getY() - tlSource.getY()));
				
				if (destination.contains(dest)) {
					block.teleport(dest.getX(), dest.getY());
				}
			}
		}
		area.getMap().disconnect();
	}

	private IntPoint findTopLeft(BlockArea area) {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		for (BlockTile dest : area.getAreas()) {
			if (dest.getX() < minX)
				minX = dest.getX();
			if (dest.getY() < minY)
				minY = dest.getY();
		}
		return new IntPoint(minX, minY);
	}
	
}
