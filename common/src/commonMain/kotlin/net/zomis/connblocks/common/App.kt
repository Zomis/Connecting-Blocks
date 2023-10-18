package net.zomis.connblocks.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import net.zomis.connblocks.core.*
import net.zomis.connblocks.core.grids.GridPoint

@Composable
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }
    val platformName = getPlatformName()
    val blockMap = remember {
        BlockMap(8, 8).apply {
            addConnection(Point(4, 4)).apply { keywords += Keywords.Controllable(playerIndex = 0) }
        }
    }
    val selectedConnection = blockMap.connections.firstOrNull { c ->
        c.keywords.filterIsInstance<Keywords.Controllable>().any { it.playerIndex == 0 }
    }
    val keyboardController = remember {
        KeyboardController(blockMap, selectedConnection)
    }
    var invalidations by remember { mutableStateOf(0) }
    val requester = remember { FocusRequester() }

    Box(
        Modifier
            .onKeyEvent {
                if (it.type == KeyEventType.KeyDown && keyboardController.handleKeyEvent(it)) {
                    invalidations++
                    true
                } else false
            }
            .focusRequester(requester)
            .focusable()
            .size(0.dp)
    )
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }

    // TODO: Add viewport to only show parts of map? Allow zoom, pan etc.

    Column {
        Button(modifier = Modifier.focusable(false), onClick = {
            text = "Hello, ${platformName}"
        }) {
            Text(text)
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            invalidations.apply {} // trigger redraws whenever `invalidations` value changes
            blockMap.grid.all().forEach {
                drawTile(it)
            }
            blockMap.connections.forEach {
                drawConnection(it)
            }
        }
    }
}

fun DrawScope.drawTile(tile: GridPoint<Tile>) {
    val color = when (tile.value.type) {
        TileType.Blocked -> Color.Black
        TileType.Open -> Color.LightGray
    }
    drawRect(
        color = color,
        topLeft = Offset(tile.x * 32f, tile.y * 32f),
        size = Size(32f, 32f),
        colorFilter = null,
        blendMode = DrawScope.DefaultBlendMode,
    )
}

fun DrawScope.drawConnection(connection: Connection) {
    val origin = connection.position
    connection.blocks.all().forEach {
        val x = origin.x + it.point.x
        val y = origin.y + it.point.y
        drawRect(
            color = Color.Blue,
            topLeft = Offset(x * 32f, y * 32f),
            size = Size(32f, 32f),
            colorFilter = null,
            blendMode = DrawScope.DefaultBlendMode,
        )
    }
}