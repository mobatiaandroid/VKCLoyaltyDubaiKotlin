package com.vkc.loyaltyme.manager

import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.vkc.loyaltyme.R
import java.io.Serializable

/**
 * Created by gayatri on 18/4/17.
 */
class HeaderManager : Serializable {
    /**
     * Gets the context.
     *
     * @return the context
     */
    /**
     * Sets the context.
     *
     * @param context the new context
     */
    /**
     * The context.
     */
    var context: AppCompatActivity? = null

    /**
     * The inflator.
     */
    private var inflator: LayoutInflater

    /**
     * The header view.
     */
    var headerView: View? = null

    /**
     * The m heading1.
     */
    private var mHeading1: TextView? = null

    /**
     * The relative params.
     */
    private var relativeParams: RelativeLayout.LayoutParams? = null

    /**
     * The heading1.
     */
    private var heading1: String? = null

    /**
     * The is cancel.
     */
    private val isCancel = false
    private var edtText: EditText? = null
    /* FOR HOME SCREEN */
    /**
     * The is home.
     */
    private var isHome = false

    /**
     * Gets the left text.
     *
     * @return the left text
     */
    /**
     * Sets the left text.
     *
     * @param mLeftText the new left text
     */
    /**
     * The m right text.
     */
    var leftText: TextView? = null
    /**
     * Gets the right text.
     *
     * @return the right text
     */
    /**
     * Sets the right text.
     *
     * @param mLeftText the new right text
     */
    var rightText: TextView? = null

    /**
     * Gets the left image.
     *
     * @return the left image
     */
    /**
     * Sets the left image.
     *
     * @param mLeftImage the new left image
     */
    /**
     * Gets the right image.
     *
     * @return the right image
     */
    /**
     * Sets the right image.
     *
     * @param mLeftImage the new right image
     */
    /**
     * The m left.
     */
    var rightImage: ImageView? = null
        set(mLeftImage) {
            mRightImage = mLeftImage
        }
    private var mRightImage: ImageView? = null
    /**
     * Gets the right button.
     *
     * @return the right button
     */
    /**
     * Sets the right button.
     *
     * @param right the new right button
     */
    var rightButton: ImageView? = null
    /**
     * Gets the left button.
     *
     * @return the left button
     */
    /**
     * Sets the left button.
     *
     * @param right the new left button
     */
    var leftButton: ImageView? = null

    /**
     * Instantiates a new headermanager.
     *
     * @param context  the context
     * @param heading1 the heading1
     */
    constructor(context: AppCompatActivity?, heading1: String?) {
        this.context = context
        inflator = LayoutInflater.from(context)
        this.heading1 = heading1
    }
    /*
     * public Headermanager(Activity context,String heading1) {
	 * this.setContext(context); inflator = LayoutInflater.from(context);
	 * this.heading1=heading1; this.isCancel=isCancel; }
	 */
    /**
     * Instantiates a new headermanager.
     *
     * @param home    the home
     * @param context the context
     */
    constructor(home: Boolean, context: AppCompatActivity?) {
        this.context = context
        inflator = LayoutInflater.from(context)
        isHome = home
    }

    // image view

    /**
     * Sets the visible.
     *
     * @param v the new visible
     */
    fun setVisible(v: View?) {
        v!!.visibility = View.VISIBLE
    }

    /**
     * Sets the invisible.
     */
    fun setInvisible() {
        headerView!!.visibility = View.INVISIBLE
    }

    /**
     * Sets the invisible.
     *
     * @param v the new invisible
     */
    fun setInvisible(v: View) {
        v.visibility = View.INVISIBLE
    }

    /**
     * Gets the header.
     *
     * @param headerHolder the header holder
     * @return the header
     */
    fun getHeader(headerHolder: LinearLayout, type: Int): Int {
        initializeUI(type)
        relativeParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        relativeParams!!.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        headerHolder.addView(headerView, relativeParams)
        return headerView!!.id
    }

    /**
     * Gets the header.
     *
     * @param headerHolder the header holder
     * @return the header
     */
    fun getHeader(
        headerHolder: LinearLayout, getHeading: Boolean,
        type: Int
    ): Int {
        initializeUI(getHeading, type)
        relativeParams = RelativeLayout.LayoutParams(
            LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        relativeParams!!.addRule(LinearLayout.VERTICAL)
        headerHolder.addView(headerView, relativeParams)
        return headerView!!.id
    }

    /**
     * Initialize ui.
     */
    private fun initializeUI(type: Int) {
        inflator = LayoutInflater.from(context)
        // System.out.println("htype" + type);
        headerView = inflator.inflate(R.layout.common_header_single, null)
        mHeading1 = headerView!!.findViewById<View>(R.id.heading) as TextView
        mHeading1!!.text = heading1

        rightButton =
            headerView!!.findViewById<View>(R.id.btn_right) as ImageView
        leftButton =
            headerView!!.findViewById<View>(R.id.btn_left) as ImageView

        if (type == 0) {

            rightButton!!.visibility = View.INVISIBLE
            leftButton!!.visibility = View.INVISIBLE
            // buttons
        } else if (type == 1) {
            rightButton!!.visibility = View.INVISIBLE
            leftButton!!.visibility = View.VISIBLE
            // button
        } else if (type == 2) {
            // left
            rightButton!!.visibility = View.VISIBLE
            leftButton!!.visibility = View.INVISIBLE
        } else if (type == 3) {
            rightButton!!.visibility = View.VISIBLE
            leftButton!!.visibility = View.VISIBLE
        }
    }

    /**
     * Initialize ui.
     */
    private fun initializeUI(getHeading: Boolean, type: Int) {
        inflator = LayoutInflater.from(context)
        headerView = inflator.inflate(R.layout.common_header_single, null)

        mHeading1 = headerView!!.findViewById<View>(R.id.heading) as TextView
        mHeading1!!.text = heading1
        rightButton =
            headerView!!.findViewById<View>(R.id.btn_right) as ImageView
        leftButton =
            headerView!!.findViewById<View>(R.id.btn_left) as ImageView
        if (type == 0) {
            rightButton!!.visibility = View.GONE
            leftButton!!.visibility = View.GONE
        } else if (type == 1) {
            rightButton!!.visibility = View.GONE
            leftButton!!.visibility = View.VISIBLE
        } else if (type == 2) {
            rightButton!!.visibility = View.VISIBLE
            leftButton!!.visibility = View.GONE
        } else if (type == 3) {
            rightButton!!.visibility = View.VISIBLE
            leftButton!!.visibility = View.VISIBLE
        }
    }

    /**
     * Sets the title bar.
     *
     * @param titleBar the new title bar
     */
    fun setTitleBar(titleBar: Int) {
        headerView!!.setBackgroundResource(titleBar)
    }

    fun setTitle(title: String?) {
        mHeading1!!.text = title
    }

    /**
     * Sets the edits the text.
     *
     * @param editText the new edits the text
     */
    var editText: EditText?
        get() {
            mHeading1!!.visibility = View.GONE
            edtText!!.visibility = View.VISIBLE
            return edtText
        }
        set(editText) {
            edtText = editText
            setVisible(edtText)
        }

    /**
     * Sets the button right selector.
     *
     * @param normalStateResID  the normal state res id
     * @param pressedStateResID the pressed state res id
     */
    fun setButtonRightSelector(
        normalStateResID: Int,
        pressedStateResID: Int
    ) {
        rightButton!!.setImageDrawable(
            getButtonDrawableByScreenCategory(
                normalStateResID, pressedStateResID
            )
        )
        setVisible(rightButton)
    }

    /**
     * Sets the button left selector.
     *
     * @param normalStateResID  the normal state res id
     * @param pressedStateResID the pressed state res id
     */
    fun setButtonLeftSelector(
        normalStateResID: Int,
        pressedStateResID: Int
    ) {
        leftButton!!.setImageDrawable(
            getButtonDrawableByScreenCategory(
                normalStateResID, pressedStateResID
            )
        )
        setVisible(leftButton)
    }

    /**
     * Gets the button drawable by screen cathegory.
     *
     * @param normalStateResID  the normal state res id
     * @param pressedStateResID the pressed state res id
     * @return the button drawable by screen cathegory
     */
    fun getButtonDrawableByScreenCategory(
        normalStateResID: Int,
        pressedStateResID: Int
    ): Drawable {
        val state_normal = context!!.resources
            .getDrawable(normalStateResID).mutate()
        val state_pressed = context!!.resources
            .getDrawable(pressedStateResID).mutate()
        val drawable =
            StateListDrawable()
        drawable.addState(
            intArrayOf(android.R.attr.state_pressed),
            state_pressed
        )
        drawable.addState(
            intArrayOf(android.R.attr.state_enabled),
            state_normal
        )
        return drawable
    }
}