package my.dir.contactlistapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    private val contacts: MutableList<Contact>,
    private val onDeleteClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactImage: ImageView = itemView.findViewById(R.id.contactImage)
        val contactName: TextView = itemView.findViewById(R.id.contactName)
        val contactEmail: TextView = itemView.findViewById(R.id.contactEmail)
        val contactPhone: TextView = itemView.findViewById(R.id.contactPhone)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.contactName.text = contact.name
        holder.contactEmail.text = contact.email
        holder.contactPhone.text = contact.phone
        if (contact.photoUri != null) {
            holder.contactImage.setImageURI(contact.photoUri)
        } else {
            holder.contactImage.setImageResource(R.drawable.default_photo) // створимо цей ресурс
        }
        holder.deleteButton.setOnClickListener {
            onDeleteClick(contact)
        }
    }

    override fun getItemCount(): Int = contacts.size
}
