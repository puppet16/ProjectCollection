package cn.ltt.projectcollection

import android.content.Intent
import android.view.View
import cn.ltt.projectcollection.base.BaseActivity
import cn.ltt.projectcollection.beziercurve.BezierCurveActivity
import cn.ltt.projectcollection.dragbubble.DragBubbleActivity
import cn.ltt.projectcollection.picloading.PictureLoadingActivity
import cn.ltt.projectcollection.picparticlessplit.PicParticlesSplitActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * ============================================================
 * @author   李桐桐
 * date    2021年04月14日
 * desc    描述
 * ============================================================
 **/
class MainActivity: BaseActivity(), View.OnClickListener {

    private val mActivityArray: HashMap<View,Class<out BaseActivity>> = HashMap()
    private val activity = this

    override fun initPage() {
        with(mActivityArray) {
            put(btnPicLoading.apply { setOnClickListener(activity)}, PictureLoadingActivity::class.java)
            put(btnPicParticlesSplit.apply { setOnClickListener(activity)}, PicParticlesSplitActivity::class.java)
            put(btnBezierCurve.apply { setOnClickListener(activity)}, BezierCurveActivity::class.java)
            put(btnDragBubble.apply { setOnClickListener(activity)}, DragBubbleActivity::class.java)
            put(btnNetRetry.apply { setOnClickListener(activity)}, DragBubbleActivity::class.java)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onClick(v: View) {
        startActivity(Intent(mActivity, mActivityArray[v]))
    }

}