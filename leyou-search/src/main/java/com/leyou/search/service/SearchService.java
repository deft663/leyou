package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.api.BrandApiClient;
import com.leyou.search.api.CategoryApiClient;
import com.leyou.search.api.GoodsApiClient;
import com.leyou.search.api.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.respository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.AggregatorBase;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhang
 * @date 2019年04月18日 13:56
 */
@Service
public class SearchService {
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private CategoryApiClient categoryClient;
    @Autowired
    private BrandApiClient brandApiClient;
    @Autowired
    private GoodsApiClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    private ObjectMapper mapper = new ObjectMapper();

    @Transactional
    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();

        // 查询商品分类名称
        List<String> names = this.categoryClient.getCategoryNameListByIdList(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        // 查询sku
        List<Sku> skus = this.goodsClient.querySkuBySpuId(spu.getId());
        // 查询详情
        SpuDetail spuDetail = this.goodsClient.getSpuDetailById(spu.getId());
        // 查询规格参数
        List<SpecParam> params = this.specificationClient.querySpecnParamsByGroupId(null, spu.getCid3(), null, null);

        // 处理sku，仅封装id、价格、标题、图片，并获得价格集合
        List<Long> prices = new ArrayList<>();
        List<Map<String, Object>> skuList = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            skuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            skuList.add(skuMap);
        });

        // 处理规格参数
        Map<String, Object> genericSpecs = mapper.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> specialSpecs = mapper.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, Object>>() {
        });
        // 获取可搜索的规格参数
        Map<String, Object> searchSpec = new HashMap<>();

        // 过滤规格模板，把所有可搜索的信息保存到Map中
        Map<String, Object> specMap = new HashMap<>();
        params.forEach(p -> {
            if (p.getSearching()) {
                if (p.getGeneric()) {
                    String value = genericSpecs.get(p.getId().toString()).toString();
                    if (p.getNumeric()) {
                        value = chooseSegment(value, p);
                    }
                    specMap.put(p.getName(), StringUtils.isBlank(value) ? "其它" : value);
                } else {
                    specMap.put(p.getName(), specialSpecs.get(p.getId().toString()));
                }
            }
        });

        goods.setId(spu.getId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setAll(spu.getTitle() + " " + StringUtils.join(names, " "));
        goods.setPrice(prices);
        goods.setSkus(mapper.writeValueAsString(skuList));
        goods.setSpecs(specMap);
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public SearchResult<Goods> findPage(SearchRequest searchRequest) {
        if (StringUtils.isBlank(searchRequest.getKey())) {
            return null;
        }

        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        QueryBuilder builder =BuildBasicQueryBuild(searchRequest);
        // 1、对key进行全文检索查询
        queryBuilder.withQuery(builder);
        if (StringUtils.isNotBlank(searchRequest.getSortBy())) {
            queryBuilder.withSort(SortBuilders.fieldSort(searchRequest.getSortBy()).order(searchRequest.getDesc() ? SortOrder.ASC : SortOrder.DESC));
        }
        // 2、通过sourceFilter设置返回的结果字段,我们只需要id、skus、subTitle
        queryBuilder.withSourceFilter(new FetchSourceFilter(
                new String[]{"id", "skus", "subTitle"}, null));
        //品牌聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brandList").field("brandId"));
        //分类聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("categoryList").field("cid3"));
        // 3、分页
        // 准备分页参数
        int page = searchRequest.getPage();
        int size = searchRequest.getSize();
        queryBuilder.withPageable(PageRequest.of(page - 1, size));
        AggregatedPage<Goods> goodsPage = (AggregatedPage) goodsRepository.search(queryBuilder.build());
        LongTerms list = (LongTerms) goodsPage.getAggregation("brandList");
        LongTerms list1 = (LongTerms) goodsPage.getAggregation("categoryList");
        //品牌聚合结果
        List<Brand> brandList = getBrandListByLongTerms(list);
        List<Category> categoryList = getCategoryListByLongTerms(list1);
        List<Map<String, Object>> specs = new ArrayList<>();
        if (categoryList!=null&&categoryList.size() == 1) {//只有一个分类，去聚合规格
            specs = getSpecs(categoryList.get(0).getId(), builder);
        }
        long pageSize = goodsPage.getTotalElements() % searchRequest.getSize() == 0 ? goodsPage.getTotalElements() / searchRequest.getSize() : goodsPage.getTotalElements() / searchRequest.getSize() + 1;
        return new SearchResult<Goods>(goodsPage.getTotalElements(), Integer.parseInt(pageSize + ""), goodsPage.getContent(), categoryList, brandList, specs);
    }

    private QueryBuilder BuildBasicQueryBuild(SearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        boolQueryBuilder.must( QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));
        Iterator<Map.Entry<String, String>> iterator = searchRequest.getFilter().entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value = next.getValue();
            if(!key.equals("品牌")&&!key.equals("分类")){
                boolQueryBuilder.filter(QueryBuilders.termQuery("specs."+key+".keyword",value));
            }else if(key.equals("品牌")){
                boolQueryBuilder.filter(QueryBuilders.termQuery("brandId",value));
            }else{
                boolQueryBuilder.filter(QueryBuilders.termQuery("cid3",value));
            }
        }
       return boolQueryBuilder;
    }

    private List<Map<String, Object>> getSpecs(Long cid, QueryBuilder builder) {
        List<SpecParam> specParams = this.specificationClient.querySpecnParamsByGroupId(null, cid, true, null);
        NativeSearchQueryBuilder nativeSearchQuery = new NativeSearchQueryBuilder();
        nativeSearchQuery.withQuery(builder);
        //1.8特性真特么好使
        specParams.stream().forEach(e -> {
            String name = e.getName();
            nativeSearchQuery.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
        });
        List<Map<String, Object>> specs = new ArrayList<>();
        Map<String, Aggregation> stringAggregationMap = this.elasticsearchTemplate.query(nativeSearchQuery.build(), SearchResponse::getAggregations).asMap();
        specParams.forEach(e -> {
            Map map = new HashMap();
            String key = e.getName();
            map.put("k", key);//eg.内存
            StringTerms stringTerms = (StringTerms) stringAggregationMap.get(key);
            List<String> list = stringTerms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString).collect(Collectors.toList());
            map.put("options", list);//4G 8G
            specs.add(map);
        });
        return specs;
    }

    private List<Brand> getBrandListByLongTerms(LongTerms LongTerms) {
        List<Brand> list = new ArrayList<>();
        List<LongTerms.Bucket> buckets = LongTerms.getBuckets();
        buckets.forEach(e -> {
            Brand brand = this.brandApiClient.getBrandById(e.getKeyAsNumber().longValue());
            if (brand != null) {
                list.add(brand);
            }
        });
        return list;
    }

    private List<Category> getCategoryListByLongTerms(LongTerms LongTerms) {
        List<LongTerms.Bucket> buckets = LongTerms.getBuckets();
        List<Long> longs = buckets.stream().map(e -> {
            return e.getKeyAsNumber().longValue();
        }).collect(Collectors.toList());
        if(longs!=null&&longs.size()>0)
        return categoryClient.getCategoryListByIdList(longs);
        return null;
    }
}
