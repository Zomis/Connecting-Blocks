package net.zomis.connblocks.core

import net.zomis.connblocks.core.grids.ExpandableGrid
import net.zomis.connblocks.core.grids.Grid
import net.zomis.connblocks.core.grids.GridImpl

class Block(var offset: Point) {
    val keywords = mutableSetOf<Keyword>()
}

enum class TileType {
    Blocked,
    Open,
    ;
}
class Tile(val x: Int, val y: Int, var type: TileType = TileType.Open)

class Connection(origin: Point) {
    var position: Point = origin
    val keywords = mutableSetOf<Keyword>()
    val blocks = ExpandableGrid<Block>().apply {
        set(0, 0, Block(Point(0, 0)))
    }

    fun tryMove(direction: Direction4) {
        position += direction.delta()
    }
}

class BlockMap(val sizeX: Int, val sizeY: Int) {
    val grid: Grid<Tile> = GridImpl(sizeX, sizeY) { x, y ->
        Tile(x, y)
    }
    private val _connections = mutableSetOf<Connection>()
    val connections: Set<Connection> = _connections

    fun addConnection(origin: Point): Connection {
        val conn = Connection(origin)
        _connections.add(conn)
        return conn
    }
}

interface Keyword
enum class BlockColor {
    Black, // Black connects to nothing, not even black itself
    Blue,
    Red,
    Green,
    White, // White connects to everything
    ;
}

object Keywords {

    // Other blocks:
    class Reformer : Keyword
    class Breaker : Keyword
    class LimitedDirections(val allowed: Set<Direction4>) : Keyword
    object Goal : Keyword
    class NoGoal(val color: Set<BlockColor>) : Keyword// Requires NONE of the specified colors to finish the level.
    class LimitedSize(val allowedRange: IntRange): Keyword
    class BlockLink: Keyword
    class Isolation: Keyword

    // Part of connections:
    class Colors(val colors: Set<BlockColor>) : Keyword
    class Controllable(val playerIndex: Int) : Keyword// Controllable by a player
    // class Ghost(val passBy: CollisionLayer) : Keyword// Can pass through some stuff
    object Gliding : Keyword// Doesn't stop moving until it hits an obstacle
    object Pure : Keyword // Can only merge with your own color (e.g. pure blue cannot merge into a blue-red)

    // Pushing
    object Strong : Keyword// Can push normal weight and lighter objects
    object Heavy : Keyword// Cannot be pushed, not even by Strong objects
    object Light : Keyword// Can be pushed by normal-strength objects
    object Weak : Keyword// Cannot push even light objects



    // Modifier keywords:
    class Ephemeral(val keyword: Keyword, val mergesLeft: Int) : Keyword// remains until X merges
    class Limited(val keyword: Keyword, val maxSize: Int) : Keyword // remains until you've exceeded size X
}