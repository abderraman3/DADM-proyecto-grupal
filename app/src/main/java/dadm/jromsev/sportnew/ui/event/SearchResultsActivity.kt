package dadm.jromsev.sportnew.ui.event

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.TextView
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.SearchResultsBinding
import dadm.jromsev.sportnew.domain.model.SportEvent
import dadm.jromsev.sportnew.ui.adapter.SportEventAdapter
import dadm.jromsev.sportnew.ui.player.ScoutsActivity
import dadm.jromsev.sportnew.ui.settings.SettingsActivity
import dadm.jromsev.sportnew.ui.player.SearchPlayersActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Actividad encargada de mostrar los resultados de búsqueda de eventos deportivos.
@AndroidEntryPoint
class SearchResultsActivity : AppCompatActivity() {
    private lateinit var binding: SearchResultsBinding
    private lateinit var eventAdapter: SportEventAdapter
    private val sportEventViewModel: SportEventViewModel by viewModels()

    private var selectedLeagueId: String = "4335" // Default La Liga
    private var selectedSeason: String = "2024-2025" // Default Season
    private var selectedRoundIndex: Int = 0

    // Metodo principal donde se inicializan los componentes de la interfaz y se configuran los observadores.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge behavior
        binding = SearchResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa el RecyclerView con una lista vacía inicialmente
        val recyclerView = binding.eventsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        eventAdapter = SportEventAdapter(emptyList()) { event ->
        }
        recyclerView.adapter = eventAdapter

        // Observa el ViewModel para obtener los eventos
        sportEventViewModel.events.observe(this) { events ->
            eventAdapter.updateEvents(events)
        }

        lifecycleScope.launch {
            sportEventViewModel.errorState.collect { error ->
                error?.let {
                    Toast.makeText(this@SearchResultsActivity, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Obtiene eventos usando la liga y temporada por defecto
        sportEventViewModel.getEventsBySeason(selectedLeagueId, selectedSeason)

        // Configura el botón de filtro para mostrar el diálogo de liga y temporada
        binding.btnFilter.setOnClickListener {
            showLeagueAndSeasonFilter()
        }

        // Configura la barra de navegación inferior
        setupBottomNavigation()

        // Configura el SearchView para manejar las búsquedas
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        sportEventViewModel.searchEventsByQuery(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        // Aplica relleno para evitar superposición con componentes del sistema como la cámara
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
        binding.toolbar.findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }


    // Muestra un diálogo para seleccionar la liga y la temporada, y actualiza los eventos según la selección.
    private fun showLeagueAndSeasonFilter() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Select League and Season")

        val dialogView = layoutInflater.inflate(R.layout.league_filter, null)
        val leagueSpinner = dialogView.findViewById<Spinner>(R.id.spinnerLeague)
        val seasonSpinner = dialogView.findViewById<Spinner>(R.id.spinnerSeason)

        val roundSpinner = dialogView.findViewById<Spinner>(R.id.spinnerRound)
        val roundLabel = dialogView.findViewById<TextView>(R.id.labelRound)

        val leagues = resources.getStringArray(R.array.leagues)
        val leagueAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, leagues)
        leagueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        leagueSpinner.adapter = leagueAdapter

        var currentSeasons = resources.getStringArray(R.array.seasons_premier_league)
        val seasonAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currentSeasons)
        seasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        seasonSpinner.adapter = seasonAdapter


        val selectedLeagueIndex = leagues.indexOfFirst {
            when (selectedLeagueId) {
                "4391" -> it == "NFL"
                "4387" -> it == "NBA"
                "4328" -> it == "Premier League"
                "4332" -> it == "Serie A"
                "4335" -> it == "La Liga"
                "4380" -> it == "NHL"
                else -> false
            }
        }.takeIf { it >= 0 } ?: 0
        leagueSpinner.setSelection(selectedLeagueIndex)

        val selectedSeasonIndex = currentSeasons.indexOf(selectedSeason).takeIf { it >= 0 } ?: 0
        seasonSpinner.setSelection(selectedSeasonIndex)

        leagueSpinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val selectedLeague = leagues[position]
                val seasonArrayId = when (selectedLeague) {
                    "NFL" -> R.array.seasons_nfl
                    "NBA" -> R.array.seasons_nba
                    "Premier League" -> R.array.seasons_premier_league
                    "Serie A" -> R.array.seasons_serie_a
                    "La Liga" -> R.array.seasons_la_liga
                    "NHL" -> R.array.seasons_nhl
                    else -> R.array.seasons_premier_league
                }
                currentSeasons = resources.getStringArray(seasonArrayId)

                val newSeasonAdapter = ArrayAdapter(this@SearchResultsActivity, android.R.layout.simple_spinner_item, currentSeasons)
                newSeasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                seasonSpinner.adapter = newSeasonAdapter
                val selectedSeasonIndex = currentSeasons.indexOf(selectedSeason).takeIf { it >= 0 } ?: 0
                seasonSpinner.setSelection(selectedSeasonIndex)

                val supportsRound = selectedLeague in listOf("La Liga", "Premier League", "Serie A")
                if (supportsRound) {
                    roundLabel.visibility = android.view.View.VISIBLE
                    roundSpinner.visibility = android.view.View.VISIBLE

                    val rounds = (1..38).map { "Round $it" }
                    val roundAdapter = ArrayAdapter(this@SearchResultsActivity, android.R.layout.simple_spinner_item, rounds)
                    roundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    roundSpinner.adapter = roundAdapter
                    roundSpinner.setSelection(selectedRoundIndex)
                } else {
                    roundLabel.visibility = android.view.View.GONE
                    roundSpinner.visibility = android.view.View.GONE
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        })

        builder.setView(dialogView)
        builder.setPositiveButton("Apply") { _, _ ->
            val selectedLeague = leagueSpinner.selectedItem as String
            val selectedSeasonValue = seasonSpinner.selectedItem as String
            selectedLeagueId = when (selectedLeague) {
                "NFL" -> "4391"
                "NBA" -> "4387"
                "Premier League" -> "4328"
                "Serie A" -> "4332"
                "La Liga" -> "4335"
                "NHL" -> "4380"
                else -> "4335" // Default La Liga
            }
            selectedSeason = selectedSeasonValue
            val supportsRound = selectedLeague in listOf("La Liga", "Premier League", "Serie A")
            if (supportsRound) {
                val roundSelected = roundSpinner.selectedItem as String
                selectedRoundIndex = roundSpinner.selectedItemPosition
                val roundNumber = roundSelected.removePrefix("Round ").trim()
                sportEventViewModel.getEventsByRound(selectedLeagueId, selectedSeason, roundNumber)
            } else {
                sportEventViewModel.getEventsBySeason(selectedLeagueId, selectedSeason)
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    // Configura los botones de navegación inferior para cambiar entre las actividades principales de la app.
    private fun setupBottomNavigation() {
        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_trophy).setOnClickListener {
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