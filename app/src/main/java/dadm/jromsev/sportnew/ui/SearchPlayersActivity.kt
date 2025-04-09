package dadm.jromsev.sportnew.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.SearchPlayersBinding
import dadm.jromsev.sportnew.ui.player.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchPlayersActivity : AppCompatActivity() {
    private lateinit var binding: SearchPlayersBinding
    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar botón de settings
        binding.toolbar.findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        setupBottomNavigation()

        // Observamos la lista de jugadores
        playerViewModel.players.observe(this) { playersList ->
            binding.textViewPlayers.text = if (playersList.isNotEmpty()) { //text view player añadido al textview del layout
                playersList.joinToString(", ") { it.player }
            } else {
                "No se han encontrado jugadores." // este texto esta hardcoding, habra que cambiarlo por un recurso de tipo string
            }
        }

        // Observamos errores
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerViewModel.errorState.collect { error ->
                    error?.let {
                        binding.textViewPlayers.text = it.message
                    }
                }
            }
        }

        // Al pulsar el botón, lanzamos la búsqueda manualmente
        binding.buttonSearch.setOnClickListener {
            val nombre = "Harry Kane"     // aquí puedes cambiar a lo que quieras
            val deporte = "Soccer"  // o conectar a un Spinner más adelante
            playerViewModel.getNewPlayers(nombre, deporte)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_trophy).setOnClickListener {
            if (!this::class.java.simpleName.contains("SearchResults")) {
                startActivity(Intent(this, SearchResultsActivity::class.java))
                finish()
            }
        }

        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_player).setOnClickListener {
            // Ya estamos en search_players, no hacemos nada
        }

        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_eye).setOnClickListener {
            if (!this::class.java.simpleName.contains("Scouts")) {
                startActivity(Intent(this, ScoutsActivity::class.java))
                finish()
            }
        }
    }
}