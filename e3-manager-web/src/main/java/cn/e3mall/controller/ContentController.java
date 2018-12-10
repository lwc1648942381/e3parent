package cn.e3mall.controller;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
import com.e3mall.common.utils.pojo.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容管理Controller
 */
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/query/list")
    @ResponseBody
    public List<TbContent> getContentList(Long categoryId){
        List<TbContent> contentList = contentService.getContentList(categoryId);
        return contentList;
    }

    @RequestMapping("/content/save")
    @ResponseBody
    public E3Result addContent(TbContent tbContent){
        E3Result e3Result = contentService.addContent(tbContent);
        return e3Result;
    }

    @RequestMapping("/rest/content/edit")
    @ResponseBody
    public E3Result updContent(TbContent tbContent){
        E3Result e3Result = contentService.updContent(tbContent);
        return e3Result;
    }

    @RequestMapping("/content/delete")
    @ResponseBody
    public E3Result delContent(Long[] ids){
        E3Result e3Result = contentService.delContent(ids);
        return e3Result;
    }
}
