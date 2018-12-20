package cn.e3mall.controller;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *    图片上传处理 Controller
 */
@Controller
public class PictureController {
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile){
        try {
            //把图片上传到图片服务器
            FastDFSClient fastDFSClient=new FastDFSClient("classpath:conf/client.conf");
            //取得文件扩展名
            String originalFilename = uploadFile.getOriginalFilename();
            String exName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //得到一个图片的地址和文件名
            String url= fastDFSClient.uploadFile(uploadFile.getBytes(), exName);
            //补充为完整的url
            url=IMAGE_SERVER_URL+url;
            //封装到map中返回
            Map result=new HashMap<>();
            result.put("error",0);
            result.put("url",url);
            return JsonUtils.objectToJson(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map result=new HashMap<>();
            result.put("error",1);
            result.put("message","图片上传失败");
            return JsonUtils.objectToJson(result);
        }

    }
}
