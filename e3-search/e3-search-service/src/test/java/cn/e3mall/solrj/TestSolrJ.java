package cn.e3mall.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

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

    @Test
    public void queryIndex() throws  Exception{
        SolrServer solrServer=new HttpSolrServer("http://192.168.25.162:8080/solr/collection1");
        SolrQuery solrQuery=new SolrQuery();
        solrQuery.setQuery("*:*");
        QueryResponse query = solrServer.query(solrQuery);
        SolrDocumentList results = query.getResults();
        System.out.println("查询结果总记录数:"+results.getNumFound());
        for (SolrDocument rls:results) {
            System.out.println(rls.get("id"));
            System.out.println(rls.get("item_title"));
            System.out.println(rls.get("item_sell_point"));
            System.out.println(rls.get("item_price"));
            System.out.println(rls.get("item_image"));
            System.out.println(rls.get("item_category_name"));
        }
    }

    @Test
    public void queryIndexFuza() throws  Exception{
        SolrServer solrServer=new HttpSolrServer("http://192.168.25.162:8080/solr/collection1");
        SolrQuery solrQuery=new SolrQuery();
        solrQuery.setQuery("手机");
        solrQuery.setStart(0);
        solrQuery.setRows(20);
        solrQuery.set("df","item_title");
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<em>");
        solrQuery.setHighlightSimplePost("</em>");
        QueryResponse query = solrServer.query(solrQuery);
        SolrDocumentList results = query.getResults();
        System.out.println("查询结果总记录数:"+results.getNumFound());
        //取高亮显示
        Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
        for (SolrDocument rls:results) {
            System.out.println(rls.get("id"));
            List<String> list = highlighting.get(rls.get("id")).get("item_title");
            String title="";
            if(list!=null&&list.size()>0){
                title=list.get(0);
            }else {
                title=(String) rls.get("item_title");
            }
            System.out.println(title);
            System.out.println(rls.get("item_sell_point"));
            System.out.println(rls.get("item_price"));
            System.out.println(rls.get("item_image"));
            System.out.println(rls.get("item_category_name"));
        }
    }
}
