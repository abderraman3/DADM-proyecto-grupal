package dadm.jromsev.sportnew.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dadm.jromsev.sportnew.databinding.PlayerProfileBinding
import dadm.jromsev.sportnew.ui.domain.model.Player

class PlayerProfileActivity : AppCompatActivity() {
    private lateinit var binding: PlayerProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val player = intent.getParcelableExtra<Player>("player")
        player?.let {
            binding.playerName.text = it.name
            binding.playerTeam.text = it.team
            binding.playerSport.text = it.sport
            binding.playerNationality.text = it.nationality
            binding.playerBirthDate.text = it.birthDate
            binding.playerPosition.text = it.position

            // Aquí deberías cargar la imagen usando una librería como Glide o Picasso
            // Glide.with(this).load(it.thumb).into(binding.playerImage)
        }
    }
}