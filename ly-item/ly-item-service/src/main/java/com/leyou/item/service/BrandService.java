package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPageAndSort(
            Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // 开始分页
        Page<Object> objects = PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("name", "%" + key + "%")
                    .orEqualTo("letter", key);
        }
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        Page<Brand> pageInfo = (Page<Brand>) brandMapper.selectByExample(example);
        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }

    public List<Brand> queryBrandByName(String name) {
        Example example = new Example(Brand.class);
        example.and().andEqualTo("name",name);
        List<Brand> brands = brandMapper.selectByExample(example);
        return brands;
    }

    public void   add(Brand brand, List<Long> cids) {
        int i = brandMapper.insertSelective(brand);
        for (Long cid : cids) {
            brandMapper.insertCategoryBrand(cid, brand.getId());
        }
    }

    public Brand findBrandById(Long id) {
        Brand brand=new Brand();
        brand.setId(id);
        return  brandMapper.selectOne(brand);
    }

    /**
     * 修改品牌时先删除中间表的关联，重新添加
     * 图片问题
     * @param brand
     * @param cids
     * @return
     */
    public void editBrand(Brand brand, List<Long> cids) {
        brandMapper.updateByPrimaryKey(brand);
        brandMapper.deleteCategoryBrandByBrandId(brand.getId());
        for (Long cid : cids) {
            brandMapper.insertCategoryBrand(cid,brand.getId());
        }
    }

    public void delBrandById(Long id) {
        Brand brand=new Brand();
        brand.setId(id);
        brandMapper.deleteCategoryBrandByBrandId(id);
        brandMapper.delete(brand);

    }
}
