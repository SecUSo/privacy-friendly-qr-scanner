package com.secuso.privacyfriendlycodescanner.qrscanner.ui.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.secuso.privacyfriendlycodescanner.qrscanner.R

/**
 * ArrayAdapter with support for one icon and string for each item.
 * @param layoutResource layout for the list item, must contain both R.id.textView and R.id.imageView
 */
class IconArrayAdapter(
    context: Context,
    private val layoutResource: Int,
    private val strings: Array<String>,
    private val icons: Array<Int>
) : ArrayAdapter<String>(
    context,
    layoutResource,
    strings
) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val item: View =
            convertView ?: inflater.inflate(layoutResource, parent, false)
        item.findViewById<TextView>(R.id.textView)?.text = strings[position]
        item.findViewById<ImageView>(R.id.imageView)?.setImageResource(icons[position])
        return item
    }
}