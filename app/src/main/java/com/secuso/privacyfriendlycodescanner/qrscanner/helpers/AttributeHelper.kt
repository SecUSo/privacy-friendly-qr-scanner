package com.secuso.privacyfriendlycodescanner.qrscanner.helpers

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue

class AttributeHelper {
    companion object {
        private const val TAG = "AttributeHelper"
        private fun getColorId(context: Context, attributeId: Int): Int {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(attributeId, typedValue, true)
            return typedValue.resourceId
        }

        fun getColor(context: Context, attributeId: Int, defaultColor: Int) : Int{
            val colorId = getColorId(context, attributeId)
            var color = defaultColor
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                try {
                    color = context.resources.getColor(colorId, context.theme)
                } catch (e: Resources.NotFoundException) {
                    Log.w(TAG, "Color Resource $colorId not found for attribute $attributeId.")
                }
            }
            return color
        }
    }
}