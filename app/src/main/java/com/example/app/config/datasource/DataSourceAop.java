package com.example.app.config.datasource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAop {
    // 读切面（匹配module模块下所有service的查询方法）
    @Pointcut("!@annotation(com.example.app.annotation.Master) " +
            "&& (execution(* com.example.module..service..*.select*(..)) " +
            "|| execution(* com.example.module..service..*.get*(..)) " +
            "|| execution(* com.example.module..service..*.find*(..)) " +
            "|| execution(* com.example.module..service..*.query*(..)))")
    public void readPointcut() {}

    // 写切面（匹配主库操作）
    @Pointcut("@annotation(com.example.app.annotation.Master) " +
            "|| execution(* com.example.module..service..*.insert*(..)) " +
            "|| execution(* com.example.module..service..*.add*(..)) " +
            "|| execution(* com.example.module..service..*.update*(..)) " +
            "|| execution(* com.example.module..service..*.edit*(..)) " +
            "|| execution(* com.example.module..service..*.delete*(..)) " +
            "|| execution(* com.example.module..service..*.remove*(..))")
    public void writePointcut() {}

    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }
}
