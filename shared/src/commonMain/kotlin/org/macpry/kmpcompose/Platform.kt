package org.macpry.kmpcompose

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform