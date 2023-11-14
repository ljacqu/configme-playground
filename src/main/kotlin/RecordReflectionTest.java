import beantest.GradeRecord;
import ch.jalu.configme.internal.record.RecordInspector;
import kotdataclasstest.WeatherData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.RecordComponent;

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
        RecordInspector inspector = RecordInspector.getInspector();
        System.out.println("Is record: " + inspector.isRecord(GradeRecord.class));
        for (ch.jalu.configme.internal.record.RecordComponent recordComponent : inspector.getRecordComponents(GradeRecord.class)) {
            System.out.println(recordComponent.getName() + ": "
                + recordComponent.getType() + " (" + recordComponent.getGenericType() + ")");
        }
    }
}
