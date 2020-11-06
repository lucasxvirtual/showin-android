package br.com.noclaftech.showin.presentation.util

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import br.com.noclaftech.showin.R

object AlertHelper {

    private var alertDialog: AlertDialog? = null
    private var hasDialog = false

    fun alertGenericTwoButtons(context: Context,
                               title: String,
                               description: String,
                               titleButtonPositive: String?,
                               color: Int,
                               callbackButtonPositive: DialogInterface.OnClickListener?,
                               titleButtonNegative: String?,
                               callbackButtonNegative: DialogInterface.OnClickListener? ) {
        if (!hasDialog) {

            val dialogBuilder = AlertDialog.Builder( context, R.style.StackedAlertDialogStyle )
            alertDialog = dialogBuilder.create( )
            alertDialog!!.setTitle( title )

            if ( titleButtonPositive != null )
                alertDialog!!.setButton( DialogInterface.BUTTON_POSITIVE,
                    titleButtonPositive,
                    callbackButtonPositive )

            alertDialog!!.setButton( DialogInterface.BUTTON_NEGATIVE,
                titleButtonNegative,
                callbackButtonNegative )

            alertDialog!!.setMessage( description )
            alertDialog!!.setOnDismissListener { hasDialog = false }
            alertDialog!!.show( )
            alertDialog!!.getButton( DialogInterface.BUTTON_NEGATIVE ).setTextColor( color )
            alertDialog!!.getButton( DialogInterface.BUTTON_POSITIVE ).setTextColor( color )

            hasDialog = true
        }
    }

}