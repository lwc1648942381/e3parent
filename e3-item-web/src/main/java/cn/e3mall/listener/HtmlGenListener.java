package cn.e3mall.listener;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 监听商品添加消息,生成对应的静态页面
 */
public class HtmlGenListener implements MessageListener {
    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${HTML_GEN_PATH}")
    private String HTML_GEN_PATH;

    @Override
    public void onMessage(Message message) {
        try{
            //创建一个模板，参考jsp
            //从消息中取商品id
            TextMessage textMessage=(TextMessage)message;
            String text = textMessage.getText();
            Long itemId=new Long(text);
            //等待事务提交
            Thread.sleep(1000);
            //根据商品id查询商品基本信息和商品描述
            TbItem tbItem = itemService.getItemById(itemId);
            Item item=new Item(tbItem);
            TbItemDesc itemDesc = itemService.getItemDescById(itemId);
            //创建一个数据集,把商品数据封装
            Map date=new HashMap();
            date.put("item",item);
            date.put("itemDesc",itemDesc);
            //加载模板对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            //创建一个输出流，指定输出的目录和文件名
            Writer writer=new FileWriter(new File(HTML_GEN_PATH+itemId+".html"));
            //生成静态页面
            template.process(date,writer);
            //关闭流
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
