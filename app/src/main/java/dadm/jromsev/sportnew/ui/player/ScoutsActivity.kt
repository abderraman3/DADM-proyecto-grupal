package dadm.jromsev.sportnew.ui.player

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.ScoutsBinding
import dadm.jromsev.sportnew.ui.settings.SettingsActivity
import dadm.jromsev.sportnew.ui.event.SearchResultsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScoutsActivity : AppCompatActivity() {
    private lateinit var binding: ScoutsBinding

    private val viewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ScoutsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavBar) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.systemBars()
            )

            view.updatePadding(
                left = bars.left,
                top = 0,
                right = 0,
                bottom = bars.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
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
        // Configurar botón de settings
        binding.toolbar.findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        setupBottomNavigation()

        lifecycleScope.launch {
            val players = viewModel.getAllPlayers()
            val names = players.joinToString("\n") { it.player }
            binding.tvSavedPlayers.text = names
        }
    }

    private fun setupBottomNavigation() {
        // Acceder a los botones directamente desde el LinearLayout usando findViewById
        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_trophy).setOnClickListener {
            if (!this::class.java.simpleName.contains("SearchResults")) {
                startActivity(Intent(this, SearchResultsActivity::class.java))
                finish()
            }
        }

        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_player).setOnClickListener {
            if (!this::class.java.simpleName.contains("SearchPlayers")) {
                startActivity(Intent(this, SearchPlayersActivity::class.java))
                finish()
            }
        }

        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_eye).setOnClickListener {
            // Ya estamos en scouts, no hacemos nada
        }
    }
}