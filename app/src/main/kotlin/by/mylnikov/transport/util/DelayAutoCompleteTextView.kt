package by.mylnikov.transport.util

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.AutoCompleteTextView


class DelayAutoCompleteTextView(context: Context, attrs: AttributeSet) :
        AutoCompleteTextView(context, attrs) {

    private val mHandler: Handler = Handler()

    override fun performFiltering(text: CharSequence, keyCode: Int) {
        mHandler.removeCallbacksAndMessages(null)
        mHandler.postDelayed({
            super@DelayAutoCompleteTextView.performFiltering(text, keyCode)
        }, 750)
    }
}