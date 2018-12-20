package cn.e3mall.content.service;


import cn.e3mall.common.utils.pojo.E3Result;
import cn.e3mall.common.utils.pojo.EasyUITreeNode;

import java.util.List;

public interface ContentCategoryService {
    List<EasyUITreeNode> getContentCatList(Long parentId);
    E3Result addContentCategory(Long parentId,String name);
    E3Result delContentCategory(Long id);
    E3Result updContentCategory(Long Id,String name);
}
