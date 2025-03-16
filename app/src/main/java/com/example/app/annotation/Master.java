// 在 com.example.app.annotation 包下创建 Master.java
package com.example.app.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Master {
}
