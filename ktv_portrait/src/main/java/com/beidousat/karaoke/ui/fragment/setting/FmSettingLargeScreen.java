package com.beidousat.karaoke.ui.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.util.PreferenceUtil;


public class FmSettingLargeScreen extends BaseFragment implements View.OnClickListener {
    private View mRootView;
    private EditText et_ip, et_port;
    private Button btn_ok;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_large_screen, null);
        et_ip = mRootView.findViewById(R.id.et_large_screen_ip);
        et_port = mRootView.findViewById(R.id.et_large_screen_port);
        btn_ok = mRootView.findViewById(R.id.btn_large_screen_ok);
        btn_ok.setOnClickListener(this);
        mRootView.findViewById(R.id.iv_back).setOnClickListener(this);
        if (!TextUtils.isEmpty(PreferenceUtil.getString(getContext(), "screen_ip", ""))) {
            et_ip.setText(PreferenceUtil.getString(getContext(), "screen_ip", ""));
        }
        if (!TextUtils.isEmpty(PreferenceUtil.getString(getContext(), "screen_port", ""))) {
            et_port.setText(PreferenceUtil.getString(getContext(), "screen_port", ""));
        }
        return mRootView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                EventBusUtil.postSticky(EventBusId.Id.BACK_FRAGMENT, "");
                break;
            case R.id.btn_large_screen_ok:
                if (TextUtils.isEmpty(et_ip.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "ip地址不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(et_port.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "端口不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    PreferenceUtil.setString(getContext(), "screen_ip", et_ip.getText().toString().trim());
                    PreferenceUtil.setString(getContext(), "screen_port", et_port.getText().toString().trim());
                    Toast.makeText(getContext(), "设置成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
