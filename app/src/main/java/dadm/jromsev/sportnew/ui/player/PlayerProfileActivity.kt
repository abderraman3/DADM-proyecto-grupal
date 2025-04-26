package dadm.jromsev.sportnew.ui.player

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.PlayerProfileBinding
import dadm.jromsev.sportnew.domain.model.Player


class PlayerProfileActivity : AppCompatActivity() {
    private lateinit var binding: PlayerProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Obtener el jugador pasado como extra
        val player = intent.getParcelableExtra<Player>("player")
        player?.let {
            mostrarInformacionJugador(it)
        } ?: run {
            Toast.makeText(this, getString(R.string.error_loading_player), Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnReturn.setOnClickListener {
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

        // Cargar imagen si está disponible
        if (!player.image.isNullOrEmpty()) {
            Glide.with(this)
                .load(player.image)
                .error(R.drawable.unknown_player) // Mostrar icono si hay error al cargar
                .into(binding.imgPlayer)
        } else {
            binding.imgPlayer.setImageResource(R.drawable.unknown_player)
        }
    }
}