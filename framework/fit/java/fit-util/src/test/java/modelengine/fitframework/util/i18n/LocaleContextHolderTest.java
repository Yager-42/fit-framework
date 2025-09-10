/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package modelengine.fitframework.util.i18n;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link LocaleContextHolder} 的单元测试。
 *
 * @author 阮睿
 * @since 2025-09-09
 */
@DisplayName("测试 LocaleContextHolder")
public class LocaleContextHolderTest {
    @AfterEach
    void tearDown() {
        LocaleContextHolder.clear();
    }

    @Nested
    @DisplayName("Test method: setLocaleContext and getLocaleContext")
    class TestSetAndGetLocaleContext {
        @Test
        @DisplayName("Given locale context with zh_CN then return the same locale context")
        void givenLocaleContextWithZhCNThenReturnSameLocaleContext() {
            LocaleContext localeContext = new LocaleContext(Locale.SIMPLIFIED_CHINESE);
            LocaleContextHolder.setLocaleContext(localeContext);
            assertThat(LocaleContextHolder.getLocaleContext()).isEqualTo(localeContext);
        }

        @Test
        @DisplayName("Given locale context with en_US then return the same locale context")
        void givenLocaleContextWithEnUSThenReturnSameLocaleContext() {
            LocaleContext localeContext = new LocaleContext(Locale.US);
            LocaleContextHolder.setLocaleContext(localeContext);
            assertThat(LocaleContextHolder.getLocaleContext()).isEqualTo(localeContext);
        }

        @Test
        @DisplayName("Given null locale context then not set and return null")
        void givenNullLocaleContextThenReturnNull() {
            LocaleContextHolder.setLocaleContext(null);
            assertThat(LocaleContextHolder.getLocaleContext()).isNull();
        }
    }

    @Nested
    @DisplayName("Test method: getLocale")
    class TestGetLocale {
        @Test
        @DisplayName("Given locale context with zh_CN then return zh_CN locale")
        void givenLocaleContextWithZhCNThenReturnZhCNLocale() {
            LocaleContext localeContext = new LocaleContext(Locale.SIMPLIFIED_CHINESE);
            LocaleContextHolder.setLocaleContext(localeContext);
            assertThat(LocaleContextHolder.getLocale()).isEqualTo(Locale.SIMPLIFIED_CHINESE);
        }

        @Test
        @DisplayName("Given locale context with en_US then return en_US locale")
        void givenLocaleContextWithEnUSThenReturnEnUSLocale() {
            LocaleContext localeContext = new LocaleContext(Locale.US);
            LocaleContextHolder.setLocaleContext(localeContext);
            assertThat(LocaleContextHolder.getLocale()).isEqualTo(Locale.US);
        }

        @Test
        @DisplayName("Given no locale context then return null")
        void givenNoLocaleContextThenReturnNull() {
            LocaleContextHolder.clear();
            assertThat(LocaleContextHolder.getLocale()).isNull();
        }
    }

    @Nested
    @DisplayName("Test method: clear")
    class TestClear {
        @Test
        @DisplayName("Given existing locale context then clear it")
        void givenExistingLocaleContextThenClearIt() {
            LocaleContext localeContext = new LocaleContext(Locale.SIMPLIFIED_CHINESE);
            LocaleContextHolder.setLocaleContext(localeContext);
            assertThat(LocaleContextHolder.getLocaleContext()).isNotNull();

            LocaleContextHolder.clear();
            assertThat(LocaleContextHolder.getLocaleContext()).isNull();
        }
    }
}