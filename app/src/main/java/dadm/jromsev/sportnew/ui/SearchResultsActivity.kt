package dadm.jromsev.sportnew.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.SearchResultsBinding

class SearchResultsActivity : AppCompatActivity() {
    private lateinit var binding: SearchResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar botón de settings
        binding.toolbar.findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_trophy).setOnClickListener {
            // Ya estamos en search_results, no hacemos nada
        }

        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_player).setOnClickListener {
            if (!this::class.java.simpleName.contains("SearchPlayers")) {
                startActivity(Intent(this, SearchPlayersActivity::class.java))
                finish()
            }
        }

        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_eye).setOnClickListener {
            if (!this::class.java.simpleName.contains("Scouts")) {
                startActivity(Intent(this, ScoutsActivity::class.java))
                finish()
            }
        }
    }
}