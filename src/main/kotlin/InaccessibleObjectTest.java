import ch.jalu.configme.internal.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;

public class InaccessibleObjectTest {

    public static void main(String... args) throws NoSuchFieldException {
        //try {
            // Access an internal JDK class that is encapsulated by a module
            Class<?> internalClass = String.class;


            // Try to access a private field
            Field field = internalClass.getDeclaredField("value");

            // Attempt to make it accessible

            ReflectionHelper.setAccessibleIfNeeded(field);
        /*} catch (InaccessibleObjectException e) {
            System.out.println("Caught InaccessibleObjectException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
