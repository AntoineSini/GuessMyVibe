package fr.isen.guessmyvibe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.guessmyvibe.classes.Game
import kotlinx.android.synthetic.main.profile_recycler_item.view.*

class RecyclerAdapterProfile(val content: ArrayList<Game>): RecyclerView.Adapter<RecyclerAdapterProfile.ProfileViewHolder>() {
    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(content[position])
    }

    override fun getItemCount(): Int {
        return content.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_recycler_item, parent,false) as View
        return ProfileViewHolder(view)
    }
    class ProfileViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        fun bind(content: Game?){
            view.winner.text = content?.id_winner
            view.difficulty.text = content?.difficulty
        }
    }
}