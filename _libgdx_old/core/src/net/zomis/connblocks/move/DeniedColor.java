package net.zomis.connblocks.move;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.zomis.connblocks.Block;
import net.zomis.connblocks.BlockTile;
import net.zomis.connblocks.MoveOrder;
import net.zomis.connblocks.MoveStrategy;

public class DeniedColor implements MoveStrategy {
	@JsonProperty
	private int denied;

    public int getDenied() {
        return denied;
    }

    DeniedColor() {
	}
	public DeniedColor(int denied) {
		this.denied = denied;
	}

	@Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
		int color = block.getConnection().getConnectGroups();
        if ((denied & color) != 0) {
            return false;
        }
        return true;
	}
	
}
