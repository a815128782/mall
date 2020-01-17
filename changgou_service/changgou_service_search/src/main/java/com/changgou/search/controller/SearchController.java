package com.changgou.search.controller;

import com.changgou.common.entity.Page;
import com.changgou.common.util.CookieUtil;
import com.changgou.order.feign.CartFeign;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import com.changgou.user.feign.UserFeign;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * @author LiXiang
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;
    @Autowired
    UserFeign userFeign;
    @Autowired
    CartFeign cartFeign;

//    //查询购物车
//    @GetMapping("/list")
//    public String list(Model model) {
//        Map map = cartFeign.list();
//        model.addAttribute("items",map);
//        String username = (String) userFeign.getUsername().getData();
//        model.addAttribute("username",username);
//        return "cart";
//    }

    @GetMapping("/list")
    public String list(@RequestParam Map<String,String> searchMap, Model model, HttpServletRequest request) {
//        String username = (String) userFeign.getUsername().getData();
        Map<String, String> map = CookieUtil.readCookie(request, "username");
        String username = map.get("username");
        model.addAttribute("username",username);
//        int i = 1/0;
        if(searchMap == null ||searchMap.size() <= 0) {
            searchMap.put("keyWords","");
        }
        //特殊符号处理
        if(StringUtils.isEmpty(searchMap.get("sortRule"))){
            searchMap.put("sortRule","ASC");
        }
        this.handleSearchMap(searchMap);
        Map resultMap = searchService.search(searchMap);
        model.addAttribute("result",resultMap);
        model.addAttribute("searchMap",searchMap);

        //封装分页数据并返回
        /**
         * 构造方法中的参数:
         *      参数1:总记录数
         *      参数2:当前页
         *      参数3:每页显示多少条
         */
        Page<SkuInfo> page = new Page<SkuInfo>(
                        Long.parseLong(String.valueOf(resultMap.get("total"))),
                        Integer.parseInt(String.valueOf(resultMap.get("pageNum"))),
                        Page.pageSize);


        model.addAttribute("page",page);

        //拼装Url
        StringBuilder url = new StringBuilder("/api/search/list");
        if(searchMap != null && searchMap.size() > 0) {
            //有查询条件
            url.append("?");
            for (String paramKey : searchMap.keySet()) {
                if(!"sortRule".equals(paramKey)&&!"sortField".equals(paramKey)&&!"pageNum".equals(paramKey)){
                    url.append(paramKey).append("=").append(searchMap.get(paramKey)).append("&");
                }
            }
            String urlString = url.toString();
            //取出路径上最后一个 &
            urlString = urlString.substring(0,urlString.length()-1);
            model.addAttribute("url",urlString);
        }else{
            model.addAttribute("url",url);
        }
        return "search";
    }

    @GetMapping
    @ResponseBody
    public Map search(@RequestParam Map<String,String> searchMap) {
        //特殊符号处理
        this.handleSearchMap(searchMap);
        Map searchResult = searchService.search(searchMap);
        return searchResult;
    }


    /**
     *
     *
     * 规格可能出现 "+" 如  手机的:8+128G版本
     * %2B是 + 号的意思，通过 GET方式传值的时候，+号会被浏览器处理为空，所以需要转换为%2B。
     * @param searchMap
     */
    private void handleSearchMap(Map<String, String> searchMap) {
        Set<Map.Entry<String, String>> entries = searchMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if(entry.getKey().startsWith("spec_")){
                searchMap.put(entry.getKey(),entry.getValue().replace("+","%2B"));
            }
        }
    }
}
