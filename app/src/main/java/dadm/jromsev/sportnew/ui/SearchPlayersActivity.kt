package dadm.jromsev.sportnew.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dadm.jromsev.sportnew.databinding.SearchPlayersBinding

class SearchPlayersActivity : AppCompatActivity() {
    private lateinit var binding: SearchPlayersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavBar.btnTrophy.setOnClickListener {
            if (!this::class.java.simpleName.contains("SearchResults")) {
                startActivity(Intent(this, SearchResultsActivity::class.java))
                finish()
            }
        }

        binding.bottomNavBar.btnPlayer.setOnClickListener {
            // Ya estamos en search_players, no hacemos nada
        }

        binding.bottomNavBar.btnEye.setOnClickListener {
            if (!this::class.java.simpleName.contains("Scouts")) {
                startActivity(Intent(this, ScoutsActivity::class.java))
                finish()
            }
        }
    }
}