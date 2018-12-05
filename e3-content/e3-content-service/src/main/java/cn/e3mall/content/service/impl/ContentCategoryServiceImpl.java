package cn.e3mall.content.service.impl;

import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import com.e3mall.common.utils.pojo.E3Result;
import com.e3mall.common.utils.pojo.EasyUITreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类管理Service
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    @Override
    public List<EasyUITreeNode> getContentCatList(Long parentId) {
        //根据parentId查询子节点列表
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> CatList = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        //转换成EasyUITreeNode的列表
        List<EasyUITreeNode> nodeList=new ArrayList<>();
        for (TbContentCategory tbContentCategory:CatList){
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(tbContentCategory.getId());
            easyUITreeNode.setText(tbContentCategory.getName());
            easyUITreeNode.setState(tbContentCategory.getIsParent()?"closed":"open");
            nodeList.add(easyUITreeNode);
        }
        return nodeList;
    }

    @Override
    public E3Result addContentCategory(Long parentId, String name) {
        //创建一个表对应的pojo、
        TbContentCategory tbContentCategory = new TbContentCategory();
        //设置pojo属性
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
        //1.正常 2.删除
        tbContentCategory.setStatus(1);
        //默认排序是1
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        //插入到数据库
        tbContentCategoryMapper.insert(tbContentCategory);
        //判断父节点的isparent属性，如果不是true改为true
        //根据parentID查询父节点
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){
            parent.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果，返回E3result,包含pojo
        return E3Result.ok(tbContentCategory);
    }

    /**
     * 删除分类
     * @param id
     */
    @Override
    public E3Result delContentCategory(Long id) {
        //查询要删除的分类
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);

                //如果分类是父级
        if(tbContentCategory.getIsParent()){
            TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
            criteria.andParentIdEqualTo(tbContentCategory.getParentId());
            List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
            if(tbContentCategories.size()!=0){
                TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
                parent.setIsParent(true);
                tbContentCategoryMapper.updateByPrimaryKey(parent);
            }else if(tbContentCategories.size()==0){
                TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
                parent.setIsParent(false);
                tbContentCategoryMapper.updateByPrimaryKey(parent);
            }
        }else {
            tbContentCategoryMapper.deleteByPrimaryKey(id);
            TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
            criteria.andParentIdEqualTo(tbContentCategory.getParentId());
            List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
            if(tbContentCategories.size()>0){

            }else {
                TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
                parent.setIsParent(false);
                tbContentCategoryMapper.updateByPrimaryKey(parent);
            }
        }
        return new E3Result();
    }


    /**
     * 重命名
     */
    @Override
    public E3Result updContentCategory(Long id, String name) {
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        tbContentCategory.setName(name);
        tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);
        return new E3Result();
    }



}
