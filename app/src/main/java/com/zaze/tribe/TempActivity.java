package com.zaze.tribe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zaze.tribe.music.MusicPlayerRemote;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-08-23 - 22:01
 */
@Deprecated
public class TempActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_empty_view);
    }
}
