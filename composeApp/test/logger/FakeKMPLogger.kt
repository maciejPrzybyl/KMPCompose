class FakeKMPLogger(private val handledException: (Throwable) -> Unit) : IKMPLogger {

    override fun logError(exception: Throwable) {
        handledException(exception)
    }
}