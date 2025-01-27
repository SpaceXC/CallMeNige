import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import auth.ui.AuthScreen
import block.ui.BlockScreen
import utils.readCredentialsFromFile

enum class Screens {
    Auth, Block
}

@Composable
@Preview
fun App() {
    var currentScreen: Screens? by remember { mutableStateOf(null) }
    var accessToken by remember { mutableStateOf("") }
    var accessTokenSecret by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val credentials = readCredentialsFromFile()
        if (credentials != null) {
            accessToken = credentials.first
            accessTokenSecret = credentials.second
            currentScreen = Screens.Block
        }
        else {
            currentScreen = Screens.Auth
        }
    }
    when (currentScreen) {
        Screens.Auth -> AuthScreen {
            accessToken = it.first
            accessTokenSecret = it.second
            currentScreen = Screens.Block
        }

        Screens.Block -> BlockScreen(accessToken, accessTokenSecret)
        null -> {}
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
