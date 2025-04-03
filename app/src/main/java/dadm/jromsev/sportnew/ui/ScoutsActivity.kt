package dadm.jromsev.sportnew.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.ScoutsBinding

class ScoutsActivity : AppCompatActivity() {
    private lateinit var binding: ScoutsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScoutsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar botón de settings
        binding.toolbar.findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavBar.btnTrophy.setOnClickListener {
            if (!this::class.java.simpleName.contains("SearchResults")) {
                startActivity(Intent(this, SearchResultsActivity::class.java))
                finish()
            }
        }

        binding.bottomNavBar.btnPlayer.setOnClickListener {
            if (!this::class.java.simpleName.contains("SearchPlayers")) {
                startActivity(Intent(this, SearchPlayersActivity::class.java))
                finish()
            }
        }

        binding.bottomNavBar.btnEye.setOnClickListener {
            // Ya estamos en scouts, no hacemos nada
        }
    }
}