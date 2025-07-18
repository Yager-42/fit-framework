/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2024 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package modelengine.fitframework.validation.data;

import modelengine.fitframework.annotation.Component;
import modelengine.fitframework.annotation.Fit;
import modelengine.fitframework.log.Logger;
import modelengine.fitframework.validation.Validated;
import org.hibernate.validator.constraints.Range;



import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 表示测试用校验服务。
 *
 * @author 易文渊
 * @since 2024-09-27
 */
@Component
@Validated
public class ValidateService {
    private static final Logger LOG = Logger.get(ValidateService.class);

    @Fit
    private GroupValidateService.StudentValidateService studentValidateService;
    
    @Fit
    private GroupValidateService.TeacherValidateService teacherValidateService;
    
    @Fit
    private GroupValidateService.AdvancedValidateService advancedValidateService;

    /**
     * 测试原始类型。
     *
     * @param num 表示输入的 {@code int}。
     */
    public void foo0(@Positive(message = "必须是正数") int num) {
        LOG.debug("{}", num);
    }

    /**
     * 测试结构体类型。
     *
     * @param employee 表示输入的 {@code Employee}。
     */
    public void foo1(@Valid Employee employee) {
        LOG.debug("{}", employee);
    }

    /**
     * 测试嵌套类型。
     *
     * @param company 表示输入的 {@code Company}。
     */
    public void foo2(@Valid Company company) {
        LOG.debug("{}", company);
    }

    // 新增的校验注解测试方法
    public void testNotNull(@NotNull String value) {}

    public void testNotEmpty(@NotEmpty String value) {}

    public void testNotBlank(@NotBlank String value) {}

    public void testNull(@Null String value) {}

    public void testSize(@Size(min = 2, max = 10) String value) {}

    public void testSizeList(@Size(min = 1, max = 3) List<String> value) {}

    public void testMin(@Min(10) int value) {}

    public void testMax(@Max(100) int value) {}

    public void testDecimalMin(@DecimalMin("10.5") BigDecimal value) {}

    public void testDecimalMax(@DecimalMax("100.5") BigDecimal value) {}

    public void testPositive(@Positive int value) {}

    public void testPositiveOrZero(@PositiveOrZero int value) {}

    public void testNegative(@Negative int value) {}

    public void testNegativeOrZero(@NegativeOrZero int value) {}

    public void testDigits(@Digits(integer = 3, fraction = 2) BigDecimal value) {}

    public void testPast(@Past LocalDate value) {}

    public void testPastOrPresent(@PastOrPresent LocalDate value) {}

    public void testFuture(@Future LocalDate value) {}

    public void testFutureOrPresent(@FutureOrPresent LocalDate value) {}

    public void testPattern(@Pattern(regexp = "^[a-zA-Z]+$") String value) {}

    public void testEmail(@Email String value) {}

    public void testAssertTrue(@AssertTrue boolean value) {}

    public void testAssertFalse(@AssertFalse boolean value) {}

    public void testValidObject(@Valid ValidationTestData data) {}

    // Range注解测试方法
    public void testRange(@Range(min = 10, max = 100, message = "需要在10和100之间") int value) {}

    public void testRangeBigDecimal(@Range(min = 10, max = 100, message = "需要在10和100之间") BigDecimal value) {}

    // 默认分组测试方法
    public void validateEmployee(@Valid Employee employee) {
        LOG.debug("Validating employee: {}", employee);
    }

    public void validateAge(@Positive(message = "必须是正数") int age) {
        LOG.debug("Validating age: {}", age);
    }

    public void validateNameAndAge(@NotBlank String name, @Positive int age) {
        LOG.debug("Validating name: {} and age: {}", name, age);
    }

    // 分组验证代理方法
    public void validateAdvancedGroup(ValidationTestData data) {
        if (advancedValidateService != null) {
            advancedValidateService.validateAdvancedGroup(data);
        }
    }

    public void validateStudentAge(int age) {
        if (studentValidateService != null) {
            studentValidateService.validateStudentAge(age);
        }
    }

    public void validateTeacherAge(int age) {
        if (teacherValidateService != null) {
            teacherValidateService.validateTeacherAge(age);
        }
    }

    // 嵌套测试方法
    public void validateCompany(@Valid Company company) {
        LOG.debug("Validating company: {}", company);
    }

    public void validateEmployeeList(List<@Valid Employee> employees) {
        LOG.debug("Validating employee list: {}", employees);
    }

    public void validateNestedEmployeeList(List<List<@Valid Employee>> nestedList) {
        LOG.debug("Validating nested employee list: {}", nestedList);
    }

    public void validateEmployeeMap(@Valid Map<String, Employee> employeeMap) {
        LOG.debug("Validating employee map: {}", employeeMap);
    }

    public void validateEmployeeDataMap(@Valid Map<Employee, ValidationTestData> map) {
        LOG.debug("Validating employee data map: {}", map);
    }

    public void validateNestedEmployeeDataMap(Map<@Valid Employee, Map<String, @Valid ValidationTestData>> nestedMap) {
        LOG.debug("Validating nested employee data map: {}", nestedMap);
    }

    public void validateEmployeeMapList(List<Map<String, @Valid Employee>> listOfMaps) {
        LOG.debug("Validating employee map list: {}", listOfMaps);
    }

    public void validateEmployeeDataListMap(Map<@Valid Employee, List<@Valid ValidationTestData>> map) {
        LOG.debug("Validating employee data list map: {}", map);
    }

    public void validateCompanyList(@Valid List<Company> companies) {
        LOG.debug("Validating company list: {}", companies);
    }

    // 混合场景测试方法
    public void validateMixed(@Positive int value, @Valid Employee employee) {
        LOG.debug("Validating mixed primitive {} and object {}", value, employee);
    }

    public void validateMixedCollections(@NotNull List<String> list1, @NotEmpty List<String> list2) {
        LOG.debug("Validating mixed collections: {} and {}", list1, list2);
    }
}