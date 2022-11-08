package com.lancewu.aspectj

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.DeclarePrecedence

/**
 * Description: 这个类决定了切面的执行顺序
 *                     没有写明每个切面的全路径，是因为[CoordinationAspect]和其他的切面在同一个包里面
 * Wiki：(if needed)
 * Author: lee
 * Date: 2021/10/21
 **/
@Aspect
@DeclarePrecedence("LoginAspect,TradePasswordAspect")
class CoordinationAspect