package org.macpry.kmpcompose.managers.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.core.use
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
class Networking(
    //TODO Inject client
    //private val client: HttpClient,
    //TODO Inject IO dispatcher
    //private val ioDispatcher: CoroutineDispatcher
) {

    //TODO Inject client
    private val client = HttpClient()

    suspend fun getImages() = withContext(Dispatchers.Default) {
        runCatching {
            client.use {
                it.get("https://picsum.photos/v2/list").body<ImagesResponse>()
            }
        }
    }

    data class ImagesResponse(
        val images: List<ImageResponse>
    )

    data class ImageResponse(
        val id: Long,
        val url: String
    )
}