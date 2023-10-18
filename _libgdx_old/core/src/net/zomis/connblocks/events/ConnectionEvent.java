package net.zomis.connblocks.events;

import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.ConnectingBlocks;
import net.zomis.events.IEvent;

public abstract class ConnectionEvent implements IEvent {

	private final ConnectingBlocks	connection;
	public ConnectionEvent(ConnectingBlocks conn) {
		this.connection = conn;
	}
	public ConnectingBlocks getConnection() {
		return connection;
	}
	public BlockMap getMap() {
		return connection.getMap();
	}
}
