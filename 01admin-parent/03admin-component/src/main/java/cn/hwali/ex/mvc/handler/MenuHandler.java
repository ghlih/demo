package cn.hwali.ex.mvc.handler;

import cn.hwali.ex.entity.Menu;
import cn.hwali.ex.service.api.MenuService;
import cn.hwali.ex.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hwa Li
 * @date 2021/10/16
 */
@RestController
public class MenuHandler {
    @Autowired
    private MenuService menuService;

    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTreeNew() {
        //1.查询全部的Menu对象
        List<Menu> menuList = menuService.getAll();
        //2.声明一个变量用来存储找到的根节点
        Menu root = null;
        //3.创建Map对象用来存储id和Menu对象的相应关系便于查找父节点
        Map<Integer, Menu> menuMap = new HashMap<>();

        //4.遍历menuList填充menuMap
        for (Menu menu : menuList) {
            Integer id = menu.getId();
            menuMap.put(id, menu);
        }

        //5.再次遍历menuList查找根节点、组装父子节点
        for (Menu menu : menuList) {
            //6.获取当前menu对象的pid属性值
            Integer pid = menu.getPid();
            //7.如果pid为null，判定为根节点
            if (pid == null) {
                root = menu;
                //8.如果当前节点是根节点，那么肯定没有父节点，不必继续执行
                continue;
            }

            //9.如果pid不为null，说明当前节点有父节点，那么可以根据pid到menuMap中查到对应的Menu对象
            Menu father = menuMap.get(pid);
            //10.将当前节点存入父节点的children集合
            father.getChildren().add(menu);
        }
        //经过上面的运算，根节点包含整个树形结构，返回根节点就是返回整个树
        return ResultEntity.successWithData(root);
    }

    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu){
        //Thread.sleep(2000);
        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu){
        menuService.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> removeMenu(@RequestParam("id")Integer id){
        menuService.removeMenu(id);
        return ResultEntity.successWithoutData();
    }
}
