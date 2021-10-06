package com.syb.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.syb.entity.User;
import com.syb.service.IUserService;
import com.syb.service.impl.UserServiceImpl;
import com.syb.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
*  该类作为Realm的重写,重写两个类（授权认证）
*
* */
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    IUserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
//        判断该token是否是JwtToken
        return token instanceof JwtToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        JwtToken jwtToken = (JwtToken) token;
        Claims claim = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal());//获取claims
        String userId = claim.getSubject();//获取userid

        User user = userService.getById(userId);
        if(user==null){
            throw new UnknownAccountException("账户不存在");
        }

        if(user.getStatus()==-1){
            throw new LockedAccountException("账户被锁定");
        }

//        把用户的一些基本信息返回给shiro
        AccountProfile profile=new AccountProfile();
        BeanUtil.copyProperties(user,profile);

//        System.out.println("--------------------------");

//        shiro通过一些工具类可以获取到用户的一些信息
        return new SimpleAuthenticationInfo(profile,jwtToken.getPrincipal(),getName());
    }
}
