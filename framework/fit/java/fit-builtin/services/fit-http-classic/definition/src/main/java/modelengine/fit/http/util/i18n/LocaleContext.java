/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package modelengine.fit.http.util.i18n;

import java.util.Locale;

/**
 * 地区上下文，包含地区信息
 *
 * @author fit-framework
 * @since 2025-01-01
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
