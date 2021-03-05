package com.marcossalto.targetmvd.util

import android.app.AlertDialog
import android.content.Context
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.ui.custom.LoadingDialog

private var loadingDialog: LoadingDialog? = null

object DialogUtil {

    fun showProgress(context: Context) {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(context, null)
        }

        loadingDialog!!.show()
    }

    fun hideProgress() {
        if (loadingDialog != null) {
            loadingDialog!!.dismiss()
        }
    }

    fun showError(context: Context, message: String?) {
        val builderMessage = if(message.isNullOrBlank())
            context.getString(R.string.generic_error)
        else
            message

        val builder = AlertDialog.Builder(context)

        builder.apply {
            setTitle(context.getString(R.string.error))
            setMessage(builderMessage)
            setPositiveButton(context.getString(R.string.ok)) { dialog, _ ->
                dialog.cancel()
            }
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
