package dadm.jromsev.sportnew.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels //
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle //
import androidx.lifecycle.lifecycleScope //
import androidx.lifecycle.repeatOnLifecycle //
import dadm.jromsev.sportnew.databinding.ActivityMainBinding
import dadm.jromsev.sportnew.ui.player.PlayerViewModel //
import dagger.hilt.android.AndroidEntryPoint //
import kotlinx.coroutines.launch //

@AndroidEntryPoint //
class MainActivity : AppCompatActivity() {

    private val playerViewModel: PlayerViewModel by viewModels() //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout() )
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Observamos la lista de jugadores
        playerViewModel.players.observe(this) { playersList ->
            binding.textViewPlayers.text = if (playersList.isNotEmpty()) { //text view player añadido al textview del layout
                playersList.joinToString(", ") { it.player }
            } else {
                "No se han encontrado jugadores."
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
}