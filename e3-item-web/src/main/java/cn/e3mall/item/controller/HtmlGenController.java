package cn.e3mall.item.controller;

import com.sun.javafx.collections.MappingChange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成静态页面测试Controller
 */
@Controller
public class HtmlGenController {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping("/genhtml")
    @ResponseBody
    public String genHtml() throws Exception {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //加载模板
        Template template = configuration.getTemplate("hello.ftl");
        //创建一个数据集
        Map date=new HashMap();
        date.put("hello",123456);
        //指定文件输出路径及文件名
        Writer writer=new FileWriter(new File("D:\\api\\freemarker\\hello2.txt"));
        //输出文件
        template.process(date,writer);
        //关闭流
        writer.close();
        return "ok";
    }

}


