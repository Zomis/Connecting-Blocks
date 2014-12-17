package net.zomis.connblocks;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import net.zomis.connblocks.events.ConnectionMergeEvent;
import net.zomis.connblocks.events.ConnectionMovedEvent;
import net.zomis.connblocks.postmove.PostMoveOrder;

import java.util.*;

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
public class ConnectingBlocks implements Comparable<ConnectingBlocks> {

	@JsonManagedReference
	@JsonProperty
	@ZomisNoEdit
	private List<Block> blocks = new LinkedList<Block>();
	
	@JsonBackReference
	@ZomisNoEdit
	private BlockMap	map;
	
	@ZomisNoEdit
	final int id;
	
	private boolean controllable = true;
	private int connectGroups = 0xffffff;
	
//	private MoveStrategy moveStrategy; // TODO: MoveStrategy for Connections -- A connection that can move and break other blocks for example.
	
	private boolean pusher;
	private boolean pushable;
	private boolean	farAway;
	private boolean allowBroken;
	
	public void setFarAway(boolean farAway) {
		this.farAway = farAway;
	}
	public boolean isFarAway() {
		return farAway;
	}
	public boolean isPushable() {
		return pushable;
	}
	public boolean isPusher() {
		return pusher;
	}
	public void setPushable(boolean pushable) {
		this.pushable = pushable;
	}
	public void setPusher(boolean pusher) {
		this.pusher = pusher;
	}
	
	public void setConnectGroups(int connectGroups) {
		this.connectGroups = connectGroups;
	}
	public int getConnectGroups() {
		return connectGroups;
	}
	
	public boolean isControllable() {
		return controllable;
	}
	public void setControllable(boolean controllable) {
		this.controllable = controllable;
	}
	
	ConnectingBlocks() {
		this(null, -1, null);
	}
	ConnectingBlocks(BlockMap map, int id, Collection<BlockTile> pos) {
		this.map = map;
		this.id = id;
		if (pos != null)
		for (BlockTile tile : pos) {
			this.blocks.add(new Block(this, tile.getX(), tile.getY()));
		}
	}

	/**
	 * Find the connections that will be pushed if moving in direction. Not considering whether or not the push is actually possible.
	 * @param dir Direction to move
	 * @return A list of connections that this connection will push.
	 */
	Set<ConnectingBlocks> findPushConnections(Direction4 dir) {
		Set<ConnectingBlocks> list = new HashSet<ConnectingBlocks>();
		for (Block block : this.blocks) {
			ConnectingBlocks conn = block.getPushConnection(dir);
			if (conn != null)
				list.add(conn);
		}
		return list;
	}
	
	boolean forceMove(MoveOrder order) {
		ConnBlocks.log("Moving " + this + " " + order.getDirection());
		if (this.blocks.isEmpty())
			return false;
		
		if (!this.canMove(order).isAllowed())
			return false;
		
		for (Block block : this.blocks) {
			block.move(order);
		}
		
		if (this.farAway) {
			// TODO: Animations for farAway. Don't move them here but let an outside controller take care of it?
			PostMoveOrder post = new PostMoveOrder(this, order.getDirection());
			if (!order.hasPostMoveAction(post))
				order.addPostMoveAction(post);
		}
		return true;
	}
	
	MoveOrder canMove(final MoveOrder order) {
		for (Block block : this.blocks) {
			if (!block.canMove(order).isAllowed()) {
                ConnBlocks.log("Unable to move because of " + block);
				order.deny();
				return order;
			}
		}
		return order;
	}
	public boolean move(Direction4 dir) {
		if (!this.isControllable())
			return false;
		MoveOrder order = new MoveOrder(this, dir);
		boolean moveResult = order.performMove();
		this.getMap().getEventExecutor().executeEvent(new ConnectionMovedEvent(order));
		return moveResult;
	}
	
	@JsonIgnore
	public int getBlocksSize() {
		return blocks.size();
	}
	public BlockMap getMap() {
		return map;
	}
	
	public Block hasBlockAt(int x, int y) {
		for (Block block : this.blocks)
			if (block.getX() == x && block.getY() == y)
				return block;
		return null;
	}

	@JsonIgnore
	public char getChar() {
		return (char) ('a' + this.getId());
	}
	
	@JsonIgnore
	public Set<ConnectingBlocks> getNeighborConnections() {
		Set<ConnectingBlocks> conns = new TreeSet<ConnectingBlocks>();
		for (Block block : this.blocks) {
			conns.addAll(block.getNeighborConnections());
		}
		return conns;
	}

	void mergeWith(ConnectingBlocks other) {
		this.blocks.addAll(other.blocks);
		for (Block block : other.blocks)
			block.connection = this;
		other.blocks.clear();
		this.connectGroups = this.connectGroups | other.connectGroups;
	}
	
	@Override
	public String toString() {
		return this.getChar() + " (" + this.blocks.size() + ")";
	}

	public int getId() {
		return this.id;
	}

	@JsonIgnore
	public Collection<Block> getBlocks() {
		return Collections.unmodifiableCollection(this.blocks);
	}

	@Override
	public int compareTo(ConnectingBlocks o) {
		return this.id - o.id;
	}

	public boolean canConnectTo(ConnectingBlocks other) {
		return (this.connectGroups & other.connectGroups) > 0;
	}
	public void addBlock(BlockTile tile) {
		removeBlock(tile); // in case we add it again // TODO: Codecrap. Use hasBlock(tile) method, perhaps return index in array.
		this.blocks.add(new Block(this, tile.getX(), tile.getY()));
	}
	public void removeBlock(BlockTile tile) {
		for (Block bl : this.blocks)
		if (bl.getX() == tile.getX() && bl.getY() == tile.getY()) {
			this.blocks.remove(bl);
			return;
		}
	}
	public void checkDisconnections() {
		if (this.blocks.isEmpty()) {
			this.getMap().removeConnection(this);
			return;
		}
		if (this.allowBroken)
			return;
		
		Set<Block> connected = getMainConnection(this.blocks.get(0));
		
		Set<Block> disconnected = new HashSet<Block>(this.blocks);
		disconnected.removeAll(connected);
		if (!disconnected.isEmpty()) {
			this.disconnect(disconnected);
		}
	}
	private void disconnect(Set<Block> disconnected) {
		Set<BlockTile> tiles = new HashSet<BlockTile>(disconnected.size());
		for (Block block : disconnected) {
			BlockTile tile = block.getTile();
			tiles.add(tile);
			this.removeBlock(tile);
		}
		ConnectingBlocks conn = getMap().addConnection(tiles);
		conn.copySettingsFrom(this);
		conn.checkDisconnections();
	}
	public void copySettingsFrom(ConnectingBlocks copyFrom) {
		if (copyFrom == null)
			return;
		this.farAway = copyFrom.farAway;
		this.connectGroups = copyFrom.connectGroups;
		this.controllable = copyFrom.controllable;
		this.pushable = copyFrom.pushable;
		this.pusher = copyFrom.pusher;
		this.allowBroken = copyFrom.allowBroken;
	}
	
	private Set<Block> getMainConnection(Block start) {
		Set<Block> connected = new HashSet<Block>();
		connected.add(start);
		boolean changed = true;
		while (changed) {
			// TODO: Improve FloodFill algorithm. If the connection consists of one small but very long chain, this is highly ineffective as it will check those already checked again.
			changed = false;
			for (Block block : new HashSet<Block>(connected)) {
				for (Direction4 dir : Direction4.values()) {
					BlockTile tile = block.getNeighborUsingStrategy(dir);
					Block other = this.hasBlockAt(tile.getX(), tile.getY());
					if (other != null && connected.add(other))
						changed = true;
				}
			}
		}
		return connected;
	}
	public boolean integrityCheck(MoveOrder order) {
//		CustomFacade.getLog().d("Integrity check on " + this + " with order " + order);
		if (this.allowBroken)
			return true;
		if (this.blocks.isEmpty())
			return true;
		
		Set<Block> connected = new HashSet<Block>();
		connected.add(blocks.get(0));
		
		boolean changed = true;
		while (changed) {
			changed = connectCheck(connected, order);
		}
		
		Set<Block> disconnected = new HashSet<Block>(this.blocks);
		disconnected.removeAll(connected);
//		CustomFacade.getLog().i("Connected " + connected + " disconnected: " + disconnected);
		return disconnected.isEmpty();
	}
	private boolean connectCheck(Collection<Block> connected, MoveOrder order) {
		boolean changed = false;
		for (Block block : new HashSet<Block>(connected)) {
			BlockTile dest = block.getEndSpot(order);
			for (Direction4 dir : Direction4.values()) {
//				CustomFacade.getLog().d("Integrity check on " + this + " with order " + order);
				BlockTile tile = dest.getNeighborUsingStrategy(dir);
				if (tile == null)
					continue;
				// Find out if this connection has any blocks that will end up at tile.
				for (Block other : this.blocks) {
					if (connected.contains(other))
						continue;
					if (other.getEndSpot(order) == tile) {
						if (connected.add(other))
							changed = true;
					}
				}
			}
		}
		return changed;
	}
	
    public boolean connectToNeighbors() {
        Set<ConnectingBlocks> conns = this.getNeighborConnections();
        for (ConnectingBlocks secondary : conns) {
            if (!this.canConnectTo(secondary))
                continue;
            if (secondary == this)
                continue;
            ConnBlocks.log("Merge " + this + " with " + secondary);
            merge(this, secondary);
            return true;
        }
        return false;
    }

    private void merge(ConnectingBlocks primary, ConnectingBlocks secondary) {
        if (primary == secondary)
            throw new IllegalArgumentException();
        primary.mergeWith(secondary);
        map.getEventExecutor().executeEvent(new ConnectionMergeEvent(primary, secondary));
        map.removeConnection(secondary); // this will call ConnectionRemovedEvent for secondary
    }

}
