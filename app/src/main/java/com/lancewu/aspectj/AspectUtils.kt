package com.lancewu.aspectj

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.aspectj.lang.JoinPoint

object AspectUtils {
    fun getActivity(joinPoint: JoinPoint): FragmentActivity? {
        //先看this
        if (joinPoint.getThis() is Activity) {
            return joinPoint.getThis() as FragmentActivity
        }
        //target
        if (joinPoint.target is Activity) {
            return joinPoint.target as FragmentActivity
        }
        // 一定是要获取到Activity的！
        if (joinPoint.target is Fragment) {
            return (joinPoint.target as? Fragment)?.activity as? FragmentActivity
        }

        if (joinPoint.`this` is IAspectJActivity) {
            return (joinPoint.`this` as IAspectJActivity).getAttachedActivity()
        }

        return null
    }

    /**
     * Aspect的回调，表示切面成功执行或者失败终止
     */
    @FunctionalInterface
    interface AspectCallback {
        fun onResult(result: Boolean)
    }
}
