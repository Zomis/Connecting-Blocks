package net.zomis.connblocks.levels;

import java.util.LinkedList;
import java.util.List;

public class BlockSimpleLevelSet implements BlockLevelSet {

	private List<String> str = new LinkedList<String>();
	
	public BlockSimpleLevelSet() {
	}
	@Override
	public String getLevel(int i) {
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
