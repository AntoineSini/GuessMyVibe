package fr.isen.guessmyvibe

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import fr.isen.guessmyvibe.classes.User
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.room_item.view.*

class RecyclerAdapterRoom(val content: ArrayList<User>): RecyclerView.Adapter<RecyclerAdapterRoom.RoomViewHolder>() {
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(content[position])
    }

    override fun getItemCount(): Int {
        return content.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent,false) as View
        return RoomViewHolder(view)
    }
    class RoomViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        fun bind(content: User?){
            view.username.text = content?.username
            view.level.text = content?.level
            var storage = FirebaseStorage.getInstance()
            storage.reference.child("${content?.id}/profilePicture")
                .downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).into(view.profilePic)
            }.addOnFailureListener {
                Log.d("picasso","Error while loading image from storage")
            }
        }
    }
}