import beantest.GradeRecord;
import ch.jalu.configme.beanmapper.definition.BeanDefinition;
import ch.jalu.configme.beanmapper.definition.BeanDefinitionService;
import ch.jalu.configme.beanmapper.definition.BeanDefinitionServiceImpl;
import ch.jalu.configme.beanmapper.definition.properties.BeanPropertyDefinition;
import ch.jalu.configme.internal.ReflectionHelper;
import ch.jalu.configme.internal.record.RecordInspector;
import ch.jalu.configme.internal.record.RecordInspectorImpl;
import kotdataclasstest.WeatherData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.RecordComponent;
import java.util.Optional;

public class RecordReflectionTest {

    public static void main(String... args) {
        System.out.println("isRecord(): " + GradeRecord.class.isRecord());

        System.out.println("Record components:");
        for (RecordComponent recordComponent : GradeRecord.class.getRecordComponents()) {
            System.out.println(" - " + recordComponent.getName() + " [" + recordComponent.getType() + "]");
            recordComponent.getAnnotations();
            for (Annotation annotation : recordComponent.getAnnotations()) {
                System.out.println("   - annotation: " + annotation.annotationType().getSimpleName());
            }
        }

        System.out.println();
        System.out.println("Kotlin data class");
        System.out.println("isRecord(): " + WeatherData.class.isRecord());
        for (Constructor<?> constructor : WeatherData.class.getConstructors()) {
            System.out.println("Constructor:");
            for (Parameter param : constructor.getParameters()) {
                System.out.println(" - " + param.getName() + " [" + param.getType() + "]");
            }
        }

        System.out.println();
        System.out.println("ConfigMe RecordInspector test");
        RecordInspector inspector = new RecordInspectorImpl(new ReflectionHelper());
        System.out.println("Is record: " + inspector.getRecordComponents(GradeRecord.class));
        for (ch.jalu.configme.internal.record.RecordComponent recordComponent : inspector.getRecordComponents(GradeRecord.class)) {
            System.out.println(recordComponent.getName() + ": "
                + recordComponent.getType() + " (" + recordComponent.getGenericType() + ")");
        }

        BeanDefinitionServiceImpl beanDefinitionService = new BeanDefinitionServiceImpl();
        BeanDefinition definition = beanDefinitionService.findDefinition(GradeRecord.class).orElseThrow();
        System.out.println();
        System.out.println("GradeRecord property definitions:");
        for (BeanPropertyDefinition property : definition.getProperties()) {
            System.out.println(" - " + property.getName() + " (" + property.getTypeInformation() + ")");
        }
    }
}
