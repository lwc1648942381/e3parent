package cn.e3mall.item.pojo;

import cn.e3mall.pojo.TbItem;

import java.util.Date;

public class Item extends TbItem {

    public Item(TbItem tbItem){
        this.setId(tbItem.getId());
        this.setTitle(tbItem.getTitle());
        this.setSellPoint(tbItem.getSellPoint());
        this.setPrice(tbItem.getPrice());
        this.setNum(tbItem.getNum());
        this.setBarcode(tbItem.getBarcode());
        this.setImage(tbItem.getImage());
        this.setCid(tbItem.getCid());
        this.setStatus(tbItem.getStatus());
        this.setCreated(tbItem.getCreated());
        this.setUpdated(tbItem.getUpdated());
    }
    public String[] getImages(){
        String image = this.getImage();
        if(image!=null&&!"".equals(image)){
            String[] split = image.split(",");
            return split;
        }
        return null;
    }
}
