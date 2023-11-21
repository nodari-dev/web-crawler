package configuration

object Configuration{
    var HOST = "localhost"
    var PORT = 6879
    var TIME_BETWEEN_FETCHING: Long = 3500
    var MAX_NUMBER_OF_CRAWLERS: Int = 1
    var SAVE_FILE_LOCATION: String = "./crawlingResults"
}