package com.fh.shop.area.controller;

import com.fh.shop.area.biz.AreaService;
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
@RequestMapping("/api/areas")
@Api(tags = "地区接口")
public class AreaController {

    @Autowired
    private AreaService areaService;
      @GetMapping

      @ApiOperation("地区查询")
    public ServerResponse findChrds(Long id){
        return areaService.findChrds(id);
    }
}
