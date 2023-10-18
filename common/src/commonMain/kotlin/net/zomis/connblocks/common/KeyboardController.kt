package net.zomis.connblocks.common

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import net.zomis.connblocks.core.BlockMap
import net.zomis.connblocks.core.Connection
import net.zomis.connblocks.core.Direction4
import net.zomis.connblocks.core.Point

class KeyboardController(
    private val blockMap: BlockMap,
    private var selectedConnection: Connection?,
) {

    @OptIn(ExperimentalComposeUiApi::class)
    fun handleKeyEvent(it: KeyEvent): Boolean {
        when (it.key) {
            Key.DirectionLeft -> selectedConnection?.tryMove(Direction4.LEFT)
            Key.DirectionRight -> selectedConnection?.tryMove(Direction4.RIGHT)
            Key.DirectionUp -> selectedConnection?.tryMove(Direction4.UP)
            Key.DirectionDown -> selectedConnection?.tryMove(Direction4.DOWN)
            Key.C -> {
                blockMap.addConnection(Point(3, 3))
            }
            Key.Tab -> {}
            else -> return false
        }
        return true
    }

}
