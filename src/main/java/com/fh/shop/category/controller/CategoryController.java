package com.fh.shop.category.controller;


import com.fh.shop.annotation.Check;
import com.fh.shop.category.biz.CategoryService;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/category")
@Api(tags = "商品分类接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

        @GetMapping("/queryCategoryList")
        @Check
        @ApiOperation("分类查询")
    public ServerResponse queryCategoryList(){

           return  categoryService.queryCategoryList();


    }

}
