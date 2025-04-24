package my.dir.contactlistapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyTextView: TextView
    private lateinit var addButton: Button
    private lateinit var contactAdapter: ContactAdapter
    private val contacts = mutableListOf<Contact>()

    companion object {
        const val REQUEST_ADD_CONTACT = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        emptyTextView = findViewById(R.id.emptyTextView)
        addButton = findViewById(R.id.addButton)

        contactAdapter = ContactAdapter(contacts) { contactToDelete ->
            contacts.remove(contactToDelete)
            updateUI()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = contactAdapter

        addButton.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_CONTACT)
        }

        updateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_CONTACT && resultCode == Activity.RESULT_OK) {
            val bundle = data?.getBundleExtra(AddContactActivity.EXTRA_CONTACT)
            if (bundle != null) {
                val name = bundle.getString("name") ?: ""
                val email = bundle.getString("email") ?: ""
                val phone = bundle.getString("phone") ?: ""
                val photoUriString = bundle.getString("photoUri")
                val photoUri = if (photoUriString != null) Uri.parse(photoUriString) else null
                val contact = Contact(name, email, phone, photoUri)
                contacts.add(contact)
                updateUI()
            }
        }
    }

    private fun updateUI() {
        contactAdapter.notifyDataSetChanged()
        emptyTextView.visibility = if (contacts.isEmpty()) TextView.VISIBLE else TextView.GONE
    }
}
