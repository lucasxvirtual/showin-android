package br.com.noclaftech.showin.presentation.util

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.Selection
import android.text.TextUtils
import android.text.method.ArrowKeyMovementMethod
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.widget.TextView

class EditTextWithImages : TextViewWithImages {
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?) : super(context)

    override fun getFreezesText(): Boolean {
        return true
    }

    override fun getDefaultEditable(): Boolean {
        return true
    }

    override fun getDefaultMovementMethod(): MovementMethod? {
        return ArrowKeyMovementMethod.getInstance()
    }

    override fun getText(): Editable? {
        return if (Build.VERSION.SDK_INT >= 28) {
            val text = super.getText() ?: return null
            // This can only happen during construction.
            if (text is Editable) {
                return super.getText() as Editable
            }
            super.setText(text, BufferType.EDITABLE)
            return super.getText() as Editable
        } else super.getEditableText()
    }

    override fun setText(
        text: CharSequence,
        type: BufferType?
    ) {
        super.setText(text, BufferType.EDITABLE)
    }

    /**
     * Convenience for [Selection.setSelection].
     */
    fun setSelection(start: Int, stop: Int) {
        Selection.setSelection(text, start, stop)
    }

    /**
     * Convenience for [Selection.setSelection].
     */
    fun setSelection(index: Int) {
        Selection.setSelection(text, index)
    }

    /**
     * Convenience for [Selection.selectAll].
     */
    fun selectAll() {
        Selection.selectAll(text)
    }

    /**
     * Convenience for [Selection.extendSelection].
     */
    fun extendSelection(index: Int) {
        Selection.extendSelection(text, index)
    }

    /**
     * Causes words in the text that are longer than the view's width to be ellipsized instead of
     * broken in the middle. [ TextUtils.TruncateAt#MARQUEE][TextUtils.TruncateAt.MARQUEE] is not supported.
     *
     * @param ellipsis Type of ellipsis to be applied.
     * @throws IllegalArgumentException When the value of `ellipsis` parameter is
     * [TextUtils.TruncateAt.MARQUEE].
     * @see TextView.setEllipsize
     */
    override fun setEllipsize(ellipsis: TextUtils.TruncateAt) {
        require(ellipsis != TextUtils.TruncateAt.MARQUEE) {
            ("EditText cannot use the ellipsize mode "
                    + "TextUtils.TruncateAt.MARQUEE")
        }
        super.setEllipsize(ellipsis)
    }

    override fun getAccessibilityClassName(): CharSequence? {
        return android.widget.EditText::class.java.name
    }

    /** @hide
     */
    protected fun supportsAutoSizeText(): Boolean {
        return false
    }


}