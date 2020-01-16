package com.changgou.web.order.controller;

import com.changgou.common.entity.Result;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.feign.AddressFeign;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author LiXiang
 */
@Controller
@RequestMapping("/worder")
public class OrderController {
    @Autowired
    private AddressFeign addressFeign;
    @Autowired
    private CartFeign cartFeign;
    @Autowired
    UserFeign userFeign;

    /**
     * 封装订单数据
     * @param model
     * @return
     */
    @RequestMapping("/ready/order")
    public String readyOrder(Model model) {
        //收件人的地址信息
        List<Address> addressList = addressFeign.list().getData();
        model.addAttribute("address",addressList);
        //收件人的购物车信息
        Map map = cartFeign.list();
        List<OrderItem> orderItemList = (List<OrderItem>) map.get("orderItemList");
        Integer totalMoney = (Integer) map.get("totalMoney");
        Integer totalNum = (Integer) map.get("totalNum");
        model.addAttribute("carts",orderItemList);
        model.addAttribute("totalMoney",totalMoney);
        model.addAttribute("totalNum",totalNum);
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        //默认收件人信息
        for (Address address : addressList) {
            if("1".equals(address.getIsDefault())) {
                //默认收件人
                model.addAttribute("deAddr",address);
                break;
            }
        }
        return "order";
    }

    @Autowired
    OrderFeign orderFeign;

    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody Order order){
        Result result = orderFeign.add(order);
        return result;
    }


    @GetMapping("/toPayPage")
    public String toPayPage(@RequestParam("orderId") String orderId,@RequestParam("payMoney") String payMoney,Model model){
        model.addAttribute("orderId",orderId);
        model.addAttribute("payMoney",payMoney);
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return "pay";
    }

}
