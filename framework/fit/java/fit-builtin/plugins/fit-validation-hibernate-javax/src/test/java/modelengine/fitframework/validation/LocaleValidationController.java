package modelengine.fitframework.validation;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;

import modelengine.fit.http.util.i18n.LocaleResolveFilter;
import modelengine.fitframework.annotation.Bean;
import modelengine.fitframework.annotation.Component;
import modelengine.fitframework.util.LocaleContextHolder;
import modelengine.fitframework.validation.data.Company;
import modelengine.fitframework.validation.data.Employee;
import modelengine.fit.http.annotation.PostMapping;
import modelengine.fit.http.annotation.RequestBody;
import modelengine.fit.http.annotation.RequestMapping;

import java.util.Locale;

/**
 * 地区验证控制器 - 测试ValidationHandler与LocaleContextMessageInterpolator的集成
 *
 * @author 阮睿
 * @since 2025-01-27
 */
@Component
@RequestMapping(path = "/validation/locale", group = "地区验证测试接口")
@Validated
public class LocaleValidationController {

    /**
     * 测试验证消息的地区化 - 使用简单参数
     */
    @PostMapping(path = "/simple", description = "测试简单参数的地区化验证消息")
    public void validateSimpleParam(@RequestBody @Valid Company company) {}

}