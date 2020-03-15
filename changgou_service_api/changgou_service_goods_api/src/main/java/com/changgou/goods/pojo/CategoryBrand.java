package com.changgou.goods.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/22 17:34
 */
@Table(name = "tb_category_brand")
public class CategoryBrand implements Serializable {
    @Id
    private Integer categoryId;

    @Id
    private Integer brandId;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }
}
