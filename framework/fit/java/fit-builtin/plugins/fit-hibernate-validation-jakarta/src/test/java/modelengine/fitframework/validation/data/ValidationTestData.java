/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2024 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

package modelengine.fitframework.validation.data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 测试用的复杂验证数据类。
 *
 * @since 2024-09-27
 */
public class ValidationTestData {
    
    // 分组接口定义
    public interface BasicGroup {}
    public interface AdvancedGroup {}
    public interface StudentGroup {}
    public interface TeacherGroup {}
    
    @NotBlank(message = "名称不能为空")
    private String name;
    
    @Min(value = 0, message = "年龄必须大于等于0")
    @Max(value = 150, message = "年龄必须小于等于150")
    @Max(value = 200, message = "高级组年龄必须小于等于200", groups = AdvancedGroup.class)
    private Integer age;
    
    @NotBlank(message = "描述不能为空")
    private String description;
    
    @NotBlank(message = "内容不能为空白")
    private String content;
    
    @Size(min = 2, max = 10, message = "标题长度必须在2-10之间")
    private String title;
    
    @DecimalMin(value = "0.0", message = "价格必须大于等于0")
    @DecimalMax(value = "999.99", message = "价格必须小于等于999.99")
    private BigDecimal price;
    
    @Positive(message = "数量必须是正数")
    private Integer quantity;
    
    @PositiveOrZero(message = "库存必须是正数或零")
    private int stock;
    
    @Negative(message = "折扣必须是负数")
    private BigDecimal discount;
    
    @NegativeOrZero(message = "债务必须是负数或零")
    private int debt;
    
    @Digits(integer = 3, fraction = 2, message = "金额格式不正确")
    private BigDecimal amount;
    
    @Past(message = "出生日期必须是过去时间")
    private LocalDate birthDate;
    
    @PastOrPresent(message = "创建时间必须是过去或现在时间")
    private LocalDate createDate;
    
    @Future(message = "到期时间必须是未来时间")
    private LocalDate expireDate;
    
    @FutureOrPresent(message = "更新时间必须是未来或现在时间")
    private LocalDate updateDate;
    
    @Pattern(regexp = "^[a-zA-Z]+$", message = "代码只能包含字母")
    private String code;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @AssertTrue(message = "必须同意条款")
    private Boolean agreed;
    
    @AssertFalse(message = "必须取消订阅")
    private boolean subscribed;
    
    // 构造函数
    public ValidationTestData() {}
    
    public ValidationTestData(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
    
    public ValidationTestData(String name, String description, String content, String title, 
                             int age, BigDecimal price, int quantity, int stock, BigDecimal discount, 
                             int debt, BigDecimal amount, LocalDate birthDate, LocalDate createDate,
                             LocalDate expireDate, LocalDate updateDate, String code, String email,
                             Boolean agreed, boolean subscribed) {
        this.name = name;
        this.description = description;
        this.content = content;
        this.title = title;
        this.age = age;
        this.price = price;
        this.quantity = quantity;
        this.stock = stock;
        this.discount = discount;
        this.debt = debt;
        this.amount = amount;
        this.birthDate = birthDate;
        this.createDate = createDate;
        this.expireDate = expireDate;
        this.updateDate = updateDate;
        this.code = code;
        this.email = email;
        this.agreed = agreed;
        this.subscribed = subscribed;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAgreed() {
        return agreed;
    }

    public void setAgreed(Boolean agreed) {
        this.agreed = agreed;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
    
    @Override
    public String toString() {
        return "ValidationTestData{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", stock=" + stock +
                ", discount=" + discount +
                ", debt=" + debt +
                ", amount=" + amount +
                ", birthDate=" + birthDate +
                ", createDate=" + createDate +
                ", expireDate=" + expireDate +
                ", updateDate=" + updateDate +
                ", code='" + code + '\'' +
                ", email='" + email + '\'' +
                ", agreed=" + agreed +
                ", subscribed=" + subscribed +
                '}';
    }
}
