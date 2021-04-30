package cn.ltt.projectcollection.kotlin.coroutinesLab;

import android.os.utils.Logger;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlinx.coroutines.DelayKt;

/**
 * ============================================================
 *
 * @author 李桐桐
 * date    2021年04月30日
 * desc    java代码描述协程操作
 * ============================================================
 **/
//public class ContinuationImpl implements Continuation<Object> {
//
//    private int mLabel = 0;
//
//    private final Continuation<Unit> mCompletion;
//
//    public ContinuationImpl(Continuation<Unit> completion) {
//        mCompletion = completion;
//    }
//
//    @NotNull
//    @Override
//    public CoroutineContext getContext() {
//        return EmptyCoroutineContext.INSTANCE;
//    }
//
//    @Override
//    public void resumeWith(@NotNull Object o) {
//        try {
//            Object result = o;
//            switch (mLabel) {
//                case 0: {
//                    Logger.debug(1);
//                    result = ConsoleMainKt.returnSuspend(this);
//                    mLabel++;
//                    if (isSuspended(result)) return;
//                }
//                case 1: {
//                    Logger.debug(result);
//                    Logger.debug(2);
//                    result = DelayKt.delay(1000, this);
//                    mLabel++;
//                    if (isSuspended(result)) return;
//                }
//                case 2: {
//                    Logger.debug(3);
//                    result = ConsoleMainKt.returnImmediately(this);
//                    mLabel++;
//                    if (isSuspended(result)) return;
//                }
//                case 3: {
//                    Logger.debug(result);
//                    Logger.debug(4);
//                }
//            }
//            mCompletion.resumeWith(Unit.INSTANCE);
//        } catch (Exception e) {
//            mCompletion.resumeWith(e);
//        }
//    }
//
//    private boolean isSuspended(Object o) {
//        return o == IntrinsicsKt.getCOROUTINE_SUSPENDED();
//    }
//}
