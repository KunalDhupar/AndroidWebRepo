package com.cg.androidweb

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MyDialog :DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dlg:Dialog?=null
        val builder=AlertDialog.Builder(activity!!)
        builder.setTitle("Getting places..")
        builder.setMessage("Please wait....")
        dlg=builder.create()

        return dlg
    }
}