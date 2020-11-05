package cn.ltt.projectcollection;

import android.content.Intent;
import android.util.SparseArray;
import android.view.View;

import butterknife.OnClick;
import cn.ltt.projectcollection.base.BaseActivity;
import cn.ltt.projectcollection.beziercurve.BezierCurveActivity;
import cn.ltt.projectcollection.picloading.PictureLoadingActivity;
import cn.ltt.projectcollection.picparticlessplit.PicParticlesSplitActivity;

public class MainActivity extends BaseActivity {

    private SparseArray<Class<? extends BaseActivity>> mActivityArray = new SparseArray<Class<? extends BaseActivity>>() {
        {
            put(R.id.btnPicLoading, PictureLoadingActivity.class);
            put(R.id.btnPicParticlesSplit, PicParticlesSplitActivity.class);
            put(R.id.btnBezierCurve, BezierCurveActivity.class);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPage() {

    }

    @OnClick({R.id.btnPicParticlesSplit, R.id.btnPicLoading, R.id.btnBezierCurve})
    public void onClick(View v) {
        startActivity(new Intent(mActivity, mActivityArray.get(v.getId())));
    }
}