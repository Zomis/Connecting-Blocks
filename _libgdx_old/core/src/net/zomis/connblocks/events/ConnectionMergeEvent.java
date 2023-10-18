package net.zomis.connblocks.events;

import net.zomis.connblocks.ConnectingBlocks;
import net.zomis.events.IEvent;

public class ConnectionMergeEvent implements IEvent {

	private final ConnectingBlocks	primary;
	private final ConnectingBlocks	secondary;

	public ConnectionMergeEvent(ConnectingBlocks primary,
			ConnectingBlocks secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}
	
	public ConnectingBlocks getPrimary() {
		return primary;
	}
	public ConnectingBlocks getSecondary() {
		return secondary;
	}

}
