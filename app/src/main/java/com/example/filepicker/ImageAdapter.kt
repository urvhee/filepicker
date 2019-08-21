package com.example.filepicker

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.util.ArrayList

/**
 * Created by droidNinja on 29/07/16.
 */
class ImageAdapter(private val context: Context, private val paths: ArrayList<String>) :
    RecyclerView.Adapter<ImageAdapter.FileViewHolder>() {
    private var imageSize: Int = 0

    init {
        setColumnNumber(context, 3)
    }

    private fun setColumnNumber(context: Context, columnNum: Int) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        val widthPixels = metrics.widthPixels
        imageSize = widthPixels / columnNum
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)

        return FileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val path = paths[position]
        Glide.with(context)
            .load(File(path))
            .apply(
                RequestOptions.centerCropTransform()
                    .dontAnimate()
                    .override(imageSize, imageSize)
                    .placeholder(droidninja.filepicker.R.drawable.image_placeholder)
            )
            .thumbnail(0.5f)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return paths.size
    }

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var imageView: AppCompatImageView = itemView.findViewById(R.id.iv_photo)

    }
}
