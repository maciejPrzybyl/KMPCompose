package org.macpry.kmpcompose.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface INetworkData {
    suspend fun getImages(): List<ImageResponse>
}

class NetworkData(
    private val client: HttpClient
) : INetworkData {

    override suspend fun getImages() =
        client.get("https://picsum.photos/v2/list").body<List<ImageResponse>>()

}

@Serializable
data class ImageResponse(
    val id: Long,
    @SerialName("download_url")
    val url: String,
    val author: String
)
