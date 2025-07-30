/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/
package modelengine.fit.http.util.i18n;

import modelengine.fit.http.server.HttpClassicServerRequest;
import modelengine.fit.http.server.HttpClassicServerResponse;

import java.util.Locale;

/**
 * 地区解析器接口，用于从HTTP请求中解析用户的地区设置
 *
 * @author fit-framework
 * @since 2025-01-01
 */
public interface LocaleResolver {

    /**
     * 解析用户的地区设置
     *
     * @param request HTTP请求 {@link HttpClassicServerRequest}。
     * @return 解析出来的地区 {@link Locale}。
     */
    Locale resolveLocale(HttpClassicServerRequest request);

    /**
     * 设置地区到响应中
     *
     * @param response HTTP响应 {@link HttpClassicServerResponse}。
     * @param locale 要设置的地区 {@link Locale}。
     */
    void setLocale(HttpClassicServerResponse response, Locale locale);
}
