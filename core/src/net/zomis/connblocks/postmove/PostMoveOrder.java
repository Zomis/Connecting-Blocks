package net.zomis.connblocks.postmove;

import net.zomis.Direction4;
import net.zomis.connblocks.ConnectingBlocks;
import net.zomis.connblocks.MoveOrder;
import net.zomis.connblocks.PostMoveAction;

public class PostMoveOrder implements PostMoveAction {

	private ConnectingBlocks	conn;

	private Direction4	dir;

	public PostMoveOrder(ConnectingBlocks conn, Direction4 direction) {
		this.conn = conn;
		this.dir = direction;
	}
	
	@Override
	public void postMove(MoveOrder order) {
		new MoveOrder(conn, dir).performMove();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conn == null) ? 0 : conn.hashCode());
		result = prime * result + ((dir == null) ? 0 : dir.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PostMoveOrder other = (PostMoveOrder) obj;
		if (conn == null) {
			if (other.conn != null)
				return false;
		}
		else if (!conn.equals(other.conn))
			return false;
		if (dir != other.dir)
			return false;
		return true;
	}
}
