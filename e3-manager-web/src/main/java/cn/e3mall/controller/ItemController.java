package cn.e3mall.controller;


        import cn.e3mall.pojo.TbItem;
        import cn.e3mall.service.ItemService;
        import cn.e3mall.common.utils.pojo.E3Result;
        import cn.e3mall.common.utils.pojo.EasyUIDataGridResult;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.*;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId){
        TbItem itemById = itemService.getItemById(itemId);
        return itemById;
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page,Integer rows){
        EasyUIDataGridResult itemList = itemService.getItemList(page, rows);
        return itemList;
    }

    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem item,String desc){
        E3Result e3Result = itemService.addItem(item, desc);
        return e3Result;
    }

    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public E3Result delItem(Long[] ids){
        E3Result e3Result = itemService.delItem(ids);
        return e3Result;
    }

    @RequestMapping(value = "/rest/item/query/item/desc/{id}")
    @ResponseBody
    public E3Result getItemDesc(@PathVariable("id") Long id){
        E3Result itemDesc = itemService.getItemDesc(id);
        return itemDesc;
    }
    @RequestMapping("/rest/item/update")
    @ResponseBody
    public E3Result updItem(TbItem item,String desc){
        E3Result e3Result = itemService.updItem(item, desc);
        return e3Result;
    }

    /**
     * 上架
     */
    @RequestMapping("/rest/item/reshelf")
    @ResponseBody
    public E3Result reshelf(Long[] ids){
        E3Result e3Result = itemService.reshelf(ids);
        return e3Result;
    }

    /**
     * 下架
     */
    @RequestMapping("/rest/item/instock")
    @ResponseBody
    public E3Result instock(Long[] ids){
        E3Result instock = itemService.instock(ids);
        return instock;
    }
}
