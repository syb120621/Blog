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

@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    IUserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
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

        Claims claim = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal());
        String userId = claim.getSubject();

        User user = userService.getById(userId);
        if(user==null){
            throw new UnknownAccountException("账户不存在");
        }

        if(user.getStatus()==-1){
            throw new LockedAccountException("账户被锁定");
        }

        AccountProfile profile=new AccountProfile();
        BeanUtil.copyProperties(user,profile);

        System.out.println("--------------------------");

        return new SimpleAuthenticationInfo(profile,jwtToken.getPrincipal(),getName());
    }
}
