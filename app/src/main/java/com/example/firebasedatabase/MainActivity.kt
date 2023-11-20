package com.example.firebasedatabase

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasedatabase.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import java.io.ByteArrayOutputStream
import java.lang.Exception
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.firebase.storage.FirebaseStorage



class MainActivity : AppCompatActivity() {
    var sImage: String? = ""
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun insert_data(view: View) {
        val itemName = binding.etName.text.toString()
        val itemRate = binding.etRt.text.toString()
        val itemUnit = binding.etUn.text.toString()

        // Thực hiện tải ảnh lên Firebase Storage
        if (!sImage.isNullOrEmpty()) {
            val storageReference = FirebaseStorage.getInstance().getReference("item_images")
            val imageFileName = "image_${System.currentTimeMillis()}.jpg"
            val imageRef = storageReference.child(imageFileName)
            val imageBytes = Base64.decode(sImage, Base64.DEFAULT)

            imageRef.putBytes(imageBytes)
                .addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val item = itemDs(itemName, itemRate, itemUnit, uri.toString())
                        db = FirebaseDatabase.getInstance().getReference("items")
                        val databaseReference = FirebaseDatabase.getInstance().reference
                        val id = databaseReference.push().key
                        db.child(id.toString()).setValue(item).addOnSuccessListener {
                            binding.etName.text.clear()
                            binding.etRt.text.clear()
                            sImage = ""
                            Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Data not inserted", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Xử lý khi không có ảnh được chọn
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    fun insert_Img(view: View) {
        val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
        myfileintent.setType("image/*")
        ActivityResultLauncher.launch(myfileintent)
    }

    private val ActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val uri = result.data!!.data
            try {
                val inputStream = contentResolver.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStream)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val bytes = stream.toByteArray()
                sImage = Base64.encodeToString(bytes, Base64.DEFAULT)
                binding.imageView.setImageBitmap(myBitmap)
                inputStream!!.close()
                Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show()
            } catch (ex: Exception) {
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }

    fun showList(view: View) {
        var i: Intent
        i = Intent(this, ItemList::class.java)
        startActivity(i)
    }
}