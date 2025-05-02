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

@AndroidEntryPoint
class ScoutsActivity : AppCompatActivity() {
    private lateinit var binding: ScoutsBinding
    private val viewModel: PlayerViewModel by viewModels()
    private lateinit var playerAdapter: PlayerAdapter

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

        lifecycleScope.launch {
            val players = viewModel.getAllPlayers()
            playerAdapter.updatePlayers(players)
        }

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

        setupBottomNavigation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            lifecycleScope.launch {
                val players = viewModel.getAllPlayers()
                playerAdapter.updatePlayers(players)
            }
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
            if (!this::class.java.simpleName.contains("SearchPlayers")) {
                startActivity(Intent(this, SearchPlayersActivity::class.java))
                finish()
            }
        }

        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_eye).setOnClickListener {
            // Ya estamos en scouts, no hacemos nada
        }
    }

    companion object {
        private const val REQUEST_CODE_PLAYER_PROFILE = 1
    }
}