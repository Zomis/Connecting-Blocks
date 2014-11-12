package net.zomis.connblocks.areas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.zomis.IntPoint;
import net.zomis.connblocks.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaClone implements AreaStrategy {

	private final BlockArea source;
	private final BlockArea destination;
	
	@JsonCreator
	public AreaClone(@JsonProperty("source") BlockArea source, @JsonProperty("destination") BlockArea destination) {
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
		List<BlockTile> destAreas = destination.getAreas();
		Map<ConnectingBlocks, ConnectingBlocks> conns = new HashMap<ConnectingBlocks, ConnectingBlocks>();
		for (BlockTile bt : source.getAreas()) {
			Block block = bt.getBlock();
			if (block != null) {
				BlockMap map = block.getMap();
				ConnectingBlocks newConn = conns.get(block.getConnection());
				if (newConn == null) {
					newConn = map.addConnection();
					newConn.copySettingsFrom(block.getConnection());
					conns.put(block.getConnection(), newConn);
				}
				BlockTile dest = map.pos(tlDest.getX() + (block.getX() - tlSource.getX()), 
										 tlDest.getY() + (block.getY() - tlSource.getY()));
				if (destAreas.contains(dest)) {
					newConn.addBlock(dest);
				}
			}
		}
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
