package operators

import configuration.Configuration

class ApplicationStartupOperator {
    fun setupArgs(args: Array<String>){
        args.forEachIndexed {index, arg ->
            if(arg == "-h"){
                Configuration.HOST = args[index + 1]
            }
            if(arg == "-p"){
                Configuration.PORT = args[index + 1].toInt()
            }
            if(arg == "-cn"){
                Configuration.MAX_NUMBER_OF_CRAWLERS = args[index + 1].toInt()
            }
            if(arg == "-localsave"){
                Configuration.SAVE_FILE_LOCATION = args[index + 1]
            }
            if(arg == "-localsave"){
                Configuration.SAVE_FILE_LOCATION = args[index + 1]
            }
            if(arg == "-timeout"){
                Configuration.TIME_BETWEEN_FETCHING = args[index + 1].toLong()
            }
        }
    }
}