package cn.e3mall.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 全局异常处理器
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    private static final Logger logger= LoggerFactory.getLogger(GlobalExceptionResolver.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception e) {
        //打印控制台
        e.printStackTrace();
        //写日志
        logger.debug("测试输出的日志。。。。");
        logger.info("系统发送异常了......0");
        logger.error("系统发送异常",e);
        //发邮件，发短信
        //使用jmail工具包发送邮件
        //显示一个错误页面
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
