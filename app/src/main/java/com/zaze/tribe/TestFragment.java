package com.zaze.tribe;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaze.tribe.data.entity.LrcLine;
import com.zaze.tribe.util.LrcProcessor;
import com.zaze.tribe.view.LyricView;
import com.zaze.utils.date.ZDateUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-02-16 - 20:07
 */
@Deprecated
public class TestFragment extends Fragment {
    private static final String KEY_CONTENT = "content";
    private String content = "";
    TextView testContentTv;
    private LyricView testLyricView;
    private int index = 0;
    //
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (testLyricView.getValues() != null && index >= testLyricView.getValues().size()) {
                index = 0;
            } else {
                index++;
            }
            testLyricView.next();
            StringBuilder builder = new StringBuilder();
            builder.append(TimeZone.getDefault().getDisplayName() + "\n");
            builder.append(TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT) + "\n");
            builder.append(TimeZone.getDefault().getRawOffset() / ZDateUtil.HOUR + "\n");
            builder.append(ZDateUtil.timeMillisToString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            mLiveData.setValue(builder.toString());
            handler.postDelayed(runnable, 1000L);
        }
    };


    private MutableLiveData<String> mLiveData = new MutableLiveData<>();

    public static TestFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString(KEY_CONTENT, content);
        TestFragment fragment = new TestFragment();
//        fragment.setUserVisibleHint(true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = getArguments().getString(KEY_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.test_frag, container, false);
        testContentTv = view.findViewById(R.id.test_content_tv);
        testLyricView = view.findViewById(R.id.test_lyric_view);
        mLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                testContentTv.setText(s);
            }
        });
        try {
            List<LrcLine> list = LrcProcessor.process(getActivity().getAssets().open("明月天涯.lrc"));
            List<String> values = new ArrayList<>();
            for (LrcLine line : list) {
                values.add(line.getContent());
            }
            testLyricView.setValues(values);
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.post(runnable);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}