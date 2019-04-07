package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category> {
    /**
     * 根据品牌id查询商品分类
     * @param id
     * @return
     */
    @Select("select * from tb_category where id in (select category_id from tb_category_brand where brand_id=#{id})")
    List<Category> findCategoryByBrandId(Long id);
}
