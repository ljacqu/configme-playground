package kotdataclasstest

import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.SettingsManagerBuilder
import ch.jalu.configme.beanmapper.MapperImpl
import ch.jalu.configme.beanmapper.definition.BeanDefinition
import ch.jalu.configme.beanmapper.definition.BeanDefinitionServiceImpl
import ch.jalu.configme.beanmapper.leafvaluehandler.LeafValueHandlerImpl
import ch.jalu.configme.beanmapper.leafvaluehandler.LeafValueHandlerImpl.createDefaultLeafTypes
import ch.jalu.configme.properties.BeanProperty
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer
import java.nio.file.Paths
import kotlin.random.Random


fun main() {
    val settingsManager = SettingsManagerBuilder
        .withYamlFile(Paths.get("./config_me.yaml"))
        .configurationData(DemoSettingsHolder::class.java)
        .useDefaultMigrationService()
        .create()

    val weatherData = settingsManager.getProperty(DemoSettingsHolder.WEATHER)

    println(settingsManager.getProperty(DemoSettingsHolder.NAME))
    println()
    println("The weather is ${weatherData.temp}°C in ${weatherData.location}")

    val sign = if (Random.nextBoolean()) 1 else -1
    weatherData.temp += 1 * sign

    settingsManager.save()
}


/**
 * Bean definition service which also supports Kotlin data classes.
 */
class DemoBeanDefinitionService : BeanDefinitionServiceImpl() {

    override fun createDefinitionIfApplicable(clazz: Class<*>): BeanDefinition? {
        if (clazz.kotlin.isData) {
            return KotlinDataBeanDefinition(clazz.kotlin)
        }
        return super.createDefinitionIfApplicable(clazz)
    }
}

/** Mapper extension to support Kotlin data classes. */
object DemoMapper : MapperImpl(DemoBeanDefinitionService(), LeafValueHandlerImpl(createDefaultLeafTypes()))

object DemoSettingsHolder : SettingsHolder {

    val NAME: Property<String> = PropertyInitializer.newProperty("name", "Demo")

    val WEATHER = BeanProperty("meteo", WeatherData::class.java, WeatherData(12.0, "T"), DemoMapper)

}
