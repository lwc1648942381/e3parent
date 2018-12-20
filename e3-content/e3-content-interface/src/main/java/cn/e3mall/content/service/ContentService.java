package cn.e3mall.content.service;

import cn.e3mall.pojo.TbContent;
import cn.e3mall.common.utils.pojo.E3Result;

import java.util.List;

/**
 *
 * 内容管理
 */
public interface ContentService {
  List<TbContent> getContentList(Long categoryId);
  E3Result addContent(TbContent tbContent);
  E3Result updContent(TbContent tbContent);
  E3Result delContent(Long[] ids);


}
