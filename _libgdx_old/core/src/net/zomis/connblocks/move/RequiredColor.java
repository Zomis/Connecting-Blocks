package net.zomis.connblocks.move;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.zomis.connblocks.Block;
import net.zomis.connblocks.BlockTile;
import net.zomis.connblocks.MoveOrder;
import net.zomis.connblocks.MoveStrategy;

public class RequiredColor implements MoveStrategy {
	@JsonProperty
	private int required;

    public int getRequired() {
        return required;
    }

    RequiredColor() {
	}
	public RequiredColor(int required) {
		this.required = required;
	}

	@Override
	public boolean canMove(BlockTile tile, Block block, MoveOrder order) {
		int color = block.getConnection().getConnectGroups();
        if ((required & color) != required) {
            return false;
        }
        return true;
	}
	
}
