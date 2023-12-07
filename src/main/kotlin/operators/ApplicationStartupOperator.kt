package operators

import configuration.Configuration

class ApplicationStartupOperator {
    fun setupArgs(args: Array<String>){
        args.forEachIndexed {index, arg ->
            when (arg){
                "-h" -> Configuration.IN_MEMORY_DB_HOST = args[index + 1]
                "-p" -> Configuration.IN_MEMORY_DB_PORT = args[index + 1].toInt()
                "-cn"-> Configuration.MAX_NUMBER_OF_CRAWLERS = args[index + 1].toInt()
                "-timeout"-> Configuration.CRAWLING_DELAY = args[index + 1].toLong()
            }
        }
    }
}