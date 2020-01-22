package com.example.filepicker

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import com.github.florent37.runtimepermission.kotlin.askPermission
import droidninja.filepicker.*
import droidninja.filepicker.fragments.DocFragment
import droidninja.filepicker.fragments.DocPickerFragment
import droidninja.filepicker.fragments.MediaPickerFragment
import droidninja.filepicker.fragments.PhotoPickerFragmentListener
import droidninja.filepicker.utils.FragmentUtil
import org.jetbrains.anko.toast
import java.io.File
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    private var type: Int = 0
    //TAG
    private var MAX_ATTACHMENT_COUNT = 0
    private var MEDIA_RC = 212
    //array list
    private var documentPath: ArrayList<String> = ArrayList()
    private var photoPath: ArrayList<String> = ArrayList()
    private var videoPath: ArrayList<String> = ArrayList()
    // ---------
    private var filePath: ArrayList<String> = ArrayList()

    //filepath
    private var checksumFilePath = System.getProperty("user.dir")+"/storage/emulated/0/Download/75145_VID_20191211_091549_splitted_newsroom.mp4.part_1.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //document
        btn_document.setOnClickListener {
            hasPermission(1)
        }

        //photo
        btn_photo.setOnClickListener {
            hasPermission(2)
        }

        //video
        btn_video.setOnClickListener {
            hasPermission(3)
        }

        btn_checksum.setOnClickListener{
            val file = File(checksumFilePath)
//            val md5 = MD5.calculateMD5(file)
//            println("Computing MD5 from file: $md5")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            MEDIA_RC ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    photoPath.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA))
                    videoPath.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA))
                    var uri= data.dataString
                    Log.d("MainActivity", "Uri is $uri")
                }
            FilePickerConst.REQUEST_CODE_DOC ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    documentPath.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS))
                }
        }

        addDataToView(documentPath, photoPath, videoPath)

    }

    private fun addDataToView(
        documentPath: java.util.ArrayList<String>,
        photoPath: java.util.ArrayList<String>,
        videoPath: java.util.ArrayList<String>
    ) {
        filePath.addAll(documentPath)
        filePath.addAll(photoPath)
        filePath.addAll(videoPath)
        if (rv != null) {
            val layoutManager = StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL)
            layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            rv.layoutManager = layoutManager

            val imageAdapter = ImageAdapter(this, filePath)

            rv.adapter = imageAdapter
            rv.itemAnimator = DefaultItemAnimator()
        }
        toast("Num of file(s) selected: ${filePath.size}")
    }

    private fun hasPermission(state: Int){
        askPermission(){
            getFilePicker(state)
            Log.d("PermissionResult", "Access Granted")
        }.onDeclined { e ->
            if (e.hasDenied()) {
                Log.v("TAG DENIED", "Access Denied")
                e.denied.forEach {
                    Log.v("Tag Denied for Each ", it+"\n")
                }

                AlertDialog.Builder(this@MainActivity)
                    .setMessage("Please accept our permission")
                    .setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()
                    } // ask again
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            if (e.hasForeverDenied()) {
                Log.v("TAG ForeverDenied", "Access Forever Denied")
                e.foreverDenied.forEach {
                    Log.v("TAG FOREACH FD ", it+"\n")
                }
                //go to setting manually
                e.goToSettings()
            }
        }
    }

    private fun getFilePicker(state: Int){

        val picker = FilePickerBuilder.instance.setMaxCount(10)
        val intent = Intent(this@MainActivity, FilePickerActivity::class.java)

        filePath.clear()
        documentPath.clear()
        photoPath.clear()
        videoPath.clear()

        when (state) {

            //document
            1 -> picker.enableDocSupport(true)
                .setActivityTitle("Select Document(s)")
                .pickFile(this)

            //photo
            2 -> picker.enableImagePicker(true).enableCameraSupport(true).enableVideoPicker(false)
                .setActivityTitle("Select Photo(s)")
                .pickPhoto(this, MEDIA_RC)

            //video
            3 -> picker.enableVideoPicker(true).enableCameraSupport(true).enableImagePicker(false)
                .setActivityTitle("Select Video(s)")
                .pickPhoto(this, MEDIA_RC)

        }
    }



}
