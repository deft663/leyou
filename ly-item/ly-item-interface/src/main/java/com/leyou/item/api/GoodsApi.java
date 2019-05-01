package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.dto.CartDto;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping()
public interface GoodsApi {

    /**
     * 分页查询商品
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
   PageResult<SpuBo> getGoodsPage(@RequestParam(name = "key",required = false)String key,
                                                   @RequestParam(name = "saleable",required = false)Boolean saleable,
                                                   @RequestParam(name = "page",defaultValue = "1")Integer page,
                                                   @RequestParam(name = "rows",defaultValue = "5") Integer rows);

    /**
     * 根据spu商品id查询详情
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail getSpuDetailById(@PathVariable("id") Long id);

    /**
     * 根据spu的id查询sku
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long id);

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public Spu querySpuById(@PathVariable("id") Long id);

    @GetMapping("sku/{id}")
    Sku querySkuById(@PathVariable("id") Long id);
    @PostMapping("reduceStock")
    ResponseEntity reduceStock(@RequestBody List<CartDto> cartDtos);
}