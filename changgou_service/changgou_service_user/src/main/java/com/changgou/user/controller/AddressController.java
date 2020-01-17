package com.changgou.user.controller;
import com.changgou.common.entity.PageResult;
import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.config.TokenDecode;
import com.changgou.user.service.AddressService;
import com.changgou.user.pojo.Address;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;


    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result findAll(){
        List<Address> addressList = addressService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",addressList) ;
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable("id") Integer id){
        Address address = addressService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",address);
    }


    /***
     * 新增数据
     * @param address
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Address address){
        String username = tokenDecode.getUserInfo().get("username");
        List<Address> addressList=addressService.findAddressByUsername(username);
        if(addressList == null || addressList.size() <=0 ){
            address.setIsDefault("1");
        }else{
            address.setIsDefault("0");
        }
        address.setUsername(username);
        addressService.add(address);
        return new Result(true,StatusCode.OK,"添加成功");
    }


    /***
     * 修改数据
     * @param address
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody Address address,@PathVariable("id") Integer id){
        address.setId(id);
        addressService.update(address);
        return new Result(true,StatusCode.OK,"修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable("id") Integer id){
        addressService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/search" )
    public Result findList(@RequestBody Map searchMap){
        List<Address> list = addressService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result findPage(@RequestBody Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<Address> pageList = addressService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }

    @Autowired
    TokenDecode tokenDecode;

    @GetMapping("/list")
    public Result<List<Address>> list() {
        //获取当前登录人名称
        String username = tokenDecode.getUserInfo().get("username");
        //查询登录人相关的收件人信息
        List<Address> addressList = addressService.list(username);
        return R.T("查询",addressList);
    }

    @GetMapping("/findAddressByUsername")
    public Result findAddressByUsername(){
        String username = tokenDecode.getUserInfo().get("username");

        List<Address> addressList=addressService.findAddressByUsername(username);
        return new Result(true,StatusCode.OK,"查询成功",addressList);
    }

    @PutMapping(value="/updateIsDefault/{id}")
    public Result updateIsDefault(@PathVariable("id") Integer id){
        String username = tokenDecode.getUserInfo().get("username");
        List<Address> addressList=addressService.findAddressByUsername(username);
        for (Address address : addressList) {
            if (address.getId()==id){
                address.setIsDefault("1");
                addressService.update(address);
            }else {
                address.setIsDefault("0");
                addressService.update(address);
            }
        }
        return new Result(true,StatusCode.OK,"修改成功");
    }
}
