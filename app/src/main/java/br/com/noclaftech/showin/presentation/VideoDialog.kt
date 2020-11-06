package br.com.noclaftech.showin.presentation

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import br.com.noclaftech.showin.R
import kotlinx.android.synthetic.main.dialog_video.view.*

class VideoDialog: DialogFragment() {

    companion object {
        fun newInstance(url: String): VideoDialog {
            val bundle = bundleOf("url" to url)
            return VideoDialog().apply {
                arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.dialog_theme)
    }

    override fun onStart() {
        super.onStart()
        val d: Dialog? = dialog
        if (d != null) {
            val width: Int = ViewGroup.LayoutParams.MATCH_PARENT
            val height: Int = ViewGroup.LayoutParams.MATCH_PARENT
            d.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_video, container, false)

        view.video.setOnPreparedListener { view.video.start() }
        view.video.setVideoURI(Uri.parse(arguments?.get("url") as String))

        return view
    }

}