package com.stic.trasher.controll

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class ChallengeViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private var _enabled = false

    init {
        this._enabled = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this._enabled) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this._enabled) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    fun setPagingEnabled(enabled: Boolean) {
        this._enabled = enabled
    }

}