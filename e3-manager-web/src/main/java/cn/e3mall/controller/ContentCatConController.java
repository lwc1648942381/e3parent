package cn.e3mall.controller;

import cn.e3mall.content.service.ContentCategoryService;
import com.e3mall.common.utils.pojo.E3Result;
import com.e3mall.common.utils.pojo.EasyUITreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类管理Controller
 */
@Controller
public class ContentCatConController {
    @Autowired
    private ContentCategoryService contentCategoryService;
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(@RequestParam(defaultValue ="0",value="id") Long parentId){
        List<EasyUITreeNode> contentCatList = contentCategoryService.getContentCatList(parentId);
        return contentCatList;
    }
    /**
     * 添加分类节点
     */
    @RequestMapping(value = "/content/category/create",method = RequestMethod.POST)
    @ResponseBody
    public E3Result CreateContentCategory(Long parentId,String name){
        E3Result e3Result = contentCategoryService.addContentCategory(parentId, name);
        return e3Result;
    }

    @RequestMapping(value = "/content/category/delete/",method = RequestMethod.POST)
    @ResponseBody
    public E3Result delContentCategory(Long id){
        E3Result e3Result = contentCategoryService.delContentCategory(id);
        return e3Result;
    }

    /**
     * 重命名
     */
    @RequestMapping("/content/category/update")
    @ResponseBody
    public E3Result updContentCategory(Long id,String name){
        E3Result e3Result = contentCategoryService.updContentCategory(id, name);
        return e3Result;
    }


}
