package net.onest.time.application

import android.app.Application

class TimeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        @JvmStatic
        var application: Application? = null
            private set
    }
}
