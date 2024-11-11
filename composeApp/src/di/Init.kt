import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        logger(KoinLogger())
        modules(appModule())
    }
}
