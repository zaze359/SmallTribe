package com.zaze.tribe.common.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.zaze.tribe.common.BaseApplication


fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(viewModelClass)


fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelProvider.AndroidViewModelFactory.getInstance(BaseApplication.INSTANCE)).get(viewModelClass)


