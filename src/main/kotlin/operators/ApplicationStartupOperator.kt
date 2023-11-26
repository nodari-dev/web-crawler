package operators

import configuration.Configuration

class ApplicationStartupOperator {
    fun setupArgs(args: Array<String>){
        args.forEachIndexed {index, arg ->
            when (arg){
                "-h" -> Configuration.HOST = args[index + 1]
                "-p" -> Configuration.PORT = args[index + 1].toInt()
                "-cn"-> Configuration.MAX_NUMBER_OF_CRAWLERS = args[index + 1].toInt()
                "-localsave"-> Configuration.SAVE_FILE_LOCATION = args[index + 1]
                "-timeout"-> Configuration.TIME_BETWEEN_FETCHING = args[index + 1].toLong()
            }
        }
    }
}