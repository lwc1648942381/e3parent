package cn.e3mall.search.service;

import cn.e3mall.common.utils.pojo.SearchResult;

public interface SearchService {

    SearchResult search(String keyword,int page,int rows) throws Exception;
}
