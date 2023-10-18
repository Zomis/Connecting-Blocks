package net.zomis.connblocks.move;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.zomis.connblocks.*;


public class ExecuteArea implements MoveStrategy {

	private BlockArea	area;
	private AreaStrategy strategy;

	@JsonCreator
	public ExecuteArea(@JsonProperty("area") BlockArea area, @JsonProperty("strategy") AreaStrategy strategy) {
		this.area = area;
		this.strategy = strategy;
	}
	public ExecuteArea(BlockArea area) {
		this(area, null);
	}
	
	@Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
		order.addPostMoveAction(new PostExecute(tile.getMap(), area, strategy));
		return true;
	}
	
	private static class PostExecute implements PostMoveAction {

		private final BlockArea	area;
		private final AreaStrategy	strategy;
		private final BlockMap	map;

		public PostExecute(BlockMap map, BlockArea area, AreaStrategy strategy) {
			this.map = map;
			this.area = area;
			this.strategy = strategy;
		}

		@Override
		public void postMove(MoveOrder order) {
			if (order.isAllowed()) {
				if (area == null) {
					map.executeAllAreas();
				}
				else area.execute(strategy == null ? area.getStrategy() : strategy);
			}
		}
		
	}

}
