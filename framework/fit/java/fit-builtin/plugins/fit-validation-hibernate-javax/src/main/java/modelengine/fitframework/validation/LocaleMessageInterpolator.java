/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package modelengine.fitframework.validation;

import javax.validation.MessageInterpolator;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.Locale;

/**
 * 地区消息插值器。
 * <p>
 * 作为 Jakarta 消息插值器的代理类，提供地区设置能力。
 * </p>
 *
 * @author 阮睿
 * @since 2025-08-18
 */
public class LocaleMessageInterpolator implements MessageInterpolator {
    private final MessageInterpolator target;

    private Locale locale;

    public LocaleMessageInterpolator(MessageInterpolator target) {
        this.target = target;
        this.locale = Locale.getDefault();
    }

    public LocaleMessageInterpolator(Locale locale) {
        this.locale = locale;
        this.target = new ParameterMessageInterpolator();
    }

    public LocaleMessageInterpolator(MessageInterpolator target, Locale locale) {
        this.target = target;
        this.locale = locale;
    }

    public LocaleMessageInterpolator() {
        locale = Locale.getDefault();
        target = new ParameterMessageInterpolator();
    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return target.interpolate(messageTemplate, context, this.locale);
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        return target.interpolate(messageTemplate, context, this.locale);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
