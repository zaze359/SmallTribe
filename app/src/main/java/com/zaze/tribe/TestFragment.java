package com.zaze.tribe;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    TextView textView;
//    private LyricView test_lyric_view;
    private int index = 0;
//
//    private Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (test_lyric_view.getValues() != null && index >= test_lyric_view.getValues().size()) {
//                index = 0;
//            } else {
//                index++;
//            }
//            test_lyric_view.next();
//            sendEmptyMessageDelayed(0, 500);
//        }
//    };
//

    private MutableLiveData<String> mLiveData = new MutableLiveData<>();

    public static TestFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString(KEY_CONTENT, content);
        TestFragment fragment = new TestFragment();
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
        textView = view.findViewById(R.id.test_content_tv);
//        test_lyric_view = view.findViewById(R.id.test_lyric_view);
        mLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        mLiveData.setValue("a");
//        try {
//            List<LrcLine> list = LrcProcessor.process(getActivity().getAssets().open("明月天涯.lrc"));
//            List<String> values = new ArrayList<>();
//            for (LrcLine line : list) {
//                values.add(line.getContent());
//            }
//            test_lyric_view.setValues(values);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        handler.sendEmptyMessage(0);
        return view;
    }

}