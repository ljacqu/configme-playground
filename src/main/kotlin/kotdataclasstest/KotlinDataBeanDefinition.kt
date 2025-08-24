package kotdataclasstest

import ch.jalu.configme.beanmapper.definition.BeanDefinition
import ch.jalu.configme.beanmapper.definition.properties.BeanPropertyComments
import ch.jalu.configme.beanmapper.definition.properties.BeanPropertyDefinition
import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder
import ch.jalu.typeresolver.TypeInfo
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaType


class KotlinDataBeanDefinition(private val klz: KClass<*>) : BeanDefinition {

    override fun getProperties(): MutableList<BeanPropertyDefinition> {
        if (!klz.isData) {
            error("Expected ${klz.simpleName} to be a data class")
        }

        val components = klz.primaryConstructor!!.parameters
        return components.map {
            DataClassPropertyDefinition(klz, it)
        }.toMutableList()
    }

    override fun create(parameters: MutableList<Any>, errorRecorder: ConvertErrorRecorder): Any {
        val ctor = klz.primaryConstructor!!
        return ctor.call(*parameters.toTypedArray())
    }


    private class DataClassPropertyDefinition(private val klz: KClass<*>,
                                              private val param: KParameter) : BeanPropertyDefinition {

        override fun getName(): String = param.name!!

        override fun getTypeInformation(): TypeInfo = TypeInfo(param.type.javaType)

        @Suppress("UNCHECKED_CAST")
        override fun getValue(instance: Any): Any? {
            val properties = klz.memberProperties.associateBy { it.name }
            val property = properties[param.name!!] as KProperty1<Any, *>
            return property.get(instance)
        }

        override fun getComments(): BeanPropertyComments = BeanPropertyComments.EMPTY
    }
}