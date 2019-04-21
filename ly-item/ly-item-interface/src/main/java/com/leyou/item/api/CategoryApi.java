package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {
    @GetMapping("getId")
    List<String> getCategoryNameListByIdList(@RequestParam("ids")List<Long> ids);
    @GetMapping("get")
    List<Category> getCategoryListByIdList(@RequestParam("ids")List<Long> ids);
}
