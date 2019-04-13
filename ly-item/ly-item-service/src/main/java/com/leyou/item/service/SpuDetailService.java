package com.leyou.item.service;

import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpuDetailService {
    @Autowired
    private SpuDetailMapper spuDetailMapper;

    public SpuDetail getSpuDetailById(Long id) {
        SpuDetail spuDetail=new SpuDetail();
        spuDetail.setSpuId(id);
        return spuDetailMapper.selectOne(spuDetail);
    }
}
