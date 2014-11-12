package net.zomis.connblocks.areas;

import net.zomis.connblocks.AreaStrategy;
import net.zomis.connblocks.BlockArea;
import net.zomis.connblocks.BlockTile;
import net.zomis.connblocks.BlockType;

public class AreaKeyForLock implements AreaStrategy {

	private final BlockArea lock;
	
	AreaKeyForLock() {
		this(null);
	}
	public AreaKeyForLock(BlockArea lock) {
		this.lock = lock;
	}
	
	@Override
	public void execute(BlockArea area) {
		BlockType type = area.isFilled() ? null : BlockType.IMPASSABLE;
		setTo(lock, type);
	}
	
	private static void setTo(BlockArea area, BlockType type) {
		for (BlockTile bt : area.getAreas()) {
			bt.setType(type);
		}
	}
	
}
