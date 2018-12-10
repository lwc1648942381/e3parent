package cn.e3mall.content.service.impl;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.common.utils.pojo.E3Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("CONTENT_LIST")
    private String CONTENT_LIST;

    @Override
    public List<TbContent> getContentList(Long categoryId) {
        //查询缓存
        try {
        //如果缓存中有直接响应结果
            String content_list = jedisClient.hget("CONTENT_LIST", categoryId + "");
            if(StringUtils.isNoneBlank(content_list)){
                List<TbContent> tbContents  = JsonUtils.jsonToList(content_list, TbContent.class);
                return tbContents;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        //如果没有查询数据库
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> tbContents = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);
        //把结果添加到缓存
        try {
            jedisClient.hset(CONTENT_LIST,categoryId+"", JsonUtils.objectToJson(tbContents));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return tbContents;
    }

    @Override
    public E3Result addContent(TbContent tbContent) {
        tbContent.setUpdated(new Date());
        tbContent.setCreated(new Date());
       tbContentMapper.insert(tbContent);
       //缓存同步，删除缓存中的数据
        jedisClient.hdel("CONTENT_LIST",tbContent.getCategoryId().toString());
        return E3Result.ok();
    }

    @Override
    public E3Result updContent(TbContent tbContent) {
        tbContentMapper.updateByPrimaryKeySelective(tbContent);
        jedisClient.hdel("CONTENT_LIST",tbContent.getCategoryId().toString());
        return E3Result.ok();
    }

    @Override
    public E3Result delContent(Long[] ids) {
        for(int i=0;i<ids.length;i++) {
            TbContentExample tbContentExample = new TbContentExample();
            TbContentExample.Criteria criteria = tbContentExample.createCriteria();
            criteria.andIdEqualTo(ids[i]);
            List<TbContent> tbContents = tbContentMapper.selectByExample(tbContentExample);
            tbContentMapper.deleteByPrimaryKey(ids[i]);
            for (int j=0;j<tbContents.size();j++){
                jedisClient.hdel("CONTENT_LIST",tbContents.get(j).getCategoryId().toString());
            }
        }
        return E3Result.ok();
    }
}
