package dadm.jromsev.sportnew.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.SettingsBinding
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar botón de retorno
        binding.toolbar.findViewById<ImageButton>(R.id.btn_return).setOnClickListener {
            finish()
        }

        // Configurar el selector de idioma
        val currentLocale = resources.configuration.locales[0]
        when (currentLocale.language) {
            "es" -> binding.languageRadioGroup.check(R.id.radioSpanish)
            "it" -> binding.languageRadioGroup.check(R.id.radioItalian)
            else -> binding.languageRadioGroup.check(R.id.radioEnglish)
        }

        binding.languageRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val langCode = when (checkedId) {
                R.id.radioSpanish -> "es"
                R.id.radioItalian -> "it"
                else -> "en"
            }

            val locale = Locale(langCode)
            Locale.setDefault(locale)

            val config = Configuration()
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

            // Actualizar el texto del modo noche según el nuevo idioma
            updateNightModeText()

            // Reiniciar la actividad para aplicar los cambios
            recreate()
        }

        // Configurar el switch de modo noche
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding.nightModeSwitch.isChecked = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        updateNightModeText()

        binding.nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            updateNightModeText()
            // Reiniciar la actividad para aplicar los cambios
            recreate()
        }
    }

    private fun updateNightModeText() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding.nightModeText.text = if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            getString(R.string.night_mode_activated)
        } else {
            getString(R.string.night_mode_disabled)
        }
    }
}