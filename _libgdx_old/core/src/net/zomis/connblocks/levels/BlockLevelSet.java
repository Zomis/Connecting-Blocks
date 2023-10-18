package net.zomis.connblocks.levels;

import net.zomis.connblocks.BlockMap;

public interface BlockLevelSet {
    BlockMap getLevel(int i);
    String getLevelData(int i);
	String getLevelSetName();
	int getLevelCount();
}
