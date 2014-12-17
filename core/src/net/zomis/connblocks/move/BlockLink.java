package net.zomis.connblocks.move;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.zomis.connblocks.Direction4;
import net.zomis.connblocks.IntPoint;
import net.zomis.connblocks.*;
import net.zomis.custommap.CustomFacade;


public class BlockLink implements NeighborStrategy, MoveStrategy {

	@JsonProperty
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
	private BlockTile pos;
	
	@JsonProperty
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
	private BlockTile	pos2;

	@JsonCreator
	private BlockLink() { this(null, null); }
	
	public BlockLink(BlockTile pos1, BlockTile pos2) {
		this.pos = pos1;
		this.pos2 = pos2;
		if (pos1 == null || pos2 == null)
			return;
		
		pos1.setNeighborStrategy(this);
		pos2.setNeighborStrategy(this);
		pos1.setMoveStrategyTo(this);
		pos2.setMoveStrategyTo(this);
	}

	@Override
	public BlockTile getBlockAt(BlockTile tile, Direction4 dir) {
		return tile.getRelative(dir.getDeltaX(), dir.getDeltaY());
//		throw new UnsupportedOperationException("Should not happen, but if it does: Return with default neighbor setting. Tile " + tile + " direction " + dir);
//		return null;
	}

	@Override
	public BlockTile redirectOnUsedAsNieghbor(BlockTile target, BlockTile usedBy, Direction4 directionLooking) {
		CustomFacade.getLog().i("Redirect on used as neighbor: " + target + " by " + usedBy + " dir " + directionLooking);
//		new Exception().printStackTrace();
		if (target == pos)
			return pos2.getNeighborUsingStrategy(directionLooking);
		else if (target == pos2)
			return pos.getNeighborUsingStrategy(directionLooking);
		else throw new UnsupportedOperationException("Neither pos1 or pos2. Should not happen. Target " + target + " used by " + usedBy 
				+ " direction " + directionLooking + " this pos=" +  pos + " pos2= " + pos2);
	}

	@Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
		BlockTile target;
		if (tile == pos)
			target = pos2;
		else if (tile == pos2)
			target = pos;
		else throw new UnsupportedOperationException("Assertion Error. " + tile + block + order);
		
		Direction4 dir = order.getDirection();
		order.setBlockTarget(block, new IntPoint(target.getX() + dir.getDeltaX(), target.getY() + dir.getDeltaY()));
		if (block.getEndSpot(order).getType() == BlockType.IMPASSABLE) {
            return false;
        }
        if (block.getEndSpot(order).getMoveStrategyTo() instanceof BlockLink) {
            return false;
        }
		return true;
	}

	@Override
	public String toString() {
		return "BlockLink for " + pos + ", " + pos2;
	}
	
}
