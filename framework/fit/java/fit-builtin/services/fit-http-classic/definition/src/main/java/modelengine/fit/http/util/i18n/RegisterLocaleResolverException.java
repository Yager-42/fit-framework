/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package modelengine.fit.http.util.i18n;

import modelengine.fit.http.server.HttpServerException;

/**
 * 表示注册 {@link LocaleResolver} 时出现的异常。
 *
 * @author 阮睿
 * @since 2025-09-07
 */
public class RegisterLocaleResolverException extends HttpServerException {
    /**
     * 通过异常消息来实例化 {@link RegisterLocaleResolverException}。
     *
     * @param message 表示异常消息的 {@link String}。
     */
    public RegisterLocaleResolverException(String message) {
        super(message);
    }

    /**
     * 通过异常消息和异常原因来实例化 {@link RegisterLocaleResolverException}。
     *
     * @param message 表示异常消息的 {@link String}。
     * @param cause 表示异常原因的 {@link Throwable}。
     */
    public RegisterLocaleResolverException(String message, Throwable cause) {
        super(message, cause);
    }
}
