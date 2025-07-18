/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
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

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

/**
 * 校验入口类。
 * <p>
 * 当调用的类或方法参数包含 {@link Validated} 注解时，会对该方法进行校验处理。
 * </p>
 *
 * @author 阮睿
 * @since 2025-07-18
 */
@Aspect(scope = Scope.GLOBAL)
@Component
public class ValidationHandler implements AutoCloseable {
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    ValidationHandler() {
        this.validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .failFast(false)
                .buildValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Before(value = "@target(validated) && execution(public * *(..))", argNames = "joinPoint, validated")
    private void handle(JoinPoint joinPoint, Validated validated) {
        // 检查方法参数是否包含被 jakarta.validation.Constraint 标注的校验注解
        if (hasJakartaConstraintAnnotations(joinPoint.getMethod().getParameters())) {
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
     * 检查方法参数是否包含被 jakarta.validation.Constraint 标注的校验注解
     *
     * @param parameters 方法参数数组
     * @return 如果包含被 jakarta.validation.Constraint 标注的校验注解则返回 true，否则返回 false
     */
    private boolean hasJakartaConstraintAnnotations(Parameter[] parameters) {
        return Arrays.stream(parameters)
                .flatMap(parameter -> Arrays.stream(parameter.getAnnotations()))
                .anyMatch(this::isJakartaConstraintAnnotation);
    }

    /**
     * 检查注解是否被 jakarta.validation.Constraint 标注
     *
     * @param annotation 要检查的注解
     * @return 如果被 jakarta.validation.Constraint 标注则返回 true，否则返回 false
     */
    private boolean isJakartaConstraintAnnotation(Annotation annotation) {
        Annotation[] metaAnnotations = annotation.annotationType().getAnnotations();
        return Arrays.stream(metaAnnotations)
                .anyMatch(metaAnnotation -> {
                    String packageName = metaAnnotation.annotationType().getPackage().getName();
                    String className = metaAnnotation.annotationType().getSimpleName();
                    return "jakarta.validation".equals(packageName) && "Constraint".equals(className);
                });
    }
}
