/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package modelengine.fitframework.validation;

import modelengine.fitframework.util.LocaleContextHolder;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.validation.MessageInterpolator;

import java.util.Locale;

/**
 * 检验消息处理包装类。
 * <p>
 * 从 {@link LocaleContextHolder} 中获取当前线程设置的 {@link Locale} 并委托 {@link MessageInterpolator} 去处理消息。
 * </p>
 *
 * @author 阮睿
 * @since 2025-07-31
 */
public class LocaleContextMessageInterpolator implements MessageInterpolator {

    private final MessageInterpolator targetInterpolator;

    public LocaleContextMessageInterpolator(MessageInterpolator targetInterpolator) {
        this.targetInterpolator = targetInterpolator;
    }

    public LocaleContextMessageInterpolator() {
        this.targetInterpolator = new ParameterMessageInterpolator();
    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return this.targetInterpolator.interpolate(messageTemplate, context, LocaleContextHolder.getLocale());
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        return this.targetInterpolator.interpolate(messageTemplate, context, locale);
    }
}
