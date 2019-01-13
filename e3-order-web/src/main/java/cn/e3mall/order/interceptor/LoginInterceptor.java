package cn.e3mall.order.interceptor;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.common.utils.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private CartService cartService;

    @Value("${SSO_URL}")
    private String SSO_URL;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中取token
        String token = CookieUtils.getCookieValue(request, "token");
        //判断token是否存在
        if(StringUtils.isBlank(token)){
            //token不存在，未登录，跳转到sso的登录页面.用户登录成功后跳转到当前请求url
            response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
            return false;
        }
        //token存在，需要调用sso系统的服务，根据token取用户信息
        E3Result e3Result = tokenService.getUserByToken(token);
        //如果取不到，用户登录过期，需要登录
        if(e3Result.getStatus()!=200){
            //token不存在，未登录，跳转到sso的登录页面.用户登录成功后跳转到当前请求url
            response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
            return false;
        }
        //如果取到了，是登录状态，把用户信息写入request
        TbUser user = (TbUser)e3Result.getData();
        request.setAttribute("user",user);
        //判断cookie中是否有购物车数据,如果有合并到服务端
        String json = CookieUtils.getCookieValue(request, "cart", true);
        if(StringUtils.isNotBlank(json)){
            //合并购物车
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(json, TbItem.class));
        }
        //放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
