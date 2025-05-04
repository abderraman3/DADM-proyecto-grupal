package dadm.jromsev.sportnew.ui.player

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.PlayerProfileBinding
import dadm.jromsev.sportnew.domain.model.Player
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
/**
 * Actividad que muestra la información detallada de un jugador seleccionado.
 * Permite guardar o eliminar al jugador de favoritos y regresar a la pantalla anterior.
 */
class PlayerProfileActivity : AppCompatActivity() {
    private lateinit var binding: PlayerProfileBinding
    private val viewModel: PlayerViewModel by viewModels()
    private lateinit var currentPlayer: Player

    /**
     * Metodo principal que se ejecuta al crear la actividad.
     * Carga la vista, obtiene el jugador desde el intent y configura los botones.
     */
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

        showPlayerInformations(currentPlayer)

        lifecycleScope.launch {
            val isSaved = viewModel.isInDatabase(currentPlayer.player)
            setEyeIcon(isSaved)
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
        binding.btnEye.setOnClickListener {
            lifecycleScope.launch {
                val isNowSaved = viewModel.togglePlayer(currentPlayer)
                setEyeIcon(isNowSaved)
            }
        }

        binding.btnReturn.setOnClickListener() {
            setResult(RESULT_OK) // Señala que se debe recargar la actividad anterior (ScoutsActivity)
            finish()
        }
    }

    /**
     * Muestra los detalles del jugador en los elementos de la interfaz.
     * Si hay una imagen disponible, la carga con Glide; si no, usa una imagen por defecto.
     */
    private fun showPlayerInformations(player: Player) {
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

    /**
     * Cambia el icono del botón de guardar según si el jugador está en favoritos o no.
     */
    private fun setEyeIcon(isSaved: Boolean) {
        val icon = if (isSaved) R.drawable.eye_closed else R.drawable.eye_open
        binding.btnEye.setImageResource(icon)
    }
}