package net.zomis.connblocks;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import net.zomis.custommap.CustomFacade;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Block {
	private int x;
	private int y;
	@JsonBackReference
	ConnectingBlocks connection;
	
	Block() {
	}
	public Block(ConnectingBlocks connection, int x, int y) {
		this.connection = connection;
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	
	@Deprecated
	public BlockTile getTile(Direction4 dir) {
		if (dir == null)
			return this.getTile();
		return getConnection().getMap().pos(x + dir.getDeltaX(), y + dir.getDeltaY());
	}
	public ConnectingBlocks getConnection() {
		return connection;
	}
	
	@JsonIgnore
	public Set<ConnectingBlocks> getNeighborConnections() {
		Set<ConnectingBlocks> conns = new HashSet<ConnectingBlocks>();
		for (Direction4 dir : Direction4.values()) {
			BlockTile tile = this.getNeighborUsingStrategy(dir);
			
			if (this.getTile().getType() == BlockType.FREEZED)
				continue;
			if (tile == null) {
				CustomFacade.getLog().e("Block tile is null when scanning neighbor connections: " + this + " dir " + dir);
				continue;
			}
			if (tile.getType() == BlockType.FREEZED)
				continue;
			
			ConnectingBlocks conn = tile.getConnection();
			if (conn != this.getConnection() && conn != null) {
				conns.add(conn);
			}
		}
		return conns;
	}
	@JsonSetter
	private void setTile(BlockTile tile) {}
	@JsonIgnore
	public BlockTile getTile() {
		return this.getConnection().getMap().pos(x, y);
	}
	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "(Block at %d, %d in connection %s)", this.x, this.y, this.getConnection());
	}
	MoveOrder canMove(final MoveOrder order) {
		BlockTile curr = this.getTile();
		if (curr.getMoveStrategyFrom() != null && !curr.getMoveStrategyFrom().canMove(curr, this, order))
			return order.deny();

		BlockTile tile = this.getTile(order.getDirection()); // TODO: What kind of strategy to use here? Needs to find out this block's destination first. Get that from the move order?
			// what if the destination tile in this step actually modifies the block's destination?
		if (tile.getMoveStrategyTo() != null && !tile.getMoveStrategyTo().canMove(tile, this, order))
			return order.deny();
		
		if (tile.getType() == BlockType.IMPASSABLE)
			return order.deny();
		
		ConnectingBlocks pushed = this.getPushConnection(order.getDirection());
		if (pushed != null && !order.hasMovingConnection(pushed)) {
			return order.deny();
		}
		
		return order;
	}
	public void move(MoveOrder order) {
		this.x += order.getDeltaX(this);
		this.y += order.getDeltaY(this);
	}
	public void teleport(int destX, int destY) {
		this.x = destX;
		this.y = destY;
	}

	ConnectingBlocks getPushConnection(Direction4 dir) {
//		BlockTile tile = this.getTile(dir);
		BlockTile tile = this.getNeighborUsingStrategy(dir);
		ConnectingBlocks conn = tile.getConnection();
		if (conn != null && conn != this.getConnection()) {
			return conn;
		}
		return null;
	}
	
	public BlockTile getNeighborUsingStrategy(Direction4 dir) {
		return this.getTile().getNeighborUsingStrategy(dir);
	}
	public BlockTile getEndSpot(MoveOrder order) {
		int x = order.getDeltaX(this);
		int y = order.getDeltaY(this);
		return getConnection().getMap().pos(this.getX() + x, this.getY() + y);
	}
	@JsonIgnore
	public BlockMap getMap() {
		return getConnection().getMap();
	}
	
}
