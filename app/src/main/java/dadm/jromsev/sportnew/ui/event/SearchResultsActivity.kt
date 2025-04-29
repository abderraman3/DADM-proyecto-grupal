package dadm.jromsev.sportnew.ui.event

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast
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

@AndroidEntryPoint
class SearchResultsActivity : AppCompatActivity() {
    private lateinit var binding: SearchResultsBinding
    private lateinit var eventAdapter: SportEventAdapter
    private val sportEventViewModel: SportEventViewModel by viewModels()

    private var selectedLeagueId: String = "4335" // Default La Liga
    private var selectedSeason: String = "2024-2025" // Default Season

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge behavior
        binding = SearchResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView with an empty list initially
        val recyclerView = binding.eventsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        eventAdapter = SportEventAdapter(emptyList()) { event ->
            Toast.makeText(this, "Clicked on: ${event.eventName}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = eventAdapter

        // Observe the ViewModel for events
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

        // Fetch events with the default league and season
        sportEventViewModel.getEventsBySeason(selectedLeagueId, selectedSeason)

        // Set up the filter button to show the league and season dialog
        binding.btnFilter.setOnClickListener {
            showLeagueAndSeasonFilter()
        }

        // Set up the bottom navigation
        setupBottomNavigation()

        // Configure the SearchView to handle query submissions
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

        // Apply padding to avoid overlap with system components like the camera
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


    private fun showLeagueAndSeasonFilter() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Select League and Season")

        val dialogView = layoutInflater.inflate(R.layout.league_filter, null)
        val leagueSpinner = dialogView.findViewById<Spinner>(R.id.spinnerLeague)
        val seasonSpinner = dialogView.findViewById<Spinner>(R.id.spinnerSeason)

        // Get the leagues from resources
        val leagues = resources.getStringArray(R.array.leagues)
        val leagueAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, leagues)
        leagueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        leagueSpinner.adapter = leagueAdapter

        // Set the default league and season (Premier League 2024-2025)
        var currentSeasons = resources.getStringArray(R.array.seasons_premier_league)
        val seasonAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currentSeasons)
        seasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        seasonSpinner.adapter = seasonAdapter

        leagueSpinner.setSelection(0)
        seasonSpinner.setSelection(0)

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
                seasonSpinner.setSelection(0) // Default selection
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
            sportEventViewModel.getEventsBySeason(selectedLeagueId, selectedSeason)
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun setupBottomNavigation() {
        // Set up the bottom navigation bar to handle button clicks
        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_trophy).setOnClickListener {
            // Implement action for the trophy button
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