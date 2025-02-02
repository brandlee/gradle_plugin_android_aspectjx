package com.lancewu.aspectj

/**
 * Description: 交易密码注解
 * 使用[AnnotationRetention.RUNTIME]是因为切面中要使用注解的值，如果切面中不使用注解的值则可以使用[AnnotationRetention.SOURCE]类型
 * Author: lee
 * Date: 2021/9/13
 **/
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequireTradePassword(val proceed: Boolean = true)
