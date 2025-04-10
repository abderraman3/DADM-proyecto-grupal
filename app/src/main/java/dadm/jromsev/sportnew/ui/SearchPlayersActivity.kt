package dadm.jromsev.sportnew.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import dadm.jromsev.sportnew.ui.player.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchPlayersActivity : AppCompatActivity() {
    private lateinit var binding: SearchPlayersBinding
    private val playerViewModel: PlayerViewModel by viewModels()

    private lateinit var sportsDisplay: Array<String>
    private lateinit var sportsValues: Array<String>
    private var selectedSportIndex: Int = 0 // Guardamos el índice seleccionado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar arrays de deportes
        sportsDisplay = resources.getStringArray(R.array.sports_display)
        sportsValues = resources.getStringArray(R.array.sports_values)
        selectedSportIndex = 0 // Valor por defecto

        // Configurar botón de settings
        binding.toolbar.findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Configurar botón de filtro
        binding.btnFilter.setOnClickListener { view ->
            showSportsFilterMenu(view)
        }

        setupBottomNavigation()

        // Observar lista de jugadores
        playerViewModel.players.observe(this) { playersList ->
            binding.textViewPlayers.text = if (playersList.isNotEmpty()) {
                playersList.joinToString(", ") { it.player }
            } else {
                getString(R.string.no_players_found)
            }
        }

        // Observar errores
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerViewModel.errorState.collect { error ->
                    error?.let {
                        binding.textViewPlayers.text = it.message
                    }
                }
            }
        }

        // Buscar jugadores con el deporte seleccionado
        binding.buttonSearch.setOnClickListener {
            val nombre = "Harry Kane"
            playerViewModel.getNewPlayers(nombre, sportsValues[selectedSportIndex])
        }
    }

    private fun showSportsFilterMenu(anchor: View) {
        val popupMenu = PopupMenu(this, anchor).apply {
            // Crear menú con todos los items
            sportsDisplay.forEachIndexed { index, sportName ->
                val menuItem = menu.add(Menu.NONE, index, Menu.NONE, sportName)

                // Aplicar estilo al item seleccionado
                if (index == selectedSportIndex) {
                    val color = ContextCompat.getColor(this@SearchPlayersActivity, R.color.teal_200)
                    val span = android.text.SpannableString(sportName)
                    span.setSpan(
                        android.text.style.ForegroundColorSpan(color),
                        0,
                        sportName.length,
                        android.text.Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )
                    menuItem.title = span
                }
            }

            // Establecer listener para selección
            setOnMenuItemClickListener { item: MenuItem ->
                selectedSportIndex = item.itemId
                true
            }
        }
        popupMenu.show()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_trophy).setOnClickListener {
            if (!this::class.java.simpleName.contains("SearchResults")) {
                startActivity(Intent(this, SearchResultsActivity::class.java))
                finish()
            }
        }

        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_player).setOnClickListener {
            // Ya estamos en la actividad actual
        }

        binding.bottomNavBar.findViewById<ImageButton>(R.id.btn_eye).setOnClickListener {
            if (!this::class.java.simpleName.contains("Scouts")) {
                startActivity(Intent(this, ScoutsActivity::class.java))
                finish()
            }
        }
    }
}