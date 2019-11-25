package com.beidousat.karaoke.ui.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;
import android.widget.Toast;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.view.FragmentModel;
import com.bestarmedia.libwidget.anim.CubeAnimation;

/**
 * Created by J Wong on 2015/10/9 11:28.
 */
public class BaseFragment extends Fragment implements HttpRequestListener {

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

//    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            if (isSupportHidden) {
//                ft.hide(this);
//            } else {
//                ft.show(this);
//            }
//            ft.commit();
//        }
//    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    public HttpRequestV4 initRequestV4(String method) {
        HttpRequestV4 request = new HttpRequestV4(getContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }
    //不再使用
//    public Context getContext() {
//        return getActivity().getApplicationContext();
//    }

    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
    }

    @Override
    public void onFailed(String method, Object object) {
        if (isAdded() && getContext() != null)
            if (object instanceof BaseModelV4) {
                BaseModelV4 baseModelV4 = (BaseModelV4) object;
                Toast.makeText(getContext(), baseModelV4.tips, Toast.LENGTH_SHORT).show();
            } else if (object instanceof String) {
                Toast.makeText(getContext(), object.toString(), Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onError(String method, String error) {
        if (isAdded() && getContext() != null)
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        return CubeAnimation.create(CubeAnimation.LEFT, enter, 150);
        //   return FlipAnimation.create(FlipAnimation.LEFT, enter, 200);
        //   return MoveAnimation.create(MoveAnimation.LEFT, enter, 200);
// return PushPullAnimation.create(PushPullAnimation.LEFT, enter, 200);
        //     return SidesAnimation.create(SidesAnimation.LEFT, enter, 200);
//        return SidesAnimation.create(SidesAnimation.LEFT, enter, 200);
        return OkConfig.boxManufacturer() == 2 ? super.onCreateAnimation(transit, enter, nextAnim) : CubeAnimation.create(CubeAnimation.LEFT, enter, 150);
    }

    public void addFragment(Fragment fragment, boolean isHideTopBar, boolean isHideLeftBar, boolean isHideBottomBar) {
        FragmentModel fragmentModel = new FragmentModel(fragment, false, isHideTopBar, isHideLeftBar, isHideBottomBar);
        EventBusUtil.postSticky(EventBusId.Id.ADD_FRAGMENT, fragmentModel);
    }
}
