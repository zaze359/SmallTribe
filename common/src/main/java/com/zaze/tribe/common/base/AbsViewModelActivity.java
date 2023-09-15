package com.zaze.tribe.common.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.zaze.tribe.common.util.AppCompatActivityExtKt;


public abstract class AbsViewModelActivity extends AbsLogActivity {

    /**
     * kotlin 中重写此方法报错， 'getDefaultViewModelProviderFactory' overrides nothing.
     * 先通过在 java 中重写处理。
     * @return ViewModelProvider.Factory
     */
    @NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return AppCompatActivityExtKt.obtainViewModelFactory(this, super.getDefaultViewModelProviderFactory());
    }
}
