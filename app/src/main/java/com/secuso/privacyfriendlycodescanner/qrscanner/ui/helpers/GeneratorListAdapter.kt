package com.secuso.privacyfriendlycodescanner.qrscanner.ui.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.secuso.privacyfriendlycodescanner.qrscanner.R


class GeneratorListAdapter(
    private val context: Context,
    private val titles: Array<String>,
    private val icons: Array<Int>
) : BaseAdapter() {

    override fun getCount(): Int {
        return titles.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val item: View = p1 ?: inflater.inflate(R.layout.list_item_generator, p2, false)
        item.findViewById<TextView>(R.id.textView)?.text = titles[p0]
        item.findViewById<ImageView>(R.id.imageView)?.setImageResource(icons[p0])
        return item
    }

}