package com.lancewu.aspectj

import android.util.Log
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

/**
 * Description: 登录切面类
 * Author: lee
 * Date: 2021/9/10
 **/
@Aspect
class LoginAspect : CommonPointcut() {

    @Around("anyExecution() && @annotation(requireLogin)")
    @Throws(Throwable::class)
    fun requireLogin(proceedingJoinPoint: ProceedingJoinPoint, requireLogin: RequireLogin) {
        Log.d("LoginAspect", "调用登录切面")
        proceedingJoinPoint.proceed()
    }
}
