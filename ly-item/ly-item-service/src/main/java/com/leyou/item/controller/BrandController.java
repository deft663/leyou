package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key) {
         PageResult<Brand> result = this.brandService.queryBrandByPageAndSort(page,rows,sortBy,desc, key);
        if (result == null || result.getItems().size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
    @GetMapping("findByName")
    public ResponseEntity<List<Brand>> queryBrandByName(
            @RequestParam(value = "name", required = true) String name) {
            List<Brand> result = this.brandService.queryBrandByName(name);
        /*if (result == null || result.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }*/
        return ResponseEntity.ok(result);
    }
    @PostMapping
    public ResponseEntity add(@RequestParam("cids") List<Long> cids,
             Brand brand) {
                this.brandService.add( brand,cids);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    @GetMapping("/bid/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Long id){
        Brand brand = brandService.findBrandById(id);
        return new ResponseEntity<Brand>(brand,HttpStatus.OK);
    }@GetMapping("/cid/{id}")
    public ResponseEntity<List<Brand>> getBrandByCategoryId(@PathVariable Long id){
         List<Brand> list = brandService.findBrandByCategoryId(id);
        return new ResponseEntity<List<Brand>>(list,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity editBrand(
            @RequestParam("cids") List<Long> cids,
            Brand brand){
        try {
            this.brandService.editBrand(brand,cids);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/del/{id}")
    public ResponseEntity delBrand(@PathVariable Long id){
        brandService.delBrandById(id);
        return ResponseEntity.ok().build();
    }
}
