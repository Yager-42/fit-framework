/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package modelengine.fit.http.util.i18n;

import modelengine.fit.http.Cookie;
import modelengine.fit.http.server.HttpClassicServerRequest;
import modelengine.fit.http.server.HttpClassicServerResponse;

import java.util.Locale;

/**
 * 默认地区解析器。
 *
 * @author 阮睿
 * @since 2025-08-01
 */
public class DefualtLocaleResolver implements LocaleResolver {
    private String cookieName = DEFAULT_COOKIE_NAME;
    private int cookieMaxAge = DEFAULT_COOKIE_MAX_AGE;
    private String cookieDomain = DEFAULT_COOKIE_DOMAIN;
    private String cookiePath = DEFAULT_COOKIE_PATH;
    private Locale defaultLocale = Locale.getDefault();

    @Override
    public Locale resolveLocale(HttpClassicServerRequest request) {
        // 先解析 Cookie，如果没有则解析 Accept-Language 头
        String newLocale = request.cookies().get(this.cookieName).map(Cookie::value).orElse(null);
        if (newLocale != null) {
            return Locale.forLanguageTag(newLocale);
        }
        String acceptLanguage;
        try {
            acceptLanguage = request.headers().require("Accept-Language");
        } catch (Exception e) {
            acceptLanguage = null;
        }
        if (acceptLanguage != null && !acceptLanguage.trim().isEmpty()) {
            return Locale.forLanguageTag(acceptLanguage);
        }

        return defaultLocale;
    }

    @Override
    public void setLocale(HttpClassicServerResponse response, Locale locale) {
        if (locale != null) {
            response.cookies()
                    .add(Cookie.builder()
                            .name(cookieName)
                            .value(locale.toLanguageTag())
                            .maxAge(cookieMaxAge)
                            .domain(cookieDomain)
                            .path(cookiePath)
                            .build());
        } else {
            response.cookies().add(Cookie.builder().name(cookieName).maxAge(0).build());
        }
    }

    @Override
    public String getName() {
        return "DefualtLocaleResolver";
    }

    @Override
    public String getUrlPattern() {
        return "/**";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    /**
     * 设置存储地区信息的 Cookie 名称。
     *
     * @param cookieName 表示将要设置的 Cookie 名称 {@link String}。
     */
    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    /**
     * 设置存储地区信息的 Cookie 的最大有效期。
     *
     * @param cookieMaxAge 表示将要设置的 Cookie 最大有效期的 {@code int}。
     */
    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    /**
     * 设置存储地区信息的 Cookie 的作用域。
     *
     * @param cookieDomain 存储地区信息的 Cookie 作用域的 {@link String}。
     */
    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    /**
     * 设置存储地区信息的 Cookie 的可见 URL 路径。
     *
     * @param cookiePath 存储地区信息的 Cookie 作用域的 {@link String}。
     */
    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    /**
     * 设置默认地区。
     *
     * @param defaultLocale 表示默认地区的 {@link Locale}。
     */
    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

}
