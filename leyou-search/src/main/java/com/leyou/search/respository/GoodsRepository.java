package com.leyou.search.respository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author zhang
 * @date 2019年04月18日 13:54
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
