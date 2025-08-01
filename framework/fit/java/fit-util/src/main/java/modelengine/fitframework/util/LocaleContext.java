/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package modelengine.fitframework.util;

import java.util.Locale;

public class LocaleContext {
    private final Locale locale;

    public LocaleContext(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}