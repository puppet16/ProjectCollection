package cn.ltt.projectcollection.kotlin;

import java.util.HashSet;

/**
 * ============================================================
 *
 * @author 李桐桐
 * date    1/27/21
 * desc    描述
 * ============================================================
 **/
public class CallbackManager {
    interface ViewCallback {
        void onViewCallback(int viewId);
    }

    public void registerCallback(ViewCallback callback) {
        mCallbacks.add(callback);
    }

    public void unregisterCallback(ViewCallback callback) {
        mCallbacks.remove(callback);
    }

    private HashSet<ViewCallback> mCallbacks = new HashSet<>();
}
