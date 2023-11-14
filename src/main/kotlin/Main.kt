import ch.jalu.configme.SettingsManagerBuilder
import ch.jalu.configme.utils.FileUtils
import java.nio.file.Paths

fun main() {
    val path = Paths.get("./configme-kt-test.yml")
    FileUtils.createFileIfNotExists(path)

    val settings = SettingsManagerBuilder
        .withYamlFile(path)
        .configurationData(TitleSettings.javaClass, BodySettings::class.java)
        .create()

    println("App: ${settings.getProperty(TitleSettings.TITLE_TEXT)}")

    settings.save()
}