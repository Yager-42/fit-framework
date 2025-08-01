/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package modelengine.fitframework.util;

import java.util.Locale;

/**
 * 地区上下文。
 *
 * @author 阮睿
 * @since 2025-08-01
 */
public class LocaleContext {
    private final Locale locale;

    public LocaleContext(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}