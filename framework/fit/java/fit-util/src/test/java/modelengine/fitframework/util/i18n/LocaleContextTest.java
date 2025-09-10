/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package modelengine.fitframework.util.i18n;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link LocaleContext} 的单元测试。
 *
 * @author 阮睿
 * @since 2025-09-09
 */
@DisplayName("测试 LocaleContext")
public class LocaleContextTest {
    @Nested
    @DisplayName("Test method: constructor and getLocale()")
    class TestConstructorAndGetLocale {
        @Test
        @DisplayName("Given locale is zh_CN then return the same locale")
        void givenZhCNThenReturnSameLocale() {
            Locale locale = Locale.SIMPLIFIED_CHINESE;
            LocaleContext localeContext = new LocaleContext(locale);
            assertThat(localeContext.getLocale()).isEqualTo(locale);
        }

        @Test
        @DisplayName("Given locale is en_US then return the same locale")
        void givenEnUSThenReturnSameLocale() {
            Locale locale = Locale.US;
            LocaleContext localeContext = new LocaleContext(locale);
            assertThat(localeContext.getLocale()).isEqualTo(locale);
        }

        @Test
        @DisplayName("Given locale is null then return null")
        void givenNullThenReturnNull() {
            LocaleContext localeContext = new LocaleContext(null);
            assertThat(localeContext.getLocale()).isNull();
        }
    }
}