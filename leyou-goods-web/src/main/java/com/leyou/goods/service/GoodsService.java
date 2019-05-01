package com.leyou.goods.service;

import com.leyou.goods.api.BrandClient;
import com.leyou.goods.api.CategoryClient;
import com.leyou.goods.api.GoodsClient;
import com.leyou.goods.api.SpecificationClient;
import com.leyou.item.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhang
 * @date 2019年04月22日 13:38
 */
@Service
public class GoodsService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;

    private static Logger logger= LoggerFactory.getLogger(GoodsService.class);
    public Map<String,Object> loadModel(Long spuId){
        Map<String,Object> map=new HashMap<>();
        //查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //查询spu详情
        SpuDetail spuDetail = goodsClient.getSpuDetailById(spuId);
        //查询skus
        List<Sku> skus = goodsClient.querySkuBySpuId(spuId);
        //查询组以及里面组内参数
        List<SpecGroup> specGroups = specificationClient.querySpecsByCid(spu.getCid3());
        //查询品牌
        Brand brand = brandClient.getBrandById(spu.getBrandId());
        //查询三级分类
        List<Category> categoryList=
                categoryClient.getCategoryListByIdList(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询特有属性
        List<SpecParam> specParams = specificationClient.querySpecnParamsByGroupId(null, spu.getCid3(), null, null);
        //spu详情里面存储的数据格式 ：{"4":["白色","黑色"],"12":["4G"],"13":["128G"]}
        //前台详情页需要遍历特有属性名
        Map specParamMap=new HashMap();
        specParams.forEach(p->{
            specParamMap.put(p.getId(),p.getName());
        });
        map.put("spu",spu);
        map.put("spuDetail",spuDetail);
        map.put("skus",skus);
        map.put("specGroups",specGroups);
        map.put("brand",brand);
        map.put("categoryList",categoryList);
        map.put("specParamMap",specParamMap);
        return  map;
    }
}
