package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("spu")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("page")
    public ResponseEntity<PageResult> getGoodsPage(@RequestParam(name = "key",required = false)String key,
                                       @RequestParam(name = "saleable",required = false)Boolean saleable,
                                       @RequestParam(name = "page",defaultValue = "1")Integer page,
                                       @RequestParam(name = "rows",defaultValue = "5") Integer rows){
        PageResult<SpuBo> result= this.goodsService.getGoodsPage(key,saleable,page,rows);
        if (result == null || result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
}
