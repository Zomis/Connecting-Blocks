package net.zomis.connblocks;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.zomis.connblocks.areas.AreaNoAction;
import net.zomis.connblocks.move.ExecuteArea;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class BlockArea {
	
	@JsonProperty
	private List<BlockTile> areas;
	@JsonProperty
	private AreaStrategy strategy;
	
	public List<BlockTile> getAreas() {
		return new ArrayList<BlockTile>(areas);
	}
	
	public BlockArea(AreaStrategy strategy) {
		this.areas = new ArrayList<BlockTile>();
		this.strategy = strategy;
	}
	BlockArea() {
		this(new AreaNoAction());
	}
	
	public void setStrategy(AreaStrategy strategy) {
		this.strategy = strategy;
	}
	public void execute() {
		this.execute(strategy);
	}

	public void addTile(BlockTile pos) {
		this.areas.add(pos);
	}
	
	public int size() {
		return this.areas.size();
	}
	public int occupiedSize() {
		int occupied = 0;
		for (BlockTile bt : areas) {
			if (bt.getBlock() != null)
				++occupied;
		}
		return occupied;
	}
	@JsonIgnore
	public boolean isFilled() {
		return this.occupiedSize() == this.size();
	}
	@JsonIgnore
	public BlockMap getMap() {
		return areas.isEmpty() ? null : areas.get(0).getMap();
	}

	public boolean contains(BlockTile blockTile) {
		return areas.contains(blockTile);
	}

	public void setupAutoExecution(boolean onEnter, boolean onExit) {
		ExecuteArea execute = new ExecuteArea(this);
		for (BlockTile bt : this.areas) {
			if (onExit)
				bt.setMoveStrategyFrom(execute);
			if (onEnter)
				bt.setMoveStrategyTo(execute);
		}
	}

	public AreaStrategy getStrategy() {
		return strategy;
	}
	public void execute(AreaStrategy strategy) {
		if (strategy == null)
			return;
		strategy.execute(this);
	}
}
