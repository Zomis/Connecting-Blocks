package net.zomis.connblocks.areas;

import net.zomis.connblocks.*;

public class AreaChangeConnection implements AreaStrategy {

	private Integer connectGroups;
	private Boolean	controllable;
	private Boolean	farAway;
	private Boolean	pushable;
	private Boolean	pusher;
	
	public AreaChangeConnection() {
	}
	
	@Override
	public void execute(BlockArea area) {
		ConnectingBlocks targetConn = area.getMap().addConnection();
		ConnectingBlocks settings = null;
		for (BlockTile bt : area.getAreas()) {
			Block block = bt.getBlock();
			if (block != null) {
				settings = block.getConnection();
				settings.removeBlock(bt);
				targetConn.addBlock(bt);
			}
		}
		
		targetConn.copySettingsFrom(settings);
		if (connectGroups != null)
			targetConn.setConnectGroups(connectGroups);
		if (controllable != null)
			targetConn.setControllable(controllable);
		if (farAway != null)
			targetConn.setFarAway(farAway);
		if (pushable != null)
			targetConn.setPushable(pushable);
		if (pusher != null)
			targetConn.setPusher(pusher);
		
		area.getMap().stateBasedEffects();
	}
	public AreaChangeConnection setConnectGroups(Integer connectGroups) {
		this.connectGroups = connectGroups;
		return this;
	}
	public AreaChangeConnection setControllable(Boolean controllable) {
		this.controllable = controllable;
		return this;
	}
	public AreaChangeConnection setFarAway(Boolean farAway) {
		this.farAway = farAway;
		return this;
	}
	public AreaChangeConnection setPushable(Boolean pushable) {
		this.pushable = pushable;
		return this;
	}
	public AreaChangeConnection setPusher(Boolean pusher) {
		this.pusher = pusher;
		return this;
	}
	

}
