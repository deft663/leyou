package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import com.leyou.item.service.GoodsService;
import com.leyou.item.service.SkuService;
import com.leyou.item.service.SpuDetailService;
import com.leyou.item.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SpuService spuService;
    @Autowired
    private SpuDetailService spuDetailService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private StockMapper stockMapper;
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult> getGoodsPage(@RequestParam(name = "key",required = false)String key,
                                       @RequestParam(name = "saleable",required = false)Boolean saleable,
                                       @RequestParam(name = "page",defaultValue = "1")Integer page,
                                       @RequestParam(name = "rows",defaultValue = "5") Integer rows){
        PageResult<SpuBo> result= this.goodsService.getGoodsPage(key,saleable,page,rows);
        if (result == null ) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
    @PostMapping("goods")
    public ResponseEntity addGoods(@RequestBody Spu spu){
        System.out.println(spu);
        goodsService.addGoods(spu);
        return ResponseEntity.ok().build();
    }
    @PutMapping("goods")
    public ResponseEntity updateGoods(@RequestBody Spu spu){
        System.out.println(spu);
        goodsService.updateGoods(spu);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> getSpuDetailById(@PathVariable Long id){
        SpuDetail spuDetail=this.spuDetailService.getSpuDetailById(id);
        return ResponseEntity.ok(spuDetail);
    }
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> getSkuListBySpuId(@RequestParam(name = "id") Long id){
        List<Sku> skuList=this.skuService.getSkuListBySpuId(id);

        return ResponseEntity.ok(skuList);
    }

}
