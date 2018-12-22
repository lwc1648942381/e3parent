package cn.e3mall.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {

    @Test
    public void testAddDocument() throws Exception{
        //创建一个集群连接，应该使用CloudSolrServer创建
        CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.162:2181,192.168.25.162:2182,192.168.25.162:2183");
        //zkHost:zookeeper的地址列表

        //设置一个defaultCollection属性.
        cloudSolrServer.setDefaultCollection("collection2");
        //创建一个文档对象
        SolrInputDocument solrInputFields = new SolrInputDocument();
        //向文档中添加域
        solrInputFields.setField("id","solrcloud01");
        solrInputFields.setField("item_title","测试商品01");
        solrInputFields.setField("item_price",123);
        //把文件写入索引库
        cloudSolrServer.add(solrInputFields);
        //提交
        cloudSolrServer.commit();
    }

    @Test
    public void testQueryDocument() throws Exception{
        CloudSolrServer solrServer=new CloudSolrServer("192.168.25.162:2181,192.168.25.162:2182,192.168.25.162:2183");
        solrServer.setDefaultCollection("collection2");
        SolrQuery solrQuery=new SolrQuery();
        solrQuery.setQuery("*:*");
        QueryResponse query = solrServer.query(solrQuery);
        SolrDocumentList results = query.getResults();
        System.out.println("总记录数:"+results.getNumFound());



    }
}
