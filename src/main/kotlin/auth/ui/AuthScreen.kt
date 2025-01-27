package auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthScreen(onNext: (Pair<String, String>) -> Unit) {
    val viewModel = remember { AuthScreenViewModel() }
    val isLoading by viewModel.isLoading.collectAsState()
    val oauthUrl by viewModel.oauthUrl.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getOauthUrl()
    }

    var textFieldValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (!isLoading) {
            Text("Step1: 登录", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Text(buildAnnotatedString {
                withLink(
                    LinkAnnotation.Url(
                        oauthUrl,
                        TextLinkStyles(style = SpanStyle(color = Color.Blue))
                    )
                ) {
                    append("点击此处")
                }
                append("以登录")
            })

            SelectionContainer {
                Text(oauthUrl)
            }

            Text(buildAnnotatedString {
                append("登录完成后，你将会在浏览器中看到一串")
                withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                    append("7位数字的PIN码")
                }
            })
            Text("请将它输入在下面⬇️")
            TextField(textFieldValue, onValueChange = {
                textFieldValue = viewModel.validateInputValue(it, textFieldValue)
            })
            Button(onClick = {
                viewModel.getAccessToken(textFieldValue, onNext)
            }) {
                Text("下一步")
            }
            Text(errorMessage, color = Color.Red)
        } else {
            Text("加载中...")
        }
    }
}