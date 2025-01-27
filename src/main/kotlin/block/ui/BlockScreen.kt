package block.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BlockScreen(accessToken: String, accessTokenSecret: String) {
    val viewModel = remember { BlockScreenViewModel(accessToken, accessTokenSecret) }
    val isLoading by viewModel.isLoading.collectAsState()
    val niJieName by remember { mutableStateOf("叫妮姐") }
    val executedCount by viewModel.executedCount.collectAsState()
    val niJies by viewModel.niJies.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getNiJies()
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (!isLoading) {
            Text("Step2: 处决！", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("你可能会发现这个过程特别缓慢，不用担心，这是为了防止被Elon Musk风控而刻意为之。")
            Text("坐和放宽，把这个小工具置于后台即可。需要退出这个小工具或者关闭电脑也没问题，下次打开时，会继续上次的进度。")
            Text("下面列出了需要Block的用户ID:")
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                niJies.forEach { niJie ->
                    item(key = niJie) {
                        Text(niJie.key.toString(), color = if (niJie.value) Color.Red else Color.Black)
                    }
                }
            }
            Button(onClick = {
                viewModel.banNiJies()
            }) {
                Text("处决！")
            }
            Text("已处决$executedCount/${niJies.size}")
        } else {
            Text("加载中...")
        }
    }
}