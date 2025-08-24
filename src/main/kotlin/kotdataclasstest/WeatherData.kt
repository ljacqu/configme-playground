package kotdataclasstest

import ch.jalu.configme.Comment

data class WeatherData(@field:Comment("Current temperature") var temp: Double, val location: String)
