package my.dir.contactlistapp

import android.net.Uri
import java.io.Serializable

data class Contact(
    val name: String,
    val email: String,
    val phone: String,
    val photoUri: Uri?
) : Serializable
