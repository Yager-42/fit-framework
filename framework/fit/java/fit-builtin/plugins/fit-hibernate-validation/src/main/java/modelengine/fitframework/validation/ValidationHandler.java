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
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
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
        if (hasJavaxConstraintAnnotations(joinPoint.getMethod().getParameters())) {
            ExecutableValidator execVal = this.validator.forExecutables();
            Set<ConstraintViolation<Object>> result = execVal.validateParameters(joinPoint.getTarget(),
                    joinPoint.getMethod(),
                    joinPoint.getArgs(),
                    validated.value());
            if (!result.isEmpty()) {
                throw new ConstraintViolationException(result);
            }
        }
    }

    @PreDestroy
    @Override
    public void close() {
        this.validatorFactory.close();
    }

    /**
     * 检查方法参数是否包含 javax.validation 校验注解
     *
     * @param parameters 方法参数数组
     * @return 如果包含 javax.validation 标注的校验注解则返回 true，否则返回 false
     */
    private boolean hasJavaxConstraintAnnotations(Parameter[] parameters) {
        return Arrays.stream(parameters)
                .anyMatch(this::hasConstraintAnnotationsInParameter);
    }

    /**
     * 检查参数及其泛型类型参数是否包含校验注解
     * @param parameter 方法参数数组
     * @return 如果包含 javax.validation 标注的校验注解则返回 true，否则返回 false
     */
    private boolean hasConstraintAnnotationsInParameter(Parameter parameter) {
        return hasConstraintAnnotationsInType(parameter.getAnnotatedType());
    }

    /**
     * 判断参数注解类型，解析参数本身注解及其泛型类型参数注解
     * @param annotatedType  参数注解类型
     * @return 如果包含 javax.validation 标注的校验注解则返回 true，否则返回 false
     */
    private boolean hasConstraintAnnotationsInType(AnnotatedType annotatedType) {
        // 检查当前类型上的注解
        if (annotatedType.getAnnotations().length > 0) {
            return Arrays.stream(annotatedType.getAnnotations()).anyMatch(this::isJavaxConstraintAnnotation);
        }
        
        // 如果是参数化类型，递归检查类型参数
        if (annotatedType instanceof AnnotatedParameterizedType) {
            AnnotatedParameterizedType parameterizedType = (AnnotatedParameterizedType) annotatedType;
            return Arrays.stream(parameterizedType.getAnnotatedActualTypeArguments())
                    .anyMatch(this::hasConstraintAnnotationsInType);
        }
        
        return false;
    }

    /**
     * 检查注解是否属于 javax.validation 注解
     *
     * @param annotation 要检查的注解
     * @return 如果属于 javax.validation 注解则返回 true，否则返回 false
     */
    private boolean isJavaxConstraintAnnotation(Annotation annotation) {
        // @Valid 注解检查
        if(annotation.annotationType().getName().equals("javax.validation.Valid")){
            return true;
        }
        // javax.validation.constraints， org.hibernate.validator.constraints 包下的注解检查
        // 通过 Constraint 注解检查当前注解是否为校验注解
        Annotation[] metaAnnotations = annotation.annotationType().getAnnotations();
        return Arrays.stream(metaAnnotations)
                .anyMatch(metaAnnotation -> {
                    String packageName = metaAnnotation.annotationType().getPackage().getName();
                    String className = metaAnnotation.annotationType().getSimpleName();
                    return "javax.validation".equals(packageName) && "Constraint".equals(className);
                });
    }
}
