import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import net.zomis.connblocks.common.App


fun main() = application {
    val windowState = rememberWindowState(position = WindowPosition.Aligned(Alignment.Center))
    Window(onCloseRequest = ::exitApplication, state = windowState) {
        App()
    }
}
