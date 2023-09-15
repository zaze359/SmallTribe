package com.zaze.tribe.common.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.zaze.tribe.common.util.FragmentExtKt;

public abstract class AbsViewModelFragment extends AbsLogFragment {
    public AbsViewModelFragment() {
    }

    public AbsViewModelFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return FragmentExtKt.obtainViewModelFactory(this, super.getDefaultViewModelProviderFactory());
    }
}
