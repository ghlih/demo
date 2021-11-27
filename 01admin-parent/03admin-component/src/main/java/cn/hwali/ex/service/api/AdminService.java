package cn.hwali.ex.service.api;

import cn.hwali.ex.entity.Admin;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author Lihua
 * @create 10/13/2021 20:15
 */
public interface AdminService {
    Admin getAdminById(Integer id);

    Admin getAdminByLoginAcct(String username);

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    PageInfo<Admin> getAdminPage(String keyword, Integer pageNum, Integer pageSize);

    void saveAdmin(Admin admin);

    void update(Admin admin);

    void remove(Integer adminId);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);
}
