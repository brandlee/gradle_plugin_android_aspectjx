package com.lancewu.aspectj

/**
 * Description: 登录注解
 * 使用[AnnotationRetention.RUNTIME]是因为切面中要使用注解的值，如果切面中不使用注解的值则可以使用[AnnotationRetention.SOURCE]类型
 * @param alertContent 登录弹窗提示内容，用户自定义
 * Author: lee
 * Date: 2021/9/10
 **/
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequireLogin(val proceed: Boolean = true, val alertContent: String = "登录即可解锁该功能")
