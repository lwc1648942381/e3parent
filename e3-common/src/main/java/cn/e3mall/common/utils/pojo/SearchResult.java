package cn.e3mall.common.utils.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }

    private  long recordCount;
    private int totalPages;
    private List<SearchItem> itemList;
}
