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
import androidx.recyclerview.widget.LinearLayoutManager
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.ScoutsBinding
import dadm.jromsev.sportnew.ui.adapter.PlayerAdapter
import dadm.jromsev.sportnew.ui.settings.SettingsActivity
import dadm.jromsev.sportnew.ui.event.SearchResultsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.max

/**
 * Actividad que muestra la lista de jugadores guardados por el usuario.
 * Permite eliminar jugadores mediante deslizamiento y acceder a su perfil.
 */

@AndroidEntryPoint
class ScoutsActivity : AppCompatActivity() {
    private lateinit var binding: ScoutsBinding
    private val viewModel: PlayerViewModel by viewModels()
    private lateinit var playerAdapter: PlayerAdapter

    /**
     * Metodo que se ejecuta al iniciar la actividad.
     * Configura la interfaz, el RecyclerView y la navegación inferior.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ScoutsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerAdapter = PlayerAdapter(emptyList()) { player ->
            val intent = Intent(this, PlayerProfileActivity::class.java).apply {
                putExtra("player", player)
            }
            startActivityForResult(intent, REQUEST_CODE_PLAYER_PROFILE)
        }

        binding.rvScoutedPlayers.layoutManager = LinearLayoutManager(this)
        binding.rvScoutedPlayers.adapter = playerAdapter

        // Funcionalidad de deslizar para eliminar jugadores
        val itemTouchHelper = androidx.recyclerview.widget.ItemTouchHelper(object : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(0, androidx.recyclerview.widget.ItemTouchHelper.LEFT or androidx.recyclerview.widget.ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                target: androidx.recyclerview.widget.RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val player = playerAdapter.getPlayerAt(position)
                lifecycleScope.launch {
                    viewModel.deletePlayer(player)
                    val updatedPlayers = viewModel.getAllPlayers()
                    playerAdapter.updatePlayers(updatedPlayers)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvScoutedPlayers)

        lifecycleScope.launch {
            val players = viewModel.getAllPlayers()
            playerAdapter.updatePlayers(players)
        }

        //Padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())

            val left = max(systemBars.left, cutout.left)
            val right = max(systemBars.right, cutout.right)
            val bottom = max(systemBars.bottom, cutout.bottom)

            binding.rvScoutedPlayers.updatePadding(
                left = left,
                right = right,
                bottom = bottom
            )

            binding.toolbar.updatePadding(
                top = systemBars.top
            )

            binding.bottomNavBar.updatePadding(
                left = left,
                right = right,
                bottom = bottom
            )

            WindowInsetsCompat.CONSUMED
        }

        binding.toolbar.findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        setupBottomNavigation()
    }

    /**
     * Se llama al volver de otra actividad para actualizar la lista si hubo cambios.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            lifecycleScope.launch {
                val players = viewModel.getAllPlayers()
                playerAdapter.updatePlayers(players)
            }
        }
    }

    /**
     * Configura los botones de navegación inferior para cambiar entre actividades.
     */
    private fun setupBottomNavigation() {
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
            // Ya nos encontramos en ScoutsActivity, no se realiza ninguna acción
        }
    }

    companion object {
        private const val REQUEST_CODE_PLAYER_PROFILE = 1
    }
}