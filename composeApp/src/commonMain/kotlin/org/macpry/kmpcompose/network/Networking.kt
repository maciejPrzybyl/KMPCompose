package org.macpry.kmpcompose.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.core.use
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.macpry.kmpcompose.providers.KMPDispatchers

@Single
class Networking(
    private val client: HttpClient,
    @Named(KMPDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getImages() = withContext(ioDispatcher) {
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
