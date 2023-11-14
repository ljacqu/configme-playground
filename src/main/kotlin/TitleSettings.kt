import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.PropertyInitializer.newProperty

object TitleSettings: SettingsHolder {

    @Comment("Demo")
    val TITLE_TEXT = newProperty("title.text", "woof")

}