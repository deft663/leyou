package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhang
 * @date 2019年04月18日 14:04
 */
@RequestMapping("spec")
public interface SpecificationApi {
    @GetMapping("/params")
    List<SpecParam> querySpecnParamsByGroupId(@RequestParam(name = "gid",required = false) Long  gid,
                                              @RequestParam(name = "cid",required = false) Long cid,
                                              @RequestParam(name = "searchable",required = false) Boolean searchable,
                                              @RequestParam(name = "generic",required = false) Boolean generic);
    @GetMapping("groups/{cid}")
    List<SpecGroup> querySpecGroups(@PathVariable("cid") Long cid);
    // 查询规格参数组，及组内参数
    @GetMapping("{cid}")
    List<SpecGroup> querySpecsByCid(@PathVariable("cid") Long cid);
}
