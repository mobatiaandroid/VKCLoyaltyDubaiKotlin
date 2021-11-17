package com.vkc.loyaltyme.manager

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.vkc.loyaltyme.R

internal class CustomEditTextBlack : AppCompatEditText {
    constructor(context: Context?) : super(context!!) {
        setFont()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
        setFont()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context!!, attrs, defStyle) {
        setFont()
    }

    private fun setFont() {
        val font =
            Typeface.createFromAsset(context.assets, "fonts/Roboto-Regular.ttf")
        setTypeface(font, Typeface.NORMAL)
        setTextColor(context.resources.getColor(R.color.white))
    }
}
