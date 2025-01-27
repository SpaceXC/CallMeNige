package auth.domain

import androidx.compose.material.MaterialTheme.shapes
import com.github.scribejava.apis.TwitterApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1AccessToken
import org.jetbrains.skia.Drawable
import utils.apiKey
import utils.apiSecret
import java.io.File
import java.io.PrintWriter

object AuthUtils {
    private val service = createTwitterService()
    private val requestToken = service.requestToken

    fun getOauthUrl(): String {
        return service.getAuthorizationUrl(requestToken)
    }

    fun getAccessKey(code: String): Pair<String, String> /*accessToken, accessTokenSecret*/ {
        try {
            val accessToken = service.getAccessToken(requestToken, code)
            saveAccessTokenToFile(accessToken)
            return Pair(accessToken.token, accessToken.tokenSecret)
        } catch (e: Exception) {
            throw e
        }
    }

    private fun saveAccessTokenToFile(accessToken: OAuth1AccessToken) {
        val file = File("twitter_credentials.properties")
        PrintWriter(file).use { writer ->
            writer.println("accessToken=${accessToken.token}")
            writer.println("accessTokenSecret=${accessToken.tokenSecret}")
        }
        println("Credentials saved to ${file.absolutePath}")
    }

    private fun createTwitterService() =
        ServiceBuilder(apiKey)
            .apiSecret(apiSecret)
            .build(TwitterApi.instance())
}