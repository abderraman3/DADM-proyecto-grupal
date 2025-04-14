package dadm.jromsev.sportnew.ui

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.widget.SearchView
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.SearchPlayersBinding
import dadm.jromsev.sportnew.databinding.PlayerProfileBinding
import dadm.jromsev.sportnew.ui.player.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchPlayersActivity : AppCompatActivity() {
    private lateinit var binding: SearchPlayersBinding
    private var usingOtherLayout = false
    private val playerViewModel: PlayerViewModel by viewModels()

    private lateinit var sportsDisplay: Array<String>
    private lateinit var sportsValues: Array<String>
    private var selectedSportIndex: Int = 0
    private val selectedSports = mutableSetOf<String>()
    private var lastQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sportsDisplay = resources.getStringArray(R.array.sports_display)
        sportsValues = resources.getStringArray(R.array.sports_values)
        selectedSportIndex = 0

        // Configurar botón de settings
        binding.toolbar.findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.textViewPlayers.setOnClickListener {
            if (!usingOtherLayout) {
                val nuevoBinding = PlayerProfileBinding.inflate(layoutInflater)
                setContentView(nuevoBinding.root)
                usingOtherLayout = true
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        lastQuery = it // Guardar la última búsqueda
                        val sportsToSearch = if (selectedSports.isNotEmpty()) selectedSports.toList() else sportsValues.toList()
                        playerViewModel.getNewPlayersMultiple(it, sportsToSearch)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        // Configurar botón de filtro
        binding.btnFilter.setOnClickListener { view ->
            showSportsFilterMenu(view)
        }

        setupBottomNavigation()

        // Observar lista de jugadores
        playerViewModel.players.observe(this) { playersList ->
            binding.textViewPlayers.text = if (playersList.isNotEmpty()) {
                playersList.joinToString("\n") { it.player }
            } else {
                getString(R.string.no_players_found)
            }
        }

        // Observar errores
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerViewModel.errorState.collect { error ->
                    error?.let {
                        binding.textViewPlayers.text = it.message ?: getString(R.string.unknown_error)
                    }
                }
            }
        }
    }

    private fun showSportsFilterMenu(anchor: View) {
        val currentSelected = BooleanArray(sportsValues.size) { index ->
            selectedSports.contains(sportsValues[index])
        }

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.select_sports))
            .setMultiChoiceItems(sportsDisplay, currentSelected) { _, which, isChecked ->
                val sport = sportsValues[which]
                if (isChecked) {
                    selectedSports.add(sport)
                } else {
                    selectedSports.remove(sport)
                }
            }
            .setPositiveButton(getString(android.R.string.ok)) { dialog, _ ->
                // Relanzar búsqueda si hay una consulta previa
                lastQuery?.let { query ->
                    val sportsToSearch = if (selectedSports.isNotEmpty()) selectedSports.toList() else sportsValues.toList()
                    playerViewModel.getNewPlayersMultiple(query, sportsToSearch)
                }
                dialog.dismiss()
            }
            .setNegativeButton(getString(android.R.string.cancel), null)
            .show()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_trophy).setOnClickListener {
            if (!this::class.java.simpleName.contains("SearchResults")) {
                startActivity(Intent(this, SearchResultsActivity::class.java))
                finish()
            }
        }

        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_player).setOnClickListener {
            // Actividad actual, no hacer nada
        }

        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_eye).setOnClickListener {
            if (!this::class.java.simpleName.contains("Scouts")) {
                startActivity(Intent(this, ScoutsActivity::class.java))
                finish()
            }
        }
    }
}