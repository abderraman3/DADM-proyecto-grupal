package dadm.jromsev.sportnew.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.SettingsBinding
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //padding no funciona
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.systemBars()
            )
            view.updatePadding(
                left = 0,
                top = bars.top,
                right = 0,
                bottom = 0
            )
            WindowInsetsCompat.CONSUMED
        }
        // Configurar botón de retorno
        binding.toolbar.findViewById<ImageButton>(R.id.btn_return).setOnClickListener {
            finish()
        }


        // Configurar el selector de idioma (ahora en el TextView)
        val languagesDisplay = resources.getStringArray(R.array.languages_display)
        val languagesValues = resources.getStringArray(R.array.languages_values)

        val currentLocale = resources.configuration.locales[0]
        val currentLangIndex = languagesValues.indexOf(currentLocale.language).coerceAtLeast(0)

        binding.tvLanguage.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.select_language))
                .setSingleChoiceItems(languagesDisplay, currentLangIndex) { dialog, which ->
                    val langCode = languagesValues[which]
                    changeAppLanguage(langCode)
                    dialog.dismiss()
                }
                .setNegativeButton(getString(android.R.string.cancel), null)
                .show()
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
            recreate()
        }
    }

    private fun changeAppLanguage(langCode: String) {
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

    private fun updateNightModeText() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding.nightModeText.text = if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            getString(R.string.night_mode_activated)
        } else {
            getString(R.string.night_mode_disabled)
        }
    }
}