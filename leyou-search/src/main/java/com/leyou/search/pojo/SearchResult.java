package com.leyou.search.pojo;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;

import java.util.List;
import java.util.Map;

/**
 * @author zhang
 * @date 2019年04月19日 16:00
 */
public class SearchResult<T> extends PageResult<Goods>{

    private List<Category> categories;// 分类过滤条件

    private List<Brand> brands; // 品牌过滤条件

    private List<Map<String,Object>> specs; // 规格参数过滤条件

    public SearchResult(Long total, Integer totalPage, List<Goods> items,
                        List<Category> categories, List<Brand> brands,
                        List<Map<String,Object>> specs) {
        super(total, totalPage.longValue(), items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }
}