package cn.e3mall.cart.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.common.utils.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/***
 * 购物车处理Controller
 */
@Controller
public class CartController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CartService cartService;

    @Value("${COOKIE_CART_EXPIRE}")
    private Integer COOKIE_CART_EXPIRE;

    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
                          HttpServletRequest request, HttpServletResponse response){
        //判断用户是否登录
        TbUser user =(TbUser) request.getAttribute("user");
        if(user!=null){
            //保存服务端
            cartService.addCart(user.getId(),itemId,num);
            //返回逻辑视图
         return  "cartSuccess";
        }
        //未登录，写入cookie
        //从cookie中取购物车列表
        List<TbItem> cartlist = getCartListFromCookie(request);
        //判断商品是否在商品列表中
        boolean flag=false;
        for (TbItem tbItem:cartlist) {
            if(tbItem.getId()==itemId.longValue()){
                //找到商品数量相加
                tbItem.setNum(tbItem.getNum()+num);
                flag=true;
                break;
            }
        }
        //如果不存在,根据商品id查询商品信息,得到一个TbItem对象
        if(!flag){
            TbItem tbitem = itemService.getItemById(itemId);
            //设置商品数量
            tbitem.setNum(num);
            //取一张图片
            String image=tbitem.getImage();
            if(StringUtils.isNotBlank(image)){
                tbitem.setImage(image.split(",")[0]);
                //把商品添加到商品列表
                cartlist.add(tbitem);
            }
        }
        //写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartlist),COOKIE_CART_EXPIRE,true);
        //返回添加成功页面
        return "cartSuccess";
    }

    //从cookie中取购物车列表
    private List<TbItem> getCartListFromCookie(HttpServletRequest request){
        String json = CookieUtils.getCookieValue(request, "cart", true);
        if(StringUtils.isBlank(json)){
            return new ArrayList<>();
        }
        //把json转换成list
        List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
        return list;
    }

    /**
     * 展示购物车
     * @param request
     * @return
     */
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request,HttpServletResponse response){
        //从cookie中取购物车列表
        List<TbItem> cartlist = getCartListFromCookie(request);
        //判断用户是否为登录状态
        TbUser user =(TbUser) request.getAttribute("user");
        //如果是登录状态
        if(user!=null){
            //如果不为空，把cookie中的购物车商品和服务端的购物车商品合并
            cartService.mergeCart(user.getId(),cartlist);
            //把cookie中的购物车删除
            CookieUtils.deleteCookie(request,response,"cart");
            //从服务端取购物车列表
            cartlist = cartService.getCartList(user.getId());

        }

        //未登录
        //把列表传递给列表
        request.setAttribute("cartList",cartlist);
        //返回逻辑视图
        return "cart";
    }

    /**
     * 更新购物车商品数量
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
          HttpServletRequest request,HttpServletResponse response){
        //判断用户是否为登录状态
        TbUser user =(TbUser) request.getAttribute("user");
        if(user!=null){
            cartService.updateCartNum(user.getId(),itemId,num);
            return E3Result.ok();
        }
        //从cookie中取购物车列表
        List<TbItem> cartListFromCookie = getCartListFromCookie(request);
        //遍历商品列表找到对应商品
        for (TbItem tbItem:cartListFromCookie) {
            if(tbItem.getId().longValue()==itemId){
                //更新数量
                tbItem.setNum(num);
            }
        }
        //把购物车列表写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartListFromCookie),COOKIE_CART_EXPIRE,true);
        //返回成功
        return E3Result.ok();
    }

    /**
     * 删除购物车商品
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
        //判断用户是否为登录状态
        TbUser user =(TbUser) request.getAttribute("user");
        if(user!=null){
            cartService.deleteCartItem(user.getId(),itemId);
            return "redirect:/cart/cart.html";
        }
        //从cookie中取购物车列表
        List<TbItem> cartListFromCookie = getCartListFromCookie(request);
        //遍历列表。找到要删除的商品
        for(TbItem tbItem:cartListFromCookie){
            if(tbItem.getId().longValue()==itemId){
                cartListFromCookie.remove(tbItem);
                break;
            }
        }
        //把购物车列表写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartListFromCookie),COOKIE_CART_EXPIRE,true);
        //返回逻辑视图
        return "redirect:/cart/cart.html";
    }
}
