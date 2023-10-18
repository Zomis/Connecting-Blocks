package net.zomis.connblocks.levels;

import net.zomis.connblocks.BlockMap;

import java.util.LinkedList;
import java.util.List;

public class BlockSimpleLevelSet implements BlockLevelSet {

	private List<String> str = new LinkedList<String>();
	
	public BlockSimpleLevelSet() {
	}

    @Override
    public BlockMap getLevel(int i) {
        return null;
    }

    @Override
	public String getLevelData(int i) {
		return str.get(i);
	}
	@Override
	public String getLevelSetName() {
		return this.getClass().getSimpleName();
	}
	protected void addLevel(String level) {
		this.str.add(level);
	}
	@Override
	public int getLevelCount() {
		return this.str.size();
	}
}
