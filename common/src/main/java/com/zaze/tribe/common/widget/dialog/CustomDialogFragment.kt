package com.zaze.tribe.common.widget.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.DialogFragment

class CustomDialogFragment : DialogFragment() {
    private var builder: DialogProvider.Builder? = null
    private lateinit var customDialog: CustomDialog

    fun init(builder: DialogProvider.Builder): CustomDialogFragment {
        this.builder = builder
        return this
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
        Log.i("Dialog", "CustomDialogFragment onCreateDialog")
        builder?.let {
            customDialog = CustomDialog(requireContext(), it)
            return customDialog.dialog

        } ?: return super.onCreateDialog(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        customDialog.onDestroy()
    }

    override fun getTheme(): Int {
        return builder?.theme ?: super.getTheme()
    }


    fun show(manager: FragmentManager) {
        super.show(manager, builder?.tag)
    }
}