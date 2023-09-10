package exceptions

import java.io.IOException

class FetchingFailedException(message: String): IOException(message)