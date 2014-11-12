package net.zomis.connblocks;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.zomis.ConnBlocks;
import net.zomis.connblocks.events.ConnectionAddedEvent;
import net.zomis.connblocks.events.ConnectionMergeEvent;
import net.zomis.connblocks.events.ConnectionRemovedEvent;
import net.zomis.custommap.model.GenericMapModel;
import net.zomis.events.EventExecutor;

import java.io.IOException;
import java.util.*;

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
public class BlockMap extends GenericMapModel<BlockTile> {

	@JsonManagedReference
	private Set<ConnectingBlocks> connections = new TreeSet<ConnectingBlocks>();
	private int	lastid = 0;
	private Set<BlockArea> areas = new HashSet<BlockArea>();

    @JsonIgnore
    private EventExecutor events = new EventExecutor();

    BlockMap() {}
	
	public BlockMap(int width, int height) {
		this.initMap(width, height);
		this.setEdge();
	}
	
	public BlockArea newArea(AreaStrategy strategy) {
		BlockArea area = new BlockArea(strategy);
		this.areas.add(area);
		return area;
	}
	@Deprecated
	boolean removeArea(BlockArea area) {
		return this.areas.remove(area);
	}
	
	@Override
	public void changeSize(int newWidth, int newHeight) {
		super.changeSize(newWidth, newHeight);
	}

	@Override
	public BlockTile newTile(GenericMapModel<BlockTile> map, int x, int y) {
		return new BlockTile((BlockMap) map, x, y);
	}
	private void setEdge() {
		BlockType type = BlockType.IMPASSABLE;
		for (int edgeCounter = 0; edgeCounter < this.getMapHeight(); edgeCounter++) {
			this.pos(0, edgeCounter).setType(type);
			this.pos(this.getMapWidth() - 1, edgeCounter).setType(type);
		}
		for (int edgeCounter = 0; edgeCounter < this.getMapWidth(); edgeCounter++) {
			this.pos(edgeCounter, 0).setType(type);
			this.pos(edgeCounter, this.getMapHeight() - 1).setType(type);
		}
	}

	ConnectingBlocks addConnection(Collection<BlockTile> pos) {
		ConnectingBlocks conn = new ConnectingBlocks(this, this.lastid++, pos);
		this.connections.add(conn);
		this.getEventExecutor().executeEvent(new ConnectionAddedEvent(conn));
		return conn;
	}
	public ConnectingBlocks addConnection(BlockTile... positions) {
		List<BlockTile> pos = Arrays.asList(positions);
		return addConnection(pos);
	}
	
	public Set<ConnectingBlocks> getConnections() {
		return new TreeSet<ConnectingBlocks>(connections);
	}
	public Set<BlockArea> getAreas() {
		return new HashSet<BlockArea>(areas);
	}

	public void connect() {
		boolean connectionPerformed = true;
		while (connectionPerformed) {
			connectionPerformed = false;
			outer:
			for (ConnectingBlocks conn : new TreeSet<ConnectingBlocks>(this.connections)) {
				Set<ConnectingBlocks> conns = conn.getNeighborConnections();
				for (ConnectingBlocks secondary : conns) {
					if (!conn.canConnectTo(secondary))
						continue;
					if (secondary == conn)
						continue;
                    ConnBlocks.log("Merge " + conn + " with " + secondary);
					merge(conn, secondary);
					connectionPerformed = true;
					break outer;
				}
			}
		}
	}

	private void merge(ConnectingBlocks primary, ConnectingBlocks secondary) {
		if (primary == secondary)
			throw new IllegalArgumentException();
		primary.mergeWith(secondary);
		getEventExecutor().executeEvent(new ConnectionMergeEvent(primary, secondary));
		this.removeConnection(secondary); // this will call ConnectionRemovedEvent for secondary
	}

	public boolean checkForGoal() {
		for (BlockTile bt : this) {
			if (bt.getType() == BlockType.GOAL && bt.getConnection() == null)
				return false;
			if (bt.getType() == BlockType.NOT_GOAL && bt.getConnection() != null)
				return false;
		}
		return true;
	}

	public static ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
		return mapper;
	}

	public void stateBasedEffects() {
		this.connect();
		this.disconnect();
	}
	
	public BlockMap onLoad() {
        ConnBlocks.log("onLoad: " + this);
		
		for (BlockTile bt : this)
			bt.onLoad(this);
		return this;
	}
	@Override
	public String toString() {
		int total = 0;
		for (ConnectingBlocks conn : this.connections) {
			total += conn.getBlocksSize();
		}
		return "{BlockMap " + total + " blocks, " + connections.size() + " connections}";
	}
	public void removeConnection(ConnectingBlocks conn) {
		this.connections.remove(conn);
		this.getEventExecutor().executeEvent(new ConnectionRemovedEvent(conn));
	}

	@JsonIgnore
	public EventExecutor getEventExecutor() {
		return events;
	}
	
	public String saveToString() {
		try {
			return BlockMap.mapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public BlockMap testLoadWrite() {
		String str;
		try {
			str = BlockMap.mapper().writeValueAsString(this);
//		CustomFacade.getLog().i(DatatypeConverter.printBase64Binary(str.getBytes(StandardCharsets.UTF_8)));
            ConnBlocks.log(str);
            ConnBlocks.log("Testing read: ");
			BlockMap obj = BlockMap.mapper().readValue(str, BlockMap.class).onLoad();
            ConnBlocks.log("Testing read: " + obj);
			return obj;
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ConnectingBlocks getConnectionWithId(int i) {
		for (ConnectingBlocks conn : this.connections)
			if (conn.getId() == i)
				return conn;
		return null;
	}

	public void disconnect() {
		for (ConnectingBlocks conn : new ArrayList<ConnectingBlocks>(this.connections)) {
			conn.checkDisconnections();
		}
	}
	public void executeAllAreas() {
		for (BlockArea area : this.areas) {
			area.execute();
		}
		this.connect();
		this.disconnect();
	}
	
}
