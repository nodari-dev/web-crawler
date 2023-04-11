package crawler

import fetcher.Fetcher
import frontier.Frontier
import parser.Parser

data class Components(
    val fetcher: Fetcher,
    val parser: Parser,
    val frontier: Frontier
)