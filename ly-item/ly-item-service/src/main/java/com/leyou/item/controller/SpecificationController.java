package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup> > querySpecificationByCategoryId(@PathVariable("cid") Long id){
        List<SpecGroup> specGroups = this.specificationService.queryByCategoryId(id);
        if (specGroups == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(specGroups);
    }

    @PostMapping("/groups")
    public ResponseEntity addGroup(@RequestBody SpecGroup group){
        this.specificationService.addGroup(group);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/params")
    public ResponseEntity<List<SpecParam> > querySpecnParamsByGroupId(@RequestParam(name = "gid",required = false) Long  gid,@RequestParam(name = "cid",required = false) Long cid){
        List<SpecParam> specParams = this.specificationService.querySpecParamsByGroupId(gid,cid);
        if (specParams == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(specParams);
    }
    @PostMapping("/params")
    public ResponseEntity addSpecnParams(@RequestBody SpecParam param){
        this.specificationService.addSpecParams(param);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/param/{id}")
    public ResponseEntity delSpecParam(@PathVariable Long id){
        specificationService.delSpecParams(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
