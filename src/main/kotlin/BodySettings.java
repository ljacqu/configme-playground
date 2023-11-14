import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.IntegerProperty;
import ch.jalu.configme.properties.Property;

public class BodySettings implements SettingsHolder {

    @Comment("""
        Test
        Testeroni

        """)
    public static final Property<Integer> P = new IntegerProperty("p", 9);

}
