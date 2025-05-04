package dadm.jromsev.sportnew.ui.player

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.widget.SearchView
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.SearchPlayersBinding
import dadm.jromsev.sportnew.ui.settings.SettingsActivity
import dadm.jromsev.sportnew.ui.adapter.PlayerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import dadm.jromsev.sportnew.ui.event.SearchResultsActivity

@AndroidEntryPoint
class SearchPlayersActivity : AppCompatActivity() {
    private lateinit var binding: SearchPlayersBinding
    private val playerViewModel: PlayerViewModel by viewModels()
    private var isSearchActive = false

    private lateinit var sportsDisplay: Array<String>
    private lateinit var sportsValues: Array<String>
    private var selectedSportIndex: Int = 0
    private val selectedSports = mutableSetOf<String>()
    private var lastQuery: String? = null

    private lateinit var playerAdapter: PlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = SearchPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerAdapter = PlayerAdapter(emptyList()) { player ->
            val intent = Intent(this, PlayerProfileActivity::class.java).apply {
                putExtra("player", player)  // Pasa el objeto Player como extra
            }
            startActivity(intent)
        }
        binding.rvPlayers.adapter = playerAdapter
        binding.rvPlayers.layoutManager = LinearLayoutManager(this)

        sportsDisplay = resources.getStringArray(R.array.sports_display)
        sportsValues = resources.getStringArray(R.array.sports_values)
        selectedSportIndex = 0

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

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        isSearchActive = true  // Ricerca attivata
                        lastQuery = it
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

        val sportsToSearch = if (selectedSports.isNotEmpty()) selectedSports.toList() else sportsValues.toList()
        playerViewModel.getNewPlayersAll(sportsToSearch.toString())

        // Observar lista de jugadores
        playerViewModel.players.observe(this) { playersList ->
            val sortedPlayers = playersList.sortedByDescending { it.relevance?.toIntOrNull() ?: 0 }
            playerAdapter.updatePlayers(sortedPlayers)

        }

        // Observar errores
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerViewModel.errorState.collect { error ->
                    if (isSearchActive && error != null) {
                        Toast.makeText(
                            this@SearchPlayersActivity,
                            error.message ?: getString(R.string.unknown_error),
                            Toast.LENGTH_SHORT
                        ).show()
                        isSearchActive = false
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
        }

        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_eye).setOnClickListener {
            if (!this::class.java.simpleName.contains("Scouts")) {
                startActivity(Intent(this, ScoutsActivity::class.java))
                finish()
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isSearchActive", isSearchActive)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isSearchActive = savedInstanceState.getBoolean("isSearchActive", false)
    }
}