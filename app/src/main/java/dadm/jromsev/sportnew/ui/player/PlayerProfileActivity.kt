package dadm.jromsev.sportnew.ui.player

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.PlayerProfileBinding
import dadm.jromsev.sportnew.domain.model.Player
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerProfileActivity : AppCompatActivity() {
    private lateinit var binding: PlayerProfileBinding
    private val viewModel: PlayerViewModel by viewModels()
    private lateinit var currentPlayer: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        currentPlayer = intent.getParcelableExtra("player") ?: run {
            Toast.makeText(this, getString(R.string.error_loading_player), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        mostrarInformacionJugador(currentPlayer)

        lifecycleScope.launch {
            val isSaved = viewModel.isInDatabase(currentPlayer.player)
            setEyeIcon(isSaved)
        }

        binding.btnEye.setOnClickListener {
            lifecycleScope.launch {
                val isNowSaved = viewModel.togglePlayer(currentPlayer)
                setEyeIcon(isNowSaved)
            }
        }

        binding.btnReturn.setOnClickListener {
            setResult(RESULT_OK) // Indica que ScoutsActivity debe recargarse
            finish()
        }
    }

    private fun mostrarInformacionJugador(player: Player) {
        binding.playerName.text = getString(R.string.player_name) + " " + player.player
        binding.playerTeam.text = getString(R.string.player_name_team) + " " + player.team
        binding.playerSport.text = getString(R.string.player_sport) + " " + player.sport
        binding.playerNationality.text = getString(R.string.player_nationality) + " " + player.nationality
        binding.playerDateBorn.text = getString(R.string.player_date_born) + " " + player.dateBorn
        binding.playerStatus.text = getString(R.string.player_status) + " " + (player.status ?: "N/A")
        binding.playerGender.text = getString(R.string.player_gender) + " " + player.gender
        binding.playerPosition.text = getString(R.string.player_position) + " " + player.position

        if (!player.image.isNullOrEmpty()) {
            Glide.with(this)
                .load(player.image)
                .error(R.drawable.unknown_player)
                .into(binding.imgPlayer)
        } else {
            binding.imgPlayer.setImageResource(R.drawable.unknown_player)
        }
    }

    private fun setEyeIcon(isSaved: Boolean) {
        val icon = if (isSaved) R.drawable.eye_closed else R.drawable.eye_open
        binding.btnEye.setImageResource(icon)
    }
}