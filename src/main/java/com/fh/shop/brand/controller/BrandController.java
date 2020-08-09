package com.fh.shop.brand.controller;


import com.fh.shop.brand.biz.BrandService;
import com.fh.shop.brand.po.Brand;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@Api(tags = "品牌接口")
public class BrandController {
    @Autowired
    private BrandService brandService;
    private List<Brand> brandList;

    @PostMapping
    @ApiOperation("添加品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="brandName",value="品牌名称",type="string",required =true,paramType = "query"),

    })
    public ServerResponse add(Brand brand){
        return brandService.addBrand(brand);
    }

    @GetMapping
    @ApiOperation("品牌查询")
    public ServerResponse query(){
        try {
            List<Brand> brandList= brandService.queryBrand();
          return   ServerResponse.success(brandList);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error();
        }
    }

    @DeleteMapping("/{brandId}")
    @ApiOperation("删除品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="brandId",value="品牌ID",type="string",required =true,paramType = "path"),

    })
    public ServerResponse delete(@PathVariable("brandId") Long id) {
        return brandService.delete(id);
    }

    @PutMapping
    @ApiOperation("修改品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="brandName",value="品牌名称",type="string",required =true,paramType = "body"),

    })
    public ServerResponse update(@RequestBody Brand brand) {
        return brandService.update(brand);
    }

    @DeleteMapping
    @ApiOperation("批量删除品牌")
    public ServerResponse deleteBatch(String ids) {
        return brandService.deleteBatch(ids);
    }
}
