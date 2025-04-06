package dadm.jromsev.sportnew.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.SearchPlayersBinding

class SearchPlayersActivity : AppCompatActivity() {
    private lateinit var binding: SearchPlayersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar botón de settings
        binding.toolbar.findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Configurar botón de filtro
        binding.btnFilter.setOnClickListener {
            showSportsFilterDialog()
        }

        setupBottomNavigation()
    }

    private fun showSportsFilterDialog() {
        val popupMenu = PopupMenu(this, binding.btnFilter)

        // Inflar el menú con los deportes
        val sportsDisplay = resources.getStringArray(R.array.sports_display)
        sportsDisplay.forEachIndexed { index, sport ->
            popupMenu.menu.add(0, index, 0, sport)
        }

        // Manejar la selección
        popupMenu.setOnMenuItemClickListener { item ->
            val selectedSport = resources.getStringArray(R.array.sports_values)[item.itemId]
            // Realizar acción con el deporte seleccionado
            true
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