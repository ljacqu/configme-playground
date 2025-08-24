package kotdataclasstest

import ch.jalu.configme.Comment
import ch.jalu.configme.beanmapper.definition.BeanDefinition
import ch.jalu.configme.beanmapper.definition.properties.BeanPropertyComments
import ch.jalu.configme.beanmapper.definition.properties.BeanPropertyDefinition
import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder
import ch.jalu.typeresolver.TypeInfo
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaType


class KotlinDataBeanDefinition(private val klz: KClass<*>) : BeanDefinition {

    @Suppress("UNCHECKED_CAST")
    override fun getProperties(): MutableList<BeanPropertyDefinition> {
        require(klz.isData) {
            "Expected ${klz.simpleName} to be a data class"
        }

        val properties = klz.memberProperties.associateBy { it.name }
        return klz.primaryConstructor!!.parameters.map { param ->
            val property = properties[param.name!!] as KProperty1<Any, *>
            DataClassPropertyDefinition(param, property)
        }.toMutableList()
    }

    override fun create(parameters: MutableList<Any>, errorRecorder: ConvertErrorRecorder): Any {
        val ctor = klz.primaryConstructor!!
        if (parameters.size != ctor.parameters.size) {
            error("Expected ${ctor.parameters.size} parameters but got: ${parameters.size}")
        }
        return ctor.call(*parameters.toTypedArray())
    }

    /**
     * Bean property definition based on a property class's parameter.
     */
    private class DataClassPropertyDefinition(private val param: KParameter,
                                              private val property: KProperty1<Any, *>) : BeanPropertyDefinition {

        override fun getName(): String = param.name!!

        override fun getTypeInformation(): TypeInfo = TypeInfo(param.type.javaType)

        override fun getValue(instance: Any): Any? = property.get(instance)

        override fun getComments(): BeanPropertyComments {
            // TODO: Comments are not found
            val comments = property.findAnnotation<Comment>()?.let { annotation ->
                val uuid = if (annotation.repeat) UUID.randomUUID() else null
                BeanPropertyComments(annotation.value.asList(), uuid)
            }
            return comments ?: BeanPropertyComments.EMPTY
        }
    }
}
