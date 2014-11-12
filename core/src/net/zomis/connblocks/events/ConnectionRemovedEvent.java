package net.zomis.connblocks.events;

import net.zomis.connblocks.ConnectingBlocks;

public class ConnectionRemovedEvent extends ConnectionEvent {

	public ConnectionRemovedEvent(ConnectingBlocks conn) {
		super(conn);
	}

}
