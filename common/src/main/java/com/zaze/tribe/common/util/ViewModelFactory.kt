package com.zaze.tribe.common.util

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaze.tribe.common.App


fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProvider.AndroidViewModelFactory.getInstance(App.INSTANCE).create(viewModelClass)


fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProvider.AndroidViewModelFactory.getInstance(App.INSTANCE).create(viewModelClass)

