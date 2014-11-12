package net.zomis.connblocks.events;

import net.zomis.connblocks.ConnectingBlocks;

public class ConnectionAddedEvent extends ConnectionEvent {

	public ConnectionAddedEvent(ConnectingBlocks conn) {
		super(conn);
	}

}
