package net.zomis.connblocks.events;

import net.zomis.Direction4;
import net.zomis.connblocks.MoveOrder;

public class ConnectionMovedEvent extends ConnectionEvent {

	private final MoveOrder	order;

	public ConnectionMovedEvent(MoveOrder order) {
		super(order.getConnection());
		this.order = order;
	}
	
	public MoveOrder getOrder() {
		return order;
	}
	public Direction4 getDirection() {
		return order.getDirection();
	}

}
