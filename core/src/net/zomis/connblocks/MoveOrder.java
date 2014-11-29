package net.zomis.connblocks;

import net.zomis.custommap.CustomFacade;

import java.util.*;

public class MoveOrder {

	private final Direction4	dir;
	private final ConnectingBlocks	primaryConnection;
	private boolean	allowed = true;

	private final Map<Block, IntPoint> deltas = new HashMap<Block, IntPoint>();
	
	private final Set<ConnectingBlocks> movingConnections = new HashSet<ConnectingBlocks>();
	
	private final List<PostMoveAction> postMoveActions = new LinkedList<PostMoveAction>();
	
	public MoveOrder(ConnectingBlocks connection, Direction4 direction) {
		this.movingConnections.add(connection);
		this.primaryConnection = connection;
		this.dir = direction;
	}
	
	public void addPostMoveAction(PostMoveAction action) {
		this.postMoveActions.add(action);
	}
	
	public ConnectingBlocks getConnection() {
		return primaryConnection;
	}
	public Direction4 getDirection() {
		return dir;
	}

	public MoveOrder deny() {
		this.allowed = false;
		return this;
	}
	
	@Deprecated
	public MoveOrder setAllowed(boolean b) {
		if (b)
			throw new IllegalArgumentException();
		this.allowed = b;
		return this;
	}
	public boolean isAllowed() {
		return allowed;
	}

	public boolean performMove() {
		// Add push actions
		this.findPushingConnections();
		
		// Check if all connections are allowed to move (including strategy checks on BlockTiles)
		for (ConnectingBlocks conn : this.movingConnections) {
			if (!conn.canMove(this).isAllowed())
				return false;
		}
		
		// Check for post-move actions
		
		
		// Check if the new destinations are acceptable
		for (ConnectingBlocks conn : this.movingConnections) {
			if (!conn.integrityCheck(this)) {
				CustomFacade.getLog().i("Integrity check failed on " + conn);
				deny();
				return false;
			}
		}
		
		// Perform the movement of the connections
		for (ConnectingBlocks conn : this.movingConnections) {
			conn.forceMove(this);
		}
		
		// Perform post-move actions
		for (PostMoveAction post : this.postMoveActions)
			post.postMove(this);
		
		return true;
	}
	
	// setPerform(boolean) for whether or not to actually perform move order - it can be just a check to see if the order is allowed / which actions it will cause.
	
	private void findPushingConnections() {
		boolean changed = true;
		while (changed) {
			changed = false;
			for (ConnectingBlocks conn : new ArrayList<ConnectingBlocks>(this.movingConnections)) {
				if (conn.isPusher()) {
					Set<ConnectingBlocks> pushed = conn.findPushConnections(getDirection());
					for (ConnectingBlocks push : pushed) {
						if (push.isPushable() && movingConnections.add(push))
							changed = true;
					}
				}
			}
		}
		
	}

	public boolean hasMovingConnection(ConnectingBlocks pushed) {
		return this.movingConnections.contains(pushed);
	}

	public boolean hasPostMoveAction(PostMoveAction post) {
		return this.postMoveActions.contains(post);
	}

	public void setBlockDelta(Block block, int x, int y) {
		this.setBlockDelta(block, new IntPoint(x, y));
	}
	public void setBlockDelta(Block block, IntPoint ip) {
		this.deltas.put(block, ip);
	}
	
	public int getDeltaX(Block block) {
		IntPoint ip = this.deltas.get(block);
		if (ip != null)
			return ip.getX();
		return dir.getDeltaX();
	}

	public int getDeltaY(Block block) {
		IntPoint ip = this.deltas.get(block);
		if (ip != null)
			return ip.getY();
		return dir.getDeltaY();
	}

	public void setBlockTarget(Block block, IntPoint target) {
//		int oldX = getDeltaX(block);
//		int oldY = getDeltaY(block);
//		old = 3 --> target = 7
		int x = target.getX() - block.getX();
		int y = target.getY() - block.getY();
		setBlockDelta(block, x, y);
	}
}

