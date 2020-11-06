package br.com.noclaftech.showin.presentation

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import br.com.noclaftech.domain.model.ChatMessage
import br.com.noclaftech.domain.model.User
import br.com.noclaftech.showin.R
import br.com.noclaftech.showin.presentation.artistprofile.ArtistProfileActivity
import br.com.noclaftech.showin.presentation.util.Helper
import kotlinx.android.synthetic.main.dialog_chat_user.view.*

class ChatUserDialog(val user : User, val message: ChatMessage, val callback: ((userId : Int, message : String, reason : String) -> Unit)?) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val view = it.layoutInflater.inflate(R.layout.dialog_chat_user, null)

            if (user.whatsapp.isNullOrBlank()){
                view.whatsapp_icon.visibility = View.GONE
            } else {
                view.whatsapp_icon.setOnClickListener {
                    Helper.whatsapp(activity!!, user.whatsapp!!)
                }
            }

            if (user.instagram.isNullOrBlank()){
                view.instagram_icon.visibility = View.GONE
            } else {
                view.instagram_icon.setOnClickListener {
                    Helper.instagram(activity!!, user.instagram!!)
                }
            }

            if (user.facebook.isNullOrBlank()){
                view.facebook_icon.visibility = View.GONE
            } else {
                view.facebook_icon.setOnClickListener {
                    Helper.facebook(activity!!, user.facebook!!)
                }
            }

            if (user.linkedin.isNullOrBlank()){
                view.linkedin_icon.visibility = View.GONE
            } else {
                view.linkedin_icon.setOnClickListener {
                    Helper.linkedin(activity!!, user.linkedin!!)
                }
            }

            if (user.twitter.isNullOrBlank()){
                view.twitter_icon.visibility = View.GONE
            } else {
                view.twitter_icon.setOnClickListener {
                    Helper.twitter(activity!!, user.twitter!!)
                }
            }

            if (user.whatsapp.isNullOrBlank() &&
                user.instagram.isNullOrBlank() &&
                user.facebook.isNullOrBlank() &&
                user.linkedin.isNullOrBlank() &&
                user.twitter.isNullOrBlank()) {

                view.view_1.visibility = View.GONE
            }

            view.visit_profile.setOnClickListener {
                val intent = Intent(context, ArtistProfileActivity::class.java).apply {
                    putExtra(EXTRA_ARTIST, user.id)
                }
                startActivity(intent)
                dismiss()
            }

            view.denounce.setOnClickListener {
                DenounceDialog(message) { userId, message, reason ->
                    callback?.invoke(userId, message, reason)
                }.show(activity!!.supportFragmentManager, "")
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
        const val EXTRA_ARTIST = ""
    }
}