package net.zomis.connblocks;

public enum BlockType {
	@Deprecated OPEN,
	IMPASSABLE, 
	GOAL, // TODO: GOAL and NOT_GOAL should be able to specify the connection color that has to be there GOAL.match & connection.color == GOAL.match. Use BlockAreas?
	NOT_GOAL,
	FREEZED, // Is it possible to use FREEZED as some kind of strategy instead? Not sure if it should be here or not.
	;
}
