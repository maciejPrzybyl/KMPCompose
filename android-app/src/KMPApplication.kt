import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KMPApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            logger(KoinLogger())
            androidContext(this@KMPApplication)
            modules(appModule())
        }
    }
}
