package com.leyou.item.service;

import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;

    @Autowired
    private BrandMapper brandMapper;
    public List<SpecGroup> queryByCategoryId(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCategoryId(cid);
        return specGroupMapper.select(specGroup);
    }

    public void addGroup(SpecGroup group) {
        System.out.println(group);
        specGroupMapper.insert(group);
    }

    public List<SpecParam> querySpecParamsByGroupId(Long gid, Long cid,Boolean searchable,Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCategoryId(cid);
        specParam.setSearching(searchable);
        specParam.setGeneric(generic);
        return specParamMapper.select(specParam);
    }

    public void addSpecParams(SpecParam param) {
        System.out.println(param);
        specParamMapper.insert(param);
    }

    public void delSpecParams(Long id) {
        System.out.println(id);
        SpecParam specParam = new SpecParam();
        specParam.setId(id);
        specParamMapper.delete(specParam);
    }


    public void updateSpecnParams(SpecParam param) {
        this.specParamMapper.updateByPrimaryKey(param);
    }
}
