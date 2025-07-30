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


public class DefualtLocaleResolver implements LocaleResolver{

    public static final String DEFAULT_COOKIE_NAME = "locale";
    public static final int DEFAULT_COOKIE_MAX_AGE = 60 * 60 * 24 * 365;
    public static final String DEFAULT_COOKIE_DOMAIN = "/";
    public static final String DEFAULT_COOKIE_PATH = "/";
    private String cookieName = DEFAULT_COOKIE_NAME;
    private int cookieMaxAge = DEFAULT_COOKIE_MAX_AGE;
    private String cookieDomain = DEFAULT_COOKIE_DOMAIN;
    private String cookiePath = DEFAULT_COOKIE_PATH;
    private Locale defaultLocale;

    @Override
    public Locale resolveLocale(HttpClassicServerRequest request) {
        // 先解析 Cookie，如果没有则使用 Accept-Language 头
        String newLocale = request.cookies().get(this.cookieName).map(Cookie::value).orElse(null);
        if(newLocale != null){
            return Locale.forLanguageTag(newLocale);
        }

        String acceptLanguage = request.headers().require("Accept-Language");
        if (acceptLanguage != null && !acceptLanguage.trim().isEmpty()) {
            return Locale.forLanguageTag(acceptLanguage);
        }

        return defaultLocale;
    }

    @Override
    public void setLocale(HttpClassicServerResponse response, Locale locale) {

        if(locale != null){
            response.cookies().add(Cookie.builder()
                    .name(cookieName)
                    .value(locale.toLanguageTag())
                    .maxAge(cookieMaxAge)
                    .domain(cookieDomain)
                    .path(cookiePath)
                    .build());
        }else{
            response.cookies().add(Cookie.builder()
                    .name(cookieName)
                    .maxAge(0)
                    .build());
        }
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

}
