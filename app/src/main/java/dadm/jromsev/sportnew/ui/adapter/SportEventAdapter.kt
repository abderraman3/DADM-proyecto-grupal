package dadm.jromsev.sportnew.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.databinding.ItemSportEventBinding
import dadm.jromsev.sportnew.domain.model.SportEvent

class SportEventAdapter(
    private var events: List<SportEvent>,
    private val onItemClick: (SportEvent) -> Unit
) : RecyclerView.Adapter<SportEventAdapter.EventViewHolder>() {

    inner class EventViewHolder(private val binding: ItemSportEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: SportEvent) {
            binding.tvEventName.text = event.eventName
            binding.tvHomeTeam.text = event.homeTeam
            binding.tvAwayTeam.text = event.awayTeam
            binding.tvHomeScore.text = event.homeScore ?: "–"
            binding.tvAwayScore.text = event.awayScore ?: "–"

            // Cargar el logo de la liga, mostrando un icono por defecto si no hay imagen
            if (!event.leagueBadge.isNullOrEmpty()) {
                Glide.with(binding.imgEventLogo.context)
                    .load(event.leagueBadge)
                    .error(R.drawable.default_image) // Mostrar icono si hay error al cargar
                    .into(binding.imgEventLogo)
            } else {
                binding.imgEventLogo.setImageResource(R.drawable.default_image)
            }

            binding.root.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemSportEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    fun updateEvents(newEvents: List<SportEvent>) {
        events = newEvents
        notifyDataSetChanged()
    }


}