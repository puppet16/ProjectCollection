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
public abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var mActivity: BaseActivity
    @JvmField
    val TAG : String =this::class.java.simpleName
    protected lateinit var mBinder : Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        setContentView(getLayoutId())
        initPage()
    }

    protected abstract fun initPage();

    protected abstract fun getLayoutId() : Int

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        mBinder = ButterKnife.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinder.unbind()
    }
}
