/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2024 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package modelengine.fitframework.validation;

import modelengine.fitframework.annotation.Component;
import modelengine.fitframework.annotation.Scope;
import modelengine.fitframework.aop.JoinPoint;
import modelengine.fitframework.aop.annotation.Aspect;
import modelengine.fitframework.aop.annotation.Before;
import modelengine.fitframework.ioc.annotation.PreDestroy;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;

/**
 * 校验入口类。
 * <p>
 * 当调用的类或方法参数包含 {@link Validated} 注解时，会对该方法进行校验处理。
 * </p>
 *
 * @author 易文渊
 * @since 2024-09-27
 */
@Aspect(scope = Scope.GLOBAL)
@Component
public class ValidationHandler implements AutoCloseable {
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ValidationHandler() {
        this.validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .failFast(false)
                .buildValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Before(value = "@target(validated) && execution(public * *(..))", argNames = "joinPoint, validated")
    private void handle(JoinPoint joinPoint, Validated validated) {
        // 检查方法参数是否包含被 javax.validation.Constraint 标注的校验注解
        if (hasJavaxConstraintAnnotations(joinPoint.getMethod().getParameters())) {
            return;
        }
        ExecutableValidator execVal = this.validator.forExecutables();
        Set<ConstraintViolation<Object>> result = execVal.validateParameters(joinPoint.getTarget(),
                joinPoint.getMethod(),
                joinPoint.getArgs(),
                validated.value());
        if (!result.isEmpty()) {
            throw new ConstraintViolationException(result);
        }
    }

    @PreDestroy
    @Override
    public void close() {
        this.validatorFactory.close();
    }

    /**
     * 检查方法参数是否包含被 javax.validation.Constraint 标注的校验注解
     *
     * @param parameters 方法参数数组
     * @return 如果包含被 javax.validation.Constraint 标注的校验注解则返回 true，否则返回 false
     */
    private boolean hasJavaxConstraintAnnotations(Parameter[] parameters) {
        return Arrays.stream(parameters)
                .flatMap(parameter -> Arrays.stream(parameter.getAnnotations()))
                .anyMatch(this::isJavaxConstraintAnnotation);
    }

    /**
     * 检查注解是否被 javax.validation.Constraint 标注
     *
     * @param annotation 要检查的注解
     * @return 如果被 javax.validation.Constraint 标注则返回 true，否则返回 false
     */
    private boolean isJavaxConstraintAnnotation(Annotation annotation) {
        Annotation[] metaAnnotations = annotation.annotationType().getAnnotations();
        return Arrays.stream(metaAnnotations)
                .anyMatch(metaAnnotation -> {
                    String packageName = metaAnnotation.annotationType().getPackage().getName();
                    String className = metaAnnotation.annotationType().getSimpleName();
                    return "javax.validation".equals(packageName) && "Constraint".equals(className);
                });
    }
}
