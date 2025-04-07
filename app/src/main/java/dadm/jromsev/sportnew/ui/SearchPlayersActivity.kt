package dadm.jromsev.sportnew.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.data.player.PlayerDataSource
import dadm.jromsev.sportnew.data.player.model.RemotePlayerDto
import dadm.jromsev.sportnew.databinding.SearchPlayersBinding
import dadm.jromsev.sportnew.ui.domain.model.Player
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchPlayersActivity : AppCompatActivity() {

    private lateinit var binding: SearchPlayersBinding
    private var selectedSport: String? = null
    private lateinit var adapter: ArrayAdapter<String>
    private val playerNames = mutableListOf<String>()
    private val playersMap = mutableMapOf<String, Player>()

    @Inject
    lateinit var playerDataSource: PlayerDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupSearchView()
        setupListView()
        setupToolbar()
        setupBottomNavigation()
    }

    private fun setupAdapter() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, playerNames)
        binding.listViewPlayers.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchPlayers(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }

    private fun setupListView() {
        binding.listViewPlayers.setOnItemClickListener { _, _, position, _ ->
            playersMap[playerNames[position]]?.let { player ->
                openPlayerProfile(player)
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnFilter.setOnClickListener {
            showSportsFilterDialog()
        }
    }

    private fun searchPlayers(query: String) {
        lifecycleScope.launch {
            try {
                showLoading()
                val response = playerDataSource.getPlayer(query, selectedSport ?: "")

                if (response.isSuccessful) {
                    response.body()?.let { updatePlayersList(it) } ?: showError("No data received")
                } else {
                    showError("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                showError("Connection error: ${e.message}")
            } finally {
                hideLoading()
            }
        }
    }

    private fun updatePlayersList(remotePlayerDto: RemotePlayerDto) {
        playerNames.clear()
        playersMap.clear()

        remotePlayerDto.player?.let { players ->
            for (player in players) {
                player.strPlayer?.let { name ->
                    playerNames.add(name)
                    playersMap[name] = Player(
                        id = player.idPlayer ?: "",
                        name = name,
                        team = player.strTeam ?: "",
                        sport = player.strSport ?: "",
                        thumb = player.strThumb,
                        nationality = player.strNationality,
                        birthDate = player.dateBorn,
                        position = player.strPosition
                    )
                }
            }
        }

        if (playerNames.isEmpty()) showError("No players found")
        adapter.notifyDataSetChanged()
    }

    private fun openPlayerProfile(player: Player) {
        Intent(this, PlayerProfileActivity::class.java).apply {
            putExtra("player", player)
            startActivity(this)
        }
    }

    private fun showSportsFilterDialog() {
        PopupMenu(this, binding.btnFilter).apply {
            val sports = resources.getStringArray(R.array.sports_display)
            sports.forEachIndexed { index, sport ->
                menu.add(0, index, 0, sport)
            }

            setOnMenuItemClickListener { item ->
                selectedSport = resources.getStringArray(R.array.sports_values).getOrNull(item.itemId)
                val query = binding.searchView.query?.toString() ?: ""
                if (query.isNotEmpty()) {
                    searchPlayers(query)
                }
                true
            }
            show()
        }
    }

    private fun setupBottomNavigation() {
        with(binding.bottomNavBar) {
            findViewById<ImageButton>(R.id.btn_trophy).setOnClickListener {
                if (!this@SearchPlayersActivity::class.java.simpleName.contains("SearchResults")) {
                    startActivity(Intent(this@SearchPlayersActivity, SearchResultsActivity::class.java))
                    finish()
                }
            }

            findViewById<ImageButton>(R.id.btn_player).setOnClickListener {
                // Already in search players
            }

            findViewById<ImageButton>(R.id.btn_eye).setOnClickListener {
                if (!this@SearchPlayersActivity::class.java.simpleName.contains("Scouts")) {
                    startActivity(Intent(this@SearchPlayersActivity, ScoutsActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showError(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}