package dadm.jromsev.sportnew.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.PlayerItemBinding
import dadm.jromsev.sportnew.domain.model.Player

class PlayerAdapter(
    private var players: List<Player>,
    private val onItemClick: (Player) -> Unit
) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    inner class PlayerViewHolder(private val binding: PlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player) {
            binding.tvPlayerName.text = player.player

            // Cargar imagen o mostrar icono por defecto si no hay imagen
            if (!player.image.isNullOrEmpty()) {
                Glide.with(binding.imgPlayer.context)
                    .load(player.image)
                    .error(R.drawable.unknown_player) // Mostrar icono si hay error al cargar
                    .into(binding.imgPlayer)
            } else {
                binding.imgPlayer.setImageResource(R.drawable.unknown_player)
            }

            val context = binding.root.context
            binding.tvTeam.text = context.getString(R.string.player_name_team) + " " + player.team
            binding.tvNationality.text = context.getString(R.string.player_nationality) + " " + player.nationality
            binding.tvPosition.text = context.getString(R.string.player_position) + " " + player.position
            binding.tvBirthDate.text = context.getString(R.string.player_date_born) + " " + player.dateBorn

            binding.root.setOnClickListener {
                onItemClick(player)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = PlayerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(players[position])
    }

    override fun getItemCount(): Int = players.size

    fun updatePlayers(newPlayers: List<Player>) {
        players = newPlayers
        notifyDataSetChanged()
    }

    fun getPlayerAt(position: Int): Player {
        return players[position]
    }
}