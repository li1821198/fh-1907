package com.fh.shop.category.biz;


import com.alibaba.fastjson.JSONObject;
import com.fh.shop.category.mapper.CategoryMapper;
import com.fh.shop.category.po.Category;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public ServerResponse queryCategoryList() {
     /*   if(redisTemplate.hasKey(RedisKeyConstant.Key_CATEGORY)){

            return (List<Category>)redisTemplate.opsForValue().get(RedisKeyConstant.Key_CATEGORY);
        }*/

        String categoryList = RedisUtil.get("categoryList");
        if (StringUtils.isNotEmpty(categoryList)){
            List<Category> categorie = JSONObject.parseArray(categoryList, Category.class);
            return ServerResponse.success(categorie);
        }
        List<Category> categoryList1 = categoryMapper.selectList(null);
        String categoryJson = JSONObject.toJSONString(categoryList1);
        RedisUtil.set("categoryList",categoryJson);


        return ServerResponse.success(categoryList1);


    }


}
