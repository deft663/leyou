package com.leyou.item.service;

import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SkuService {
    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;
    public List<Sku> getSkuListBySpuId(Long id) {
        Sku sku=new Sku();
        sku.setSpuId(id);
        List<Sku> skuList = skuMapper.select(sku);
        List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stockList = ids.stream().map(sku_id -> {
            return stockMapper.selectByPrimaryKey(sku_id);
        }).collect(Collectors.toList());
        Map<Long, Integer> map = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skuList=skuList.stream().map(s -> {
            s.setStock(map.get(s.getId()));
            return s;
        }).collect(Collectors.toList());
        return skuList;
    }
}
