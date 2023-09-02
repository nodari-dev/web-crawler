package interfaces

import dto.HashedUrlPair

interface IURLValidator {
    fun canProcessURL(host: String, hashedUrlPair: HashedUrlPair?): Boolean
}