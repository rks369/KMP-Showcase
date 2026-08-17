package org.rks369.news

import android.app.Application
import org.rks369.news.platform.AndroidAppContext

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidAppContext.context = this
    }
}
