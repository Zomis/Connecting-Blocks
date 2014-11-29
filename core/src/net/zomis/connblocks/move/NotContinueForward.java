package net.zomis.connblocks.move;

import com.fasterxml.jackson.annotation.*;
import net.zomis.connblocks.Direction4;
import net.zomis.connblocks.*;
import net.zomis.custommap.CustomFacade;

import java.util.LinkedList;
import java.util.List;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class NotContinueForward implements MoveStrategy {

	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
	private BlockTile tile;
	@JsonProperty
//	@JsonDeserialize(keyUsing=MyKey.class)
//	@JsonSerialize(keyAs=Block.class)
//	private Map<Block, Direction4> lastDirs = new HashMap<Block, Direction4>(); // Can not find a (Map) Key deserializer for type [simple type, class net.zomis.connblocks.Block]
	@ZomisNoEdit
	private List<BlockDir> lastDirs = new LinkedList<BlockDir>(); // Can not find a (Map) Key deserializer for type [simple type, class net.zomis.connblocks.Block]
	
	
	private static class BlockDir {
		public Block block;
		public Direction4 direction;
	}
	
	
	NotContinueForward() {
	}
	
	@JsonGetter
	private BlockTile getTile() {
		return tile;
	}
	
	@JsonSetter
	private void setTile(BlockTile tile) {
		this.tile = tile;
		tile.setMoveStrategyFrom(this);
		tile.setMoveStrategyTo(this);
	}
	
	public NotContinueForward(BlockTile pos) {
		this.tile = pos;
		pos.setMoveStrategyFrom(this);
		pos.setMoveStrategyTo(this);
	}

	@Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
		CustomFacade.getLog().i(this.getClass().getSimpleName() + " " + tile + block + order);
		if (block.getTile() == this.tile) {
			CustomFacade.getLog().i("block.tile == this.tile: " + this.lastDirs + " compared to " + order.getDirection());
			boolean isOK = getBlockDir(block) != order.getDirection();
			if (isOK) {
				this.removeBlockDir(block);
			}
			return isOK;
		}
		else if (tile == this.tile) {
			CustomFacade.getLog().i("tile == this.tile: " + this.lastDirs + " set to " + order.getDirection());
			addBlockDir(block, order.getDirection());
			return true;
		}
		
		else throw new UnsupportedOperationException("Once again, error 42. Did not expect this. " + tile + block + order);
	}

	private void removeBlockDir(Block block) {
		for (BlockDir bd : this.lastDirs) {
			if (bd.block == block) {
				lastDirs.remove(bd);
				return;
			}
		}
	}

	private void addBlockDir(Block block, Direction4 direction) {
		for (BlockDir bd : this.lastDirs) {
			if (bd.block == block) {
				bd.direction = direction;
				return;
			}
		}
		BlockDir bd = new BlockDir();
		bd.block = block;
		bd.direction = direction;
		lastDirs.add(bd);
	}

	private Direction4 getBlockDir(Block block) {
		for (BlockDir bd : this.lastDirs) {
			if (bd.block == block)
				return bd.direction;
		}
		return null;
	}

}
