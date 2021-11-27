package cn.hwali.ex.service.api;

import cn.hwali.ex.entity.Menu;

import java.util.List;

/**
 * @author Hwa Li
 * @date 2021/10/16
 */
public interface MenuService {
    List<Menu> getAll();

    void saveMenu(Menu menu);

    void updateMenu(Menu menu);

    void removeMenu(Integer id);
}
