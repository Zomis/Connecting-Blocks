package net.zomis.connblocks;

import com.fasterxml.jackson.annotation.*;
import net.zomis.custommap.model.GenericTileModel;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.NONE, getterVisibility= JsonAutoDetect.Visibility.NONE)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class BlockTile extends GenericTileModel<BlockTile, BlockMap> {

	@JsonProperty
	private MoveStrategy moveStrategyFrom;
	
	@JsonProperty
	private MoveStrategy moveStrategyTo;
	
	@Deprecated
	@JsonProperty
	private void setLink(int link){}; // Not used anymore, whatever it was.
	
	@JsonProperty
	private NeighborStrategy neighborStrategy;
	
	public void setMoveStrategyFrom(MoveStrategy moveStrategyFrom) {
		this.moveStrategyFrom = moveStrategyFrom;
	}
	public void setMoveStrategyTo(MoveStrategy moveStrategyTo) {
		this.moveStrategyTo = moveStrategyTo;
	}
	public MoveStrategy getMoveStrategyFrom() {
		return moveStrategyFrom;
	}
	public MoveStrategy getMoveStrategyTo() {
		return moveStrategyTo;
	}
	public void setNeighborStrategy(NeighborStrategy neighborStrategy) {
		this.neighborStrategy = neighborStrategy;
	}
	public NeighborStrategy getNeighborStrategy() {
		return neighborStrategy;
	}
	
	@JsonProperty
	private BlockType type;
	
	@JsonGetter
	public int getX() { return super.getX(); };
	@JsonGetter
	public int getY() { return super.getY(); };
	
	BlockTile() {
		super(null, 0, 0);
	}
	
	public BlockTile(BlockMap map, int x, int y) {
		super(map, x, y);
	}
	
	public void setType(BlockType type) {
		this.type = type;
	}
	
	public BlockType getType() {
		return type;
	}

	@JsonIgnore
	public ConnectingBlocks getConnection() {
		Block block = getBlock();
		return block != null ? block.getConnection() : null;
	}
	@JsonIgnore
	public Block getBlock() {
		if (this.map == null)
			return null;
		for (ConnectingBlocks conn : this.map.getConnections()) {
			Block block = conn.hasBlockAt(getX(), getY());
			if (block != null) 
				return block;
		}
		return null;
	}
	void onLoad(BlockMap blockMap) {
		this.map = blockMap;
	}

	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "Tile(%d, %d)@%s", this.getX(), this.getY(), this.getMap());
	}
	public BlockTile getNeighborUsingStrategy(Direction4 dir) {
		if (dir == null)
			throw new IllegalArgumentException();
		
		NeighborStrategy strat = this.getNeighborStrategy();
		BlockTile used;
		if (strat != null) {
			used = strat.getBlockAt(this, dir);
		}
		else {
			if (map == null) throw new NullPointerException("Tile does not have map: " + this);
			used = map.pos(getX() + dir.getDeltaX(), getY() + dir.getDeltaY());
		}
		
		if (used == null)
			return null;
		
//		CustomFacade.getLog().i(this + " getNeighbor with strategy " + strat + " used is " + used);
		if (used != null && used.getNeighborStrategy() != null) {
			BlockTile redir = used.getNeighborStrategy().redirectOnUsedAsNieghbor(used, this, dir);
			ConnBlocks.log("used strat is " + used.getNeighborStrategy() + " redirection is " + redir);
			return redir;
		}
		else return used;
	}
	@Deprecated
	public IntPoint getPos() {
		return new IntPoint(getX(), getY());
	}
	public BlockMap getMap() {
		return this.map;
	}
	
	@Override
	public void javaGarbage() {
		Block block = this.getBlock();
		if (block != null) {
			block.getConnection().removeBlock(this);
		}
		this.map = null;
		this.moveStrategyFrom = null;
		this.moveStrategyTo = null;
		this.neighborStrategy = null;
	}
	
	public Set<BlockArea> getAreas() {
		Set<BlockArea> areas = new HashSet<BlockArea>();
		for (BlockArea area : getMap().getAreas()) {
			if (area.contains(this))
				areas.add(area);
		}
		return areas;
	}
}
