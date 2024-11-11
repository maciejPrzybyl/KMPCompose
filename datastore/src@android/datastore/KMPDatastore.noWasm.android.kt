import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun dataStorePath() = module {
    single { androidContext().filesDir.resolve(dataStoreFileName).absolutePath }
}
