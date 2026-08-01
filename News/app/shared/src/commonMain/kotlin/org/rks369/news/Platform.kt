package org.rks369.news

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform