package com.fh.shop.token.controller;

import com.fh.shop.annotation.Check;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.token.biz.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
@Api(tags = "生成Token接口")
public class TokenController {
      @Autowired
    private TokenService tokenService;

      @Check
      @PostMapping("/createToken")
      @ApiOperation("生成token")
      @ApiImplicitParams({
              @ApiImplicitParam(name = "x-token",value = "头信息",type = "string",required = true,paramType = "header")
      })
      public ServerResponse createToken(){
        return tokenService.createToken();
      }

}
