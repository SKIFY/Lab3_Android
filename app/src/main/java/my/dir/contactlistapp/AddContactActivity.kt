package my.dir.contactlistapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream

class AddContactActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var takePhotoButton: Button
    private lateinit var photoImageView: ImageView
    private lateinit var addContactButton: Button

    private var photoUri: Uri? = null

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val EXTRA_CONTACT = "extra_contact"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        takePhotoButton = findViewById(R.id.takePhotoButton)
        photoImageView = findViewById(R.id.photoImageView)
        addContactButton = findViewById(R.id.addContactButton)

        takePhotoButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }

        addContactButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()

            if (name.isNotBlank() && email.isNotBlank() && phone.isNotBlank()) {
                val contact = Contact(name, email, phone, photoUri)
                val resultIntent = Intent().apply {
                    putExtra(EXTRA_CONTACT, contactToBundle(contact))
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Заповніть усі поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            photoImageView.setImageBitmap(bitmap)

            // Сохраняем изображение во временное URI
            val uri = getImageUriFromBitmap(bitmap)
            photoUri = uri
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            contentResolver, bitmap, "ContactPhoto", null
        )
        return Uri.parse(path)
    }

    private fun contactToBundle(contact: Contact): Bundle {
        return Bundle().apply {
            putString("name", contact.name)
            putString("email", contact.email)
            putString("phone", contact.phone)
            putString("photoUri", contact.photoUri?.toString())
        }
    }
}
