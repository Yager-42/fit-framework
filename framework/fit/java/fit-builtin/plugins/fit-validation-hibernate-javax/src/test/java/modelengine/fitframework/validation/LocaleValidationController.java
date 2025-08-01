/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package modelengine.fitframework.validation;

import javax.validation.Valid;

import modelengine.fitframework.annotation.Component;
import modelengine.fitframework.validation.data.Company;
import modelengine.fit.http.annotation.PostMapping;
import modelengine.fit.http.annotation.RequestBody;
import modelengine.fit.http.annotation.RequestMapping;

/**
 * 地区验证控制器 - 测试ValidationHandler与LocaleContextMessageInterpolator的集成
 *
 * @author 阮睿
 * @since 2025-08-01
 */
@Component
@RequestMapping(path = "/validation/locale", group = "地区验证测试接口")
@Validated
public class LocaleValidationController {

    /**
     * 测试验证消息的地区化 - 使用简单参数
     *
     * @param company 表示注解验证类 {@link Company}。
     */
    @PostMapping(path = "/simple", description = "测试简单参数的地区化验证消息")
    public void validateSimpleParam(@RequestBody @Valid Company company) {}

}