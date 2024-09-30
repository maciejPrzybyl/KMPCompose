package org.macpry.kmpcompose.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.core.use
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Single

@Single
class Networking(
    private val client: HttpClient,
    //TODO Inject IO dispatcher
    //private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getImages() = withContext(Dispatchers.Default) {
        runCatching {
            client.use {
                it.get("https://picsum.photos/v2/list").body<List<ImageResponse>>()
            }
        }
    }

    @Serializable
    data class ImageResponse(
        val id: Long,
        @SerialName("download_url")
        val url: String,
        val author: String
    )
}
