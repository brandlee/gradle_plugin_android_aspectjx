package com.lancewu.aspectj

import org.aspectj.lang.annotation.Pointcut

open class CommonPointcut {
    /**
     * Pointcut：切点是具体的链接点，切点定义了需要织入代码的链接点
     * 所有方法的execution
     */
    @Pointcut("execution(* *..*.*(..))")
    fun anyExecution() {
    }
}
