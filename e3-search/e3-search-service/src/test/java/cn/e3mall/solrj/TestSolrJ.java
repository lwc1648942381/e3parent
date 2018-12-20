package cn.e3mall.solrj;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {
    @Test
    public void addDocument() throws Exception{
        //创建一个SolrServer对象，创建一个连接，参数是solr服务的url
         SolrServer solrServer=new HttpSolrServer("http://192.168.25.162:8080/solr/collection1");
        //创建一个文档对象solrInputDocument
        SolrInputDocument solrInputFields = new SolrInputDocument();
        //向文档对象中添加域，文档中必须包含一个id域，所有的域的名称必须在scheme.xml中定义
        solrInputFields.addField("id","doc01");
        solrInputFields.addField("item_title","测试商品01");
        solrInputFields.addField("item_price",1000);
        //把文档写入索引库
        solrServer.add(solrInputFields);
        //提交
        solrServer.commit();
    }

    @Test
    public void deleteDocument()throws Exception{
        SolrServer solrServer=new HttpSolrServer("http://192.168.25.162:8080/solr/collection1");
        solrServer.deleteById("doc01");
        solrServer.commit();
    }
}
