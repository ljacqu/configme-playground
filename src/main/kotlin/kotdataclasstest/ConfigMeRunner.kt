package kotdataclasstest

import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.SettingsManagerBuilder
import ch.jalu.configme.beanmapper.MapperImpl
import ch.jalu.configme.beanmapper.definition.BeanDefinition
import ch.jalu.configme.beanmapper.definition.BeanDefinitionServiceImpl
import ch.jalu.configme.beanmapper.definition.properties.BeanPropertyDefinition
import ch.jalu.configme.beanmapper.definition.properties.BeanPropertyExtractorImpl
import ch.jalu.configme.beanmapper.leafvaluehandler.LeafValueHandlerImpl
import ch.jalu.configme.beanmapper.leafvaluehandler.LeafValueHandlerImpl.createDefaultLeafTypes
import ch.jalu.configme.internal.ReflectionHelper
import ch.jalu.configme.internal.record.RecordInspectorImpl
import ch.jalu.configme.properties.BeanProperty
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer
import java.lang.reflect.Field
import java.nio.file.Paths

class ConfigMeRunner {

    fun init() {
        val settingsManager = SettingsManagerBuilder
            .withYamlFile(Paths.get("./config_me.yaml"))
            .configurationData(DemoSettingsHolder::class.java)
            .useDefaultMigrationService()
            .create()

        val weatherData = settingsManager.getProperty(DemoSettingsHolder.WEATHER)

        println("The weather is ${weatherData.temp}°C in ${weatherData.location}")
    }
}

class DemoBeanDefinitionService : BeanDefinitionServiceImpl(RecordInspectorImpl(ReflectionHelper()), DemoBeanPropertyDefinitionExtractor) {

    override fun createDefinitionIfApplicable(clazz: Class<*>): BeanDefinition? {
        if (clazz.kotlin.isData) {
            return KotlinDataBeanDefinition(clazz.kotlin)
        }
        return super.createDefinitionIfApplicable(clazz)
    }
}

object DemoBeanPropertyDefinitionExtractor : BeanPropertyExtractorImpl() {

    override fun validateFieldForBean(clazz: Class<*>, field: Field) {
        // Do nothing
    }

    override fun validateProperties(clazz: Class<*>, properties: MutableCollection<out BeanPropertyDefinition>) {
        // Do nothing
    }
}

object DemoMapper : MapperImpl(DemoBeanDefinitionService(), LeafValueHandlerImpl(createDefaultLeafTypes())) {

}

object DemoSettingsHolder : SettingsHolder {

    val NAME: Property<String> = PropertyInitializer.newProperty("name", "Demo")

    val WEATHER = BeanProperty("meteo", WeatherData::class.java, WeatherData(12.0, "T"), DemoMapper)

}

fun main() {
    ConfigMeRunner().init()
}