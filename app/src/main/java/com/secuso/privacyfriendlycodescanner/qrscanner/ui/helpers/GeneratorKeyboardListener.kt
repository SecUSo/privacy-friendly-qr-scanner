package com.secuso.privacyfriendlycodescanner.qrscanner.ui.helpers

import android.os.Build
import android.transition.TransitionManager
import android.util.DisplayMetrics
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlin.math.roundToInt

/**
 * This is an OnGlobalLayoutListener for generator activities. It detects if the on screen keyboard
 * is open or closed based on layout changes and updates the layout accordingly. If the keyboard is
 * open the generate button gets moved to the right and shrinks. If the keyboard is hidden the
 * generate button gets centered and expands.
 */
class GeneratorKeyboardListener(
    private val rootView: ConstraintLayout,
    private val generateButton: ExtendedFloatingActionButton,
    private val generateButtonResourceId: Int,
    private val displayDensityDpi: Int
) : ViewTreeObserver.OnGlobalLayoutListener {

    override fun onGlobalLayout() {
        val heightChange = rootView.rootView.height - rootView.height
        if (heightChange > convertDpToPixel(200, displayDensityDpi)) {
            //keyboard is open
            if (generateButton.isExtended) {
                val buttonRightSet = ConstraintSet()
                buttonRightSet.clone(rootView)
                buttonRightSet.clear(generateButtonResourceId, ConstraintSet.START)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(rootView)
                }
                buttonRightSet.applyTo(rootView)
                generateButton.shrink()
            }
        } else {
            //keyboard is hidden
            if (!generateButton.isExtended) {
                val buttonBaseSet = ConstraintSet()
                buttonBaseSet.clone(rootView)
                buttonBaseSet.connect(
                    generateButtonResourceId,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START,
                    16
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(rootView)
                }
                buttonBaseSet.applyTo(rootView)
                generateButton.extend()
            }
        }
    }

    private fun convertDpToPixel(dp: Int, displayDensityDpi: Int): Int {
        return (dp.toFloat() * (displayDensityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }
}