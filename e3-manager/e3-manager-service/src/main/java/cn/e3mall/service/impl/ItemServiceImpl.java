package cn.e3mall.service.impl;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import com.e3mall.common.pojo.EasyUIDataGridResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public TbItem getItemById(long itemId) {
//        //根据主键查询
//        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        //设置查询条件
        TbItemExample example=new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> tbItems =itemMapper.selectByExample(example);
        return tbItems!=null&&tbItems.size()>0?tbItems.get(0):null;
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page,rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> tbItems = itemMapper.selectByExample(example);
        //创建返回值对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(tbItems);
        //取分页结果
        PageInfo<TbItem> pageInfo=new PageInfo<>(tbItems);
        //取总记录数
        long total=pageInfo.getTotal();
        result.setTotal(total);
        return result;
    }
}
