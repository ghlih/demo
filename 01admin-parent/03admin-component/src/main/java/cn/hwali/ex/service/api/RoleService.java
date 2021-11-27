package cn.hwali.ex.service.api;

import cn.hwali.ex.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author Hwa Li
 * @date 2021/10/16
 */
public interface RoleService {
    PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword);

    void saveRole(Role role);

    void updateRole(Role role);

    void removeRole(List<Integer> roleIdList);

    List<Role> getAssignedRole(Integer adminId);

    List<Role> getUnAssignedRole(Integer adminId);
}
