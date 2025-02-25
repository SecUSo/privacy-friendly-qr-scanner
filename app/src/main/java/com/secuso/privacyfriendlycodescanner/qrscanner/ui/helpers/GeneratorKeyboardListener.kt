/*
    Privacy Friendly QR Scanner
    Copyright (C) 2022-2025 Privacy Friendly QR Scanner authors and SECUSO

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

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