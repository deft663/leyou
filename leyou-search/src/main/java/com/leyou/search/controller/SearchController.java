package com.leyou.search.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.api.CategoryApi;
import com.leyou.item.pojo.Category;
import com.leyou.search.api.CategoryApiClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhang
 * @date 2019年04月18日 21:08
 */
@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @PostMapping("page")
    public ResponseEntity<SearchResult<Goods>> getGoodsByPage(@RequestBody SearchRequest searchRequest){
        return ResponseEntity.ok(searchService.findPage(searchRequest));
    }
}
