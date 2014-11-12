package net.zomis.connblocks.levels;

public class CombinedLevelSets extends BlockSimpleLevelSet {

	public CombinedLevelSets(BlockLevelSet... sets) {
		for (BlockLevelSet set : sets) {
			for (int i = 0; i < set.getLevelCount(); i++) {
				this.addLevel(set.getLevel(i));
			}
		}
	}
	
}
