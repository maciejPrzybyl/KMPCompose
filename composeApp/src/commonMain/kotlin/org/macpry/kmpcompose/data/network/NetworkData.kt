package org.macpry.kmpcompose.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.core.use
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class NetworkData(
    private val client: HttpClient,
    private val ioDispatcher: CoroutineDispatcher
) {

    internal suspend fun getImages() = withContext(ioDispatcher) {
        client.use {
            it.get("https://picsum.photos/v2/list").body<List<ImageResponse>>()
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
