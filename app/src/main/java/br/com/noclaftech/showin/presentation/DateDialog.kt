package br.com.noclaftech.showin.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener
import br.com.noclaftech.showin.R
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.dialog_date.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DateDialog : DialogFragment() {

    var callback: ((String) -> Unit)? = null

    private val months = ArrayList<String>()
    private val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = it.layoutInflater.inflate(R.layout.dialog_date, null)

            val title = arguments?.get("title") as String
            view.title.text = title

            val subtitle = arguments?.get("subtitle") as String?

            if(subtitle != null){
                view.subtitle.text = subtitle
                view.subtitle.visibility = View.VISIBLE
            }

            resources.getStringArray(R.array.month).forEach { str ->
                months.add(str)
            }

            view.month.setOnClickListener { _ ->
                context?.let { ctx ->
                    MaterialDialog.Builder(ctx)
                        .title(getString(R.string.month))
                        .items(months)
                        .itemsCallback { _, _, _, text ->
                            view.month.setText(text)
                        }
                        .show()
                }
            }

            view.day.addTextChangedListener(MaskEditTextChangedListener("##", view.day))
            view.year.addTextChangedListener(MaskEditTextChangedListener("####", view.year))

            view.ok.setOnClickListener {
                if(view.month.text.isNullOrBlank()){
                    view.month.error = getString(R.string.required_field)
                    return@setOnClickListener
                }

                if(view.day.text.isNullOrBlank()){
                    view.day.error = getString(R.string.required_field)
                    return@setOnClickListener
                }

                if(view.day.text.length != 2){
                    view.day.error = getString(R.string.invalid_format)
                    return@setOnClickListener
                }

                if(view.year.text.isNullOrBlank()){
                    view.year.error = getString(R.string.required_field)
                    return@setOnClickListener
                }

                if(view.year.text.length != 4){
                    view.year.error = getString(R.string.invalid_format)
                    return@setOnClickListener
                }

                val yearToInt = view.year.text.toString().toInt()

                if (title == getString(R.string.birth_date)){
                    if (yearToInt > currentYear){
                        view.year.error = getString(R.string.invalid_year)
                        return@setOnClickListener
                    }
                } else {
                    if (yearToInt < currentYear){
                        view.year.error = getString(R.string.invalid_year)
                        return@setOnClickListener
                    }
                }

                if(view.day.text.toString().toInt() > 31){
                    view.day.error = getString(R.string.invalid_day)
                    return@setOnClickListener
                }


                val string = "${view.day.text}/${String.format("%02d", months.indexOf(view.month.text.toString()) + 1)}/${view.year.text}"
                callback?.invoke(string)
                dismiss()
            }
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialogTransparent)
            builder.setView(view)

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    companion object {
        fun getInstance(title: String, subtitle: String?, callback: ((String) -> Unit)?): DateDialog{
            val bundle = bundleOf("title" to title, "subtitle" to subtitle)
            return DateDialog().apply {
                arguments = bundle
                this.callback = callback
            }
        }
    }

}