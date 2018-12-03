package cn.e3mall.service;

import cn.e3mall.pojo.TbItem;
import com.e3mall.common.utils.pojo.E3Result;
import com.e3mall.common.utils.pojo.EasyUIDataGridResult;

public interface ItemService {
    TbItem getItemById(long itemId);
    EasyUIDataGridResult getItemList(int page,int rows);
    E3Result addItem(TbItem item,String desc);
    E3Result delItem(Long[] ids);
    E3Result getItemDesc(Long id);
    E3Result updItem(TbItem item,String desc);
    E3Result reshelf(Long[] ids);
    E3Result instock(Long[] ids);
}
