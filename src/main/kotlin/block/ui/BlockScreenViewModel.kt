package block.ui

import block.domain.getMyBlocked
import block.domain.getMyFollowers
import block.domain.getMyFollowings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import twitter4j.TwitterException
import utils.initializeTwitterClient
import twitter4j.v1.Query
import twitter4j.v1.User
import java.io.File
import kotlin.math.max


class BlockScreenViewModel(accessToken: String, accessTokenSecret: String) {
    val isLoading = MutableStateFlow(true)

    val niJies = MutableStateFlow(emptyMap<Long, Boolean>())

    val twitter = initializeTwitterClient(accessToken, accessTokenSecret)!!
    val twitterV1 = twitter.v1()

    val executedCount = MutableStateFlow(0)

    fun getNiJies(page: Int = 1, maxPage: Int = 20) {
        if (page > maxPage) return
        val result = twitterV1.users().searchUsers("叫妮姐", page)
        niJies.value += result.map { it.id }.associateWith { false }
        getNiJies(page + 1, maxPage)
    }

    fun getNiJies() {
        CoroutineScope(Dispatchers.IO).launch {
            val file = File("sinner.txt")
            niJies.value = file.readLines().associate { it.toLong() to false } - getMyBlocked(twitterV1) - getMyFollowings(twitterV1) - getMyFollowers(twitterV1)
            isLoading.value = false
        }
    }

    fun banNiJies() {
        CoroutineScope(Dispatchers.IO).launch {
            val usersToBan = niJies.value
            val users = twitterV1.users()
            for (userId in usersToBan) {
                println("CurrentSinner: $userId")
                try {
                    users.createBlock(userId.key)
                    val newMap = niJies.value.toMutableMap()
                    newMap[userId.key] = true
                    niJies.value = newMap
                    executedCount.value++
                } catch (e: TwitterException) {
                    if (e.errorCode == 50) {
                        println("User not found")
                    }
                }
                delay(6000)
            }
        }
    }
}