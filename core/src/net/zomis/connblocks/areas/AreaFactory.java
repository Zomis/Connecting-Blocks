package net.zomis.connblocks.areas;

import net.zomis.connblocks.AreaStrategy;
import net.zomis.connblocks.BlockArea;
import net.zomis.connblocks.BlockTile;
import net.zomis.connblocks.ConnectingBlocks;

import java.util.Iterator;
import java.util.List;

public class AreaFactory implements AreaStrategy {

	private final boolean needsEmpty;
	private final ConnectingBlocks settingsCopy;
	
	AreaFactory() {this(null, false); }
	public AreaFactory(ConnectingBlocks settingsCopy, boolean needsEmpty) {
		this.needsEmpty = needsEmpty;
		this.settingsCopy = settingsCopy;
	}
	
	@Override
	public void execute(BlockArea area) {
		if (needsEmpty) {
			if (area.occupiedSize() >= 0)
				return;
		}
		
		List<BlockTile> areas = area.getAreas();
		Iterator<BlockTile> it = areas.iterator();
		// Remove the already occupied tiles
		while (it.hasNext()) {
			if (it.next().getBlock() != null)
				it.remove();
		}
		if (areas.isEmpty())
			return;
		// Create the new connection and configure it with the correct settings
		ConnectingBlocks copy = area.getMap().addConnection(areas.toArray(new BlockTile[areas.size()]));
		copy.copySettingsFrom(settingsCopy);
		copy.checkDisconnections();
	}
}
