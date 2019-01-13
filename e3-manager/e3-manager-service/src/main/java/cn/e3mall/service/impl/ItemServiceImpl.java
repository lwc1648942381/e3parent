package cn.e3mall.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.jedis.JedisClientPool;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemDescExample;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.pojo.E3Result;
import cn.e3mall.common.utils.pojo.EasyUIDataGridResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private JedisClient jedisClient;

    @Resource
    private Destination topicDestination;

    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;

    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE;

    @Override
    public TbItem getItemById(long itemId) {
        //查询缓存
        try{
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if(StringUtils.isNoneBlank(json)){
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //缓存中没有
       //根据主键查询
        //TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        //设置查询条件
        TbItemExample example=new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> tbItems =itemMapper.selectByExample(example);
        if(tbItems!=null&&tbItems.size()>0){
            try {
                jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(tbItems.get(0)));
                //设置过期时间
                jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE",ITEM_CACHE_EXPIRE);
            }catch (Exception e){
                e.printStackTrace();
            }
            return tbItems.get(0);
        }
        return null;
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

    @Override
    public E3Result addItem(TbItem item, String desc) {
        //生成商品id
        final long itemId = IDUtils.genItemId();
        //补全item属性
        item.setId(itemId);
        //1-正常,2-下架,3-删除
        item.setStatus((byte)1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        //向商品表插入数据
        itemMapper.insert(item);
        //创建一个商品描述表对应的pojo对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        //补全属性
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        //向商品表述表插入数据
        itemDescMapper.insert(tbItemDesc);
        //发送商品添加信息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage=session.createTextMessage(itemId+"");
                return textMessage;
            }
        });
        //返回成功
        return E3Result.ok();
    }

    @Override
    public E3Result delItem(Long[] ids) {
        for(int i=0;i<ids.length;i++){
            itemMapper.deleteByPrimaryKey(ids[i]);
            itemDescMapper.deleteByPrimaryKey(ids[i]);
        }
        return E3Result.ok();
    }

    @Override
    public E3Result getItemDesc(Long id) {
        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(id);
        return new E3Result(tbItemDesc);
    }

    @Override
    public E3Result updItem(TbItem item, String desc) {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(item.getId());
        itemMapper.updateByExampleSelective(item,example);

        TbItemDescExample tbItemDescExample = new TbItemDescExample();
        TbItemDescExample.Criteria criteria1 = tbItemDescExample.createCriteria();
        criteria1.andItemIdEqualTo(item.getId());
        TbItemDesc tbItemDesc=new TbItemDesc();
        tbItemDesc.setItemDesc(desc);
        itemDescMapper.updateByExampleSelective(tbItemDesc,tbItemDescExample);
        return E3Result.ok();
    }

    /**
     * 上架商品
     * @param ids
     * @return
     */
    @Override
    public E3Result reshelf(Long[] ids) {
        for(int i=0;i<ids.length;i++){
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andIdEqualTo(ids[i]);
            TbItem tbItem = new TbItem();
            tbItem.setStatus((byte) 1);
            itemMapper.updateByExampleSelective(tbItem,example);
        }
        return E3Result.ok();
    }

    /**
     * 下架商品
     * @param ids
     * @return
     */
    @Override
    public E3Result instock(Long[] ids) {
        for(int i=0;i<ids.length;i++){
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andIdEqualTo(ids[i]);
            TbItem tbItem = new TbItem();
            tbItem.setStatus((byte) 2);
            itemMapper.updateByExampleSelective(tbItem,example);
        }
        return E3Result.ok();
    }

    @Override
    public TbItemDesc getItemDescById(long itemId) {
        //查询缓存
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if(StringUtils.isNoneBlank(json)){
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return tbItemDesc;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        if(tbItemDesc!=null&&!"".equals(tbItemDesc)){
            try{
                jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":DESC",tbItemDesc.getItemDesc());
                jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":DESC",ITEM_CACHE_EXPIRE);
                return tbItemDesc;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  null;
    }
}
