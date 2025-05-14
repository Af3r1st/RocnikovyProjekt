package com.levurda.fitnessproject.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.levurda.fitnessproject.R

object DialogManager {
    fun showDialog(context: Context, mId: Int, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        var dialog: AlertDialog? = null
        builder.setTitle(R.string.alert)
        builder.setMessage(mId)
        builder.setPositiveButton(R.string.reset) { _, _ ->
            listener.onClick()
            dialog?.dismiss()
        }
        builder.setNegativeButton(R.string.cancel) { _, _ ->
            dialog?.dismiss()

        }

        dialog = builder.create()
        dialog.show()
    }

    interface Listener {
        fun onClick()
    }
}
