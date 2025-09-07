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
 * 地区解析器接口，用于从 HTTP 请求中解析用户的地区设置。
 *
 * @author 阮睿
 * @since 2025-08-01
 */
public interface LocaleResolver {
    public static final String DEFAULT_COOKIE_NAME = "locale";
    public static final int DEFAULT_COOKIE_MAX_AGE = 60 * 60 * 24 * 365;
    public static final String DEFAULT_COOKIE_DOMAIN = "/";
    public static final String DEFAULT_COOKIE_PATH = "/";

    /**
     * 解析用户的地区设置。
     *
     * @param request 表示待解析 HTTP 请求的 {@link HttpClassicServerRequest}。
     * @return 表示解析出来地区信息的 {@link Locale}。
     */
    Locale resolveLocale(HttpClassicServerRequest request);

    /**
     * 设置地区到返回响应中。
     *
     * @param response 表示将要设置地区 HTTP 响应的 {@link HttpClassicServerResponse}。
     * @param locale 表示要设置地区的 {@link Locale}。
     */
    void setLocale(HttpClassicServerResponse response, Locale locale);

    /**
     * 获取地区解析器的 URL 模式。
     *
     * @return 获取地区解析器 URL 模式的 {@link String}。
     */
    String getUrlPattern();

    /**
     * 设置地区解析器的 URL 模式。
     *
     * @param urlPattern 表示待设置 URL 模式的 {@link String}。
     */
    void setUrlPattern(String urlPattern);
}
