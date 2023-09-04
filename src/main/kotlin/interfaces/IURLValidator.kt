package interfaces

import dto.HashedURLPair

interface IURLValidator {
    fun canProcessURL(host: String, hashedUrlPair: HashedURLPair?): Boolean
}