package br.com.noclaftech.showin.presentation.util

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spannable
import android.text.style.ImageSpan
import android.util.AttributeSet
import br.com.noclaftech.showin.R
import java.util.regex.Matcher
import java.util.regex.Pattern


open class TextViewWithImages : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?) : super(context)

    override fun setText(text: CharSequence, type: BufferType?) {
        val spannable: Spannable = getTextWithImages(
            context,
            text,
            lineHeight,
            currentTextColor
        )
        super.setText(spannable, type)
    }



    companion object {
        private const val DRAWABLE = "drawable"

        /**
         * Regex pattern that looks for embedded images of the format: [img src=imageName/]
         */
        private const val PATTERN = "\\Q¶\\E([a-zA-Z0-9_]+?)\\Q¶\\E"
        fun getTextWithImages(
            context: Context,
            text: CharSequence,
            lineHeight: Int,
            colour: Int
        ): Spannable {
            val spannable: Spannable = Spannable.Factory.getInstance().newSpannable(text)
            addImages(context, spannable, lineHeight, colour)
            return spannable
        }

        private fun addImages(
            context: Context,
            spannable: Spannable,
            lineHeight: Int,
            colour: Int
        ): Boolean {
            val refImg: Pattern = Pattern.compile(PATTERN)
            var hasChanges = false
            val matcher: Matcher = refImg.matcher(spannable)
            while (matcher.find()) {
                var set = true
                for (span in spannable.getSpans(
                    matcher.start(), matcher.end(),
                    ImageSpan::class.java
                )) {
                    if (spannable.getSpanStart(span) >= matcher.start()
                        && spannable.getSpanEnd(span) <= matcher.end()
                    ) {
                        spannable.removeSpan(span)
                    } else {
                        set = false
                        break
                    }
                }
                val resName: String =
                    spannable.subSequence(matcher.start(1), matcher.end(1)).toString().trim()

                if(resName !in listOf("hot", "laugh", "heart", "love", "star", "clap", "stars", "bigstar"))
                    continue

                val id: Int = context.resources.getIdentifier(
                    resName,
                    DRAWABLE,
                    context.packageName
                )
                if (set) {
                    hasChanges = true
                    spannable.setSpan(
                        makeImageSpan(context, id, lineHeight, colour),
                        matcher.start(),
                        matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            return hasChanges
        }

        /**
         * Create an ImageSpan for the given icon drawable. This also sets the image size and colour.
         * Works best with a white, square icon because of the colouring and resizing.
         *
         * @param context       The Android Context.
         * @param drawableResId A drawable resource Id.
         * @param size          The desired size (i.e. width and height) of the image icon in pixels.
         * Use the lineHeight of the TextView to make the image inline with the
         * surrounding text.
         * @param colour        The colour (careful: NOT a resource Id) to apply to the image.
         * @return An ImageSpan, aligned with the bottom of the text.
         */
        private fun makeImageSpan(
            context: Context,
            drawableResId: Int,
            size: Int,
            colour: Int
        ): ImageSpan {
            val drawable: Drawable = context.resources.getDrawable(drawableResId)
            drawable.mutate()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                drawable.colorFilter = BlendModeColorFilter(colour, BlendMode.MULTIPLY)
//            } else {
//                drawable.setColorFilter(colour, PorterDuff.Mode.MULTIPLY)
//            }
            drawable.setBounds(0, 0, (size*1.2).toInt(), (size*1.2).toInt())
            return ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM)
        }
    }
}