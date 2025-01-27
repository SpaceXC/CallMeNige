package auth.ui

import androidx.compose.runtime.remember
import auth.domain.AuthUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthScreenViewModel {
    val isLoading = MutableStateFlow(true)
    val errorMessage = MutableStateFlow("")
    val oauthUrl = MutableStateFlow("")

    fun getOauthUrl() {
        CoroutineScope(Dispatchers.IO).launch {
            isLoading.value = true
            oauthUrl.value = AuthUtils.getOauthUrl()
            isLoading.value = false
        }
    }

    fun validateInputValue(value: String, prevValue: String): String {
        if (value.length > 7) return prevValue
        if (value.isEmpty()) return ""
        if (!value.matches(Regex("\\d+"))) return prevValue
        return value
    }

    fun getAccessToken(pin: String, onNext: (Pair<String, String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            isLoading.value = true
            try {
                val accessToken = AuthUtils.getAccessKey(pin)
                onNext(accessToken)
            } catch (e: Exception) {
                errorMessage.value = "出错了！${e.message}"
                isLoading.value = false
            }
        }
    }
}