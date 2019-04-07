package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据parentId查询类目
     * @param pid
     * @return
     */
    @RequestMapping("list")
    public ResponseEntity<List<Category>> queryCategoryListByParentId(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
        try {
            if (pid == null || pid.longValue() < 0){
                // pid为null或者小于等于0，响应400
                return ResponseEntity.badRequest().build();
            }
            // 执行查询操作
            List<Category> categoryList = this.categoryService.queryCategoryListByParentId(pid);
            if (CollectionUtils.isEmpty(categoryList)){
                // 返回结果集为空，响应404
                return ResponseEntity.notFound().build();
            }
            // 响应200
            return ResponseEntity.ok(categoryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 响应500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public ResponseEntity addCategory(@RequestBody Category category) {
        try {
            if (StringUtils.isEmpty(category.getName())||StringUtils.isEmpty(category.getParentId())){
                // pid为null或者小于等于0，响应400
                return ResponseEntity.badRequest().build();
            }
            categoryService.addCategory(category);
            // 响应200
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 响应500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    @RequestMapping(value = "del",method = RequestMethod.DELETE)
    public ResponseEntity delCategory(Long pid) {
        try {
            if (pid==0){
                // pid为null或者小于等于0，响应400
                return ResponseEntity.badRequest().build();
            }
            categoryService.deleteCategory(pid);
            // 响应200
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 响应500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    @RequestMapping(value = "modify",method = RequestMethod.PUT)
    public ResponseEntity modifyCategory(@RequestBody Category category) {
        try {
            if (category.getId()==0){
                // pid为null或者小于等于0，响应400
                return ResponseEntity.badRequest().build();
            }
            categoryService.modify(category);
            // 响应200
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 响应500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    @RequestMapping(value = "/bid/{id}",method = RequestMethod.GET)
    public ResponseEntity<List<Category>> findCategoryByBrandId(@PathVariable Long id) {
        try {

            List<Category> list=  categoryService.findCategoryByBrandId(id);
            // 响应200
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 响应500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
