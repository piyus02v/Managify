package com.example.managify.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.managify.R
import com.example.managify.firebase.FirestoreHandler
import com.example.managify.models.User
import com.example.managify.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException

//class MyProfileActivity : BaseActivity() {
//    private var mSelectedImageFileUri : Uri? = null
//    private var mProfileImageURL : String = ""
//    private lateinit var mUserDetails : User
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my_profile)
//        setActionBar()
//
//        findViewById<CircleImageView>(R.id.my_profile_user_image).setOnClickListener {
//            if(ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//                Constants.showImageChooser(this)
//            }else{
//                ActivityCompat.requestPermissions(this,
//                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                    Constants.READ_STORAGE_PERMISSION_CODE)
//            }
//        }
//
//        FirestoreHandler().loadUserData(this)
//
//        findViewById<Button>(R.id.my_profile_update_btn).setOnClickListener {
//            if(mSelectedImageFileUri != null){
//                uploadUserImage()
//            }else{
//                showProgressDialog()
//                updateUserProfileData()
//            }
//        }
//    }
//    private fun setActionBar(){
//        setSupportActionBar(findViewById(R.id.toolbar_my_profile_activity))
//        val actionBar = supportActionBar
//        if(actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white)
//            actionBar.title = "My Profile"
//        }
//        findViewById<Toolbar>(R.id.toolbar_my_profile_activity).setNavigationOnClickListener{onBackPressed()}
//    }
//
//    fun setUserDataInUI(user : User){
//        mUserDetails = user
//        //set user image
//        Glide.with(this)
//            .load(user.image)
//            .fitCenter()
//            .placeholder(R.drawable.ic_user_place_holder)
//            .into(findViewById(R.id.my_profile_user_image))
//        //set user details
//        findViewById<AppCompatEditText>(R.id.my_profile_name_et).setText(user.name)
//        findViewById<AppCompatEditText>(R.id.my_profile_email_et).setText(user.email)
//        if(user.mobile != 0L){
//            findViewById<AppCompatEditText>(R.id.my_profile_mobile_et).setText(user.mobile.toString())
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
//            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Constants.showImageChooser(this)
//            }
//        }else{
//            Toast.makeText(this, "You just denied permission for storage." +
//                    "You can allow it from the settings.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        //check if the user select a photo from gallery
//        if(resultCode == Activity.RESULT_OK
//            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
//            && data!!.data != null){
//            FirestoreHandler().loadUserData(this)
//            }
//        else{
//            Log.e("cancelled","cancelled")
//        }
//    }
//
//    private fun uploadUserImage() {
//        showProgressDialog()
//
//        if (mSelectedImageFileUri != null) {
//            val sRef: StorageReference = FirebaseStorage.getInstance()
//                .reference
//                .child(
//                    "USER_IMAGE" + System.currentTimeMillis()
//                            + "." + Constants.getFileExtension(this, mSelectedImageFileUri)
//                )
//
//            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener { taskSnapshot ->
//                Log.e(
//                    "Firebase Image URL",
//                    taskSnapshot.metadata!!.reference!!.downloadUrl!!.toString()
//                )
//
//                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
//                    Log.e("Downloadable Image URL", uri.toString())
//                    mProfileImageURL = uri.toString()
//
//                    updateUserProfileData()               }
//                }.addOnFailureListener { exception ->
//                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
//                    hideProgressDialog()
//                }
//            }
//        }
//
//
//        private fun updateUserProfileData(){
//            val userHashMap = HashMap<String, Any>()
//            var anyChangesMade : Boolean = false
//
//            if(mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image){
//                userHashMap[Constants.IMAGE] = mProfileImageURL
//                anyChangesMade = true
//            }
//
//            if(findViewById<AppCompatEditText>(R.id.my_profile_name_et).text.toString() != mUserDetails.name){
//                userHashMap[Constants.NAME] = findViewById<AppCompatEditText>(R.id.my_profile_name_et).text.toString()
//                anyChangesMade = true
//            }
//
//            if(findViewById<AppCompatEditText>(R.id.my_profile_mobile_et).text.toString() != mUserDetails.mobile.toString()){
//                userHashMap[Constants.MOBILE] = findViewById<AppCompatEditText>(R.id.my_profile_mobile_et).text.toString().toLong()
//                anyChangesMade = true
//            }
//
//            if(anyChangesMade)
//                FirestoreHandler().updateUserProfileData(this,userHashMap)
//            else{
//                Toast.makeText(this,"Nothing has been updated!",Toast.LENGTH_SHORT).show()
//                hideProgressDialog()
//            }
//        }
//    fun profileUpdateSuccess(){
//        hideProgressDialog()
//        setResult(Activity.RESULT_OK)
//        //finish()
//    }
//
//}




class MyProfileActivity : BaseActivity() {

    private var mSelectedImageFileUri : Uri? = null
    private var mProfileImageURL : String = ""
    private lateinit var mUserDetails : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        setActionBar()

        findViewById<CircleImageView>(R.id.my_profile_user_image).setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE)
            }
        }
        FirestoreHandler().loadUserData(this)

        findViewById<Button>(R.id.my_profile_update_btn).setOnClickListener {
            if(mSelectedImageFileUri != null){
                uploadUserImage()
            }else{
                showProgressDialog()
                updateUserProfileData()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }
        }else{
            Toast.makeText(this, "You just denied permission for storage." +
                    "You can allow it from the settings.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //check if the user select a photo from gallery
        if(resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null){
            mSelectedImageFileUri = data.data
            // set the new photo to profile image
            try{
                Glide.with(this)
                    .load(mSelectedImageFileUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(findViewById<CircleImageView>(R.id.my_profile_user_image))
            }catch (e : IOException){
                e.printStackTrace()
            }
        }
    }



    private fun setActionBar(){
        setSupportActionBar(findViewById(R.id.toolbar_my_profile_activity))
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white)
            actionBar.title = "My Profile"
        }
        findViewById<Toolbar>(R.id.toolbar_my_profile_activity).setNavigationOnClickListener{onBackPressed()}
    }

    fun setUserDataInUI(user : User){
        mUserDetails = user
        //set user image
        Glide.with(this)
            .load(user.image)
            .fitCenter()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(findViewById<CircleImageView>(R.id.my_profile_user_image))
        //set user details
        findViewById<AppCompatEditText>(R.id.my_profile_name_et).setText(user.name)
        findViewById<AppCompatEditText>(R.id.my_profile_email_et).setText(user.email)
        if(user.mobile != 0L){
            findViewById<AppCompatEditText>(R.id.my_profile_mobile_et).setText(user.mobile.toString())
        }
    }

    // store user image to firebase storage
    private fun uploadUserImage(){
        showProgressDialog()

        if(mSelectedImageFileUri != null){
            val sRef : StorageReference = FirebaseStorage.getInstance()
                .reference
                .child("USER_IMAGE" + System.currentTimeMillis()
                        + "." + Constants.getFileExtension(this,mSelectedImageFileUri))

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                    taskSnapshot ->
                Log.e("Firebase Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl!!.toString())

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    mProfileImageURL = uri.toString()

                    updateUserProfileData()               }
            }.addOnFailureListener{
                    exception ->
                Toast.makeText(this,exception.message,Toast.LENGTH_SHORT).show()
                hideProgressDialog()
            }
        }
    }

    private fun updateUserProfileData(){
        val userHashMap = HashMap<String, Any>()
        var anyChangesMade : Boolean = false

        if(mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image){
                userHashMap[Constants.IMAGE] = mProfileImageURL
                anyChangesMade = true
            }

            if(findViewById<AppCompatEditText>(R.id.my_profile_name_et).text.toString() != mUserDetails.name){
                userHashMap[Constants.NAME] = findViewById<AppCompatEditText>(R.id.my_profile_name_et).text.toString()
                anyChangesMade = true
            }

            if(findViewById<AppCompatEditText>(R.id.my_profile_mobile_et).text.toString() != mUserDetails.mobile.toString()){
                userHashMap[Constants.MOBILE] = findViewById<AppCompatEditText>(R.id.my_profile_mobile_et).text.toString().toLong()
                anyChangesMade = true
            }

            if(anyChangesMade)
                FirestoreHandler().updateUserProfileData(this,userHashMap)
        else{
            Toast.makeText(this,"Nothing as been updated!",Toast.LENGTH_SHORT).show()
            hideProgressDialog()
        }
    }

    fun profileUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)

    }


}