package cn.ltt.projectcollection.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * ============================================================
 *
 * @author 李桐桐
 * date    2020/10/10
 * desc    描述
 * ============================================================
 **/
public abstract class BaseActivity extends AppCompatActivity {
    protected BaseActivity mActivity;
    private final String TAG = getClass().getSimpleName();
    protected Unbinder mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(getLayoutId());
        initPage();
    }

    protected abstract void initPage();

    protected abstract int getLayoutId();

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        mBinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBinder != null) {
            mBinder.unbind();
        }
    }
}
