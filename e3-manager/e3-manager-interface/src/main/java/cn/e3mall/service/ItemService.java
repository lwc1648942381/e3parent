package cn.e3mall.service;

import cn.e3mall.pojo.TbItem;
import com.e3mall.common.utils.pojo.EasyUIDataGridResult;

public interface ItemService {
    TbItem getItemById(long itemId);
    EasyUIDataGridResult getItemList(int page,int rows);
}
