package com.leyou;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.api.BrandApi;
import com.leyou.item.api.CategoryApi;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.search.LySearchService;
import com.leyou.search.api.BrandApiClient;
import com.leyou.search.api.CategoryApiClient;
import com.leyou.search.api.GoodsApiClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.respository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author zhang
 * @date 2019年04月18日 08:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)
public class SearchClient {
    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private BrandApiClient brandApiClient;
    @Autowired
    private SearchService searchService;
    @Autowired
    private CategoryApiClient categoryApiClient;
    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private GoodsApiClient goodsApiClient;
    public void buildGood(Spu spu) throws JsonProcessingException {
        Goods goods=new Goods();
        goods.setId(spu.getId());

        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        String join = StringUtils.join(categoryApiClient.getCategoryNameListByIdList(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())).toArray(), " ");
        Brand brand = brandApiClient.getBrandById(spu.getBrandId());
        goods.setAll(spu.getSubTitle()+" "+brand.getName() +" "+join);// 标题 品牌 分类
        List priceList=new ArrayList();
        spu.getSkus().forEach(e->{
            priceList.add(e.getPrice());
        });
        goods.setPrice(priceList);

        String s = mapper.writeValueAsString(spu.getSkus());

    }
    @Test
    public void createIndex(){
        // 创建索引
        this.elasticsearchTemplate.createIndex(Goods.class);
        // 配置映射
        this.elasticsearchTemplate.putMapping(Goods.class);
    }
    @Test
    public void loadData(){
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            // 查询分页数据
            PageResult<SpuBo> goodsPage = this.goodsApiClient.getGoodsPage(null, null, page, rows);
            List<SpuBo> spus = goodsPage.getItems();
            size = spus.size();
            // 创建Goods集合
            List<Goods> goodsList = new ArrayList<>();
            // 遍历spu
            for (SpuBo spu : spus) {
                try {
                    Goods goods = this.searchService.buildGoods(spu);
                    goodsList.add(goods);
                } catch (Exception e) {
                    System.out.println(e);
                    break;
                }
            }

            this.goodsRepository.saveAll(goodsList);
            page++;
        } while (size == 100);
    }
}
