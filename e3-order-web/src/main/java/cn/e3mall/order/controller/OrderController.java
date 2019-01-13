package cn.e3mall.order.controller;


import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.pojo.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 订单管理Controller
 */
@Controller
public class OrderController {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/order/order-cart")
    public String showOrderCart(HttpServletRequest request){
        //取用户id
        TbUser user =(TbUser)request.getAttribute("user");
        List<TbItem> cartList = cartService.getCartList(user.getId());
        request.setAttribute("cartList",cartList);
        return "order-cart";
    }

    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, HttpServletRequest request){
        //取用户信息
        TbUser tbUser=(TbUser) request.getAttribute("user");
        //把用户信息添加到orderInfo中
        orderInfo.setUserId(tbUser.getId());
        orderInfo.setBuyerNick(tbUser.getUsername());
        //调用服务生成订单
        E3Result e3Result = orderService.createOrder(orderInfo);
        //如果订单生成成功,需要删除购物车
        if(e3Result.getStatus()==200){
            //清空购物车
            cartService.clearCartItem(tbUser.getId());
        }
        //把订单号传递给页面
        request.setAttribute("orderId",e3Result.getData());
        request.setAttribute("Payment",orderInfo.getPayment());
        return "success";
    }
}
