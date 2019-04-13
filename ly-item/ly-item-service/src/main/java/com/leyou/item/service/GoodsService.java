package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;
    public PageResult<SpuBo> getGoodsPage(String key, Boolean saleable, Integer page, Integer rows) {
        // 1、查询SPU
        // 分页,最多允许查100条
        PageHelper.startPage(page, Math.min(rows, 100));
        Example example=new Example(Spu.class);
        if(StringUtil.isNotEmpty(key)){
            example.and().andLike("title","%"+key+"%");
        }
        if(saleable!=null){
            example.and().andEqualTo("saleable",saleable);
        }
        example.setOrderByClause("last_update_time desc");
        Page<Spu> pageInfo = (Page<Spu>) this.spuMapper.selectByExample(example);
        List<SpuBo> spuBos = pageInfo.getResult().stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            List<String> names = this.categoryService.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(names, "/"));
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());
            return spuBo;
        }).collect(Collectors.toList());

        return new PageResult<SpuBo>(pageInfo.getTotal(),spuBos);
    }
    @Transactional
    public void addGoods(Spu spu) {
        //添加spu
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(new Date());
        spuMapper.insert(spu);
        //添加spu_detail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);
        List<Stock> stockList=new ArrayList<>();
        //添加sku
        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            sku.setSpuId(spu.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(new Date());
            skuMapper.insert(sku);
            Stock stock=new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockList.add(stock);
        }
        //添加stock库存
        skuMapper.insertList(stockList);
    }

    public void updateGoods(Spu spu) {
        //删除stock
        Sku sku=new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skus = this.skuMapper.select(sku);
        skus.stream().map(Sku::getId).collect(Collectors.toList()).forEach(id->{
            this.stockMapper.deleteByPrimaryKey(id);
        });
        //删除sku

        this.skuMapper.delete(sku);

        //修改spu
        this.spuMapper.updateByPrimaryKey(spu);
        //修改spu_detail
        this.spuDetailMapper.updateByPrimaryKey(spu.getSpuDetail());
        //添加sku
        spu.getSkus().forEach(sku1 -> {
            sku1.setSpuId(spu.getId());
            sku1.setEnable(true);
            sku1.setCreateTime(new Date());
            sku1.setLastUpdateTime(new Date());
            this.skuMapper.insert(sku1);
            //添加stock
            Stock stock=new Stock();
            stock.setSkuId(sku1.getId());
            stock.setStock(sku1.getStock());
            this.stockMapper.insert(stock);
        });

    }
}