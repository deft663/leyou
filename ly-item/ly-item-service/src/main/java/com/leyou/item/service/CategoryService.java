package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据parentId查询子类目
     * @param pid
     * @return
     */
    public List<Category> queryCategoryListByParentId(Long pid) {
        Category record = new Category();
        record.setParentId(pid);
        return this.categoryMapper.select(record);
    }

    public void addCategory(Category category){
        categoryMapper.insert(category);
    }
    public void deleteCategory(Long id){
        Category category=new Category();
        category.setId(id);
        categoryMapper.delete(category);
    }
    public void modify(Category category){
        categoryMapper.updateByPrimaryKey(category);
    }

    public List<Category> findCategoryByBrandId(Long id) {

        return categoryMapper.findCategoryByBrandId(id);
    }

    public List<String> queryNameByIds(List<Long> longs) {
        return this.categoryMapper.selectByIdList(longs).stream().map(Category::getName).collect(Collectors.toList());
    }

    public List<Category> queryByIds(List<Long> ids) {
        List<Category> list=new ArrayList<>();
        Category category=new Category();
        if(ids.size()>0){
            ids.forEach(e->{
                category.setId(e.longValue());
                Category category1 = categoryMapper.selectOne(category);
                if(category1!=null){
                    list.add(category1);
                }
            });
        }
        return list;
    }

    public List<Category> getCategorybyCid3(Long cid3) {
        Category category = this.categoryMapper.selectByPrimaryKey(cid3);
        Category category1 = this.categoryMapper.selectByPrimaryKey(category.getParentId());
        Category category2 = this.categoryMapper.selectByPrimaryKey(category1.getParentId());
        return Arrays.asList(category2,category1,category);
    }
}
