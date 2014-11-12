package net.zomis.connblocks.areas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.zomis.connblocks.AreaStrategy;
import net.zomis.connblocks.BlockArea;

public class AreaIfFilled implements AreaStrategy {

	private final BlockArea source;
	private final AreaStrategy strategy;
	
	@JsonCreator
	public AreaIfFilled(@JsonProperty("source") BlockArea source, @JsonProperty("strategy") AreaStrategy strategy) {
		this.source = source;
		this.strategy = strategy;
	}
	
	@Override
	public void execute(BlockArea area) {
		if (source.isFilled())
			this.strategy.execute(area);
	}
}
