package com.lancewu.aspectj

import android.util.Log
import androidx.fragment.app.FragmentActivity
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

/**
 * Description: 交易密码切面类
 * Author: lee
 * Date: 2021/9/10
 **/
@Aspect
class TradePasswordAspect : CommonPointcut() {
    /**
     * 校验交易密码，所有需要校验交易密码的地方都要校验账户状态
     */
    @Around("anyExecution() && @annotation(requireTradePassword)")
    @Throws(Throwable::class)
    fun requireTradePassword(proceedingJoinPoint: ProceedingJoinPoint, requireTradePassword: RequireTradePassword) {
            AspectUtils.getActivity(proceedingJoinPoint)?.run {
                showPasswordDialog(this, proceedingJoinPoint, requireTradePassword)
            }
    }

    private fun showPasswordDialog(activity: FragmentActivity, proceedingJoinPoint: ProceedingJoinPoint, requireTradePassword: RequireTradePassword) {
        TradePasswordDialogFragment.Builder(activity)
            .setLoginSuccessListener {
                if (requireTradePassword.proceed) proceedingJoinPoint.proceed()
            }.setDismissListener {
                (activity as? AspectUtils.AspectCallback)?.onResult(false)
            }.build().show(activity)
    }
}
