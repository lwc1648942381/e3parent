package cn.e3mall.service;

import com.e3mall.common.utils.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService  {
    List<EasyUITreeNode> getItemCatlist(long parentId);
}
