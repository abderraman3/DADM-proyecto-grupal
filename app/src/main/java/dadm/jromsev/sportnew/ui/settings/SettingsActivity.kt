package dadm.jromsev.sportnew.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
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
import kotlin.math.max

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsBinding
    private lateinit var preferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "AppSettings"
        private const val KEY_LANGUAGE = "language"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa las preferencias
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Aplica el idioma guardado antes de establecer el diseño
        applyLanguage()

        enableEdgeToEdge()
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.displayCutout() or
                        WindowInsetsCompat.Type.systemBars()
            )

            // Applica padding solo al contenuto (non alla toolbar)
            binding.linearLayoutSettings1.updatePadding(
                left = bars.left,
                top = 0, // La toolbar gestirà il suo padding
                right = bars.right,
                bottom = bars.bottom
            )
            binding.linearLayoutSettings2.updatePadding(
                left = bars.left,
                top = 0, // La toolbar gestirà il suo padding
                right = bars.right,
                bottom = bars.bottom
            )

            // Estendi la toolbar dietro la status bar
            binding.toolbar.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = 0
            )

            WindowInsetsCompat.CONSUMED
        }
        setupReturnButton()
        setupLanguageSelector()
        setupNightModeSwitch()
    }

    private fun applyLanguage() {
        // Obtiene el idioma guardado o usa el predeterminado del sistema
        val savedLanguage = preferences.getString(KEY_LANGUAGE, null)
        if (savedLanguage != null) {
            val locale = Locale(savedLanguage)
            Locale.setDefault(locale)

            val config = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }




    private fun setupReturnButton() {
        binding.toolbar.findViewById<ImageButton>(R.id.btn_return).setOnClickListener {
            finish()
        }
    }

    private fun setupLanguageSelector() {
        val languagesDisplay = resources.getStringArray(R.array.languages_display)
        val languagesValues = resources.getStringArray(R.array.languages_values)

        // Obtiene el idioma actual (o el guardado si está disponible)
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
    }

    private fun setupNightModeSwitch() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding.nightModeSwitch.isChecked = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        updateNightModeText()

        binding.nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            // Reinicia la actividad correctamente
            restartActivity()
        }
    }

    private fun changeAppLanguage(langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Guarda el idioma en las preferencias
        preferences.edit().putString(KEY_LANGUAGE, langCode).apply()

        // Reinicia la actividad para aplicar los cambios
        restartActivity()
    }

    private fun updateNightModeText() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding.nightModeText.text = if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            getString(R.string.night_mode_activated)
        } else {
            getString(R.string.night_mode_disabled)
        }
    }

    private fun restartActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        finish()
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}