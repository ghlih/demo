package cn.hwali.ex.service.api;

import cn.hwali.ex.entity.Auth;

import java.util.List;
import java.util.Map;

/**
 * @author Hwa Li
 * @date 2021/10/16
 */
public interface AuthService {
    List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

    List<Auth> getAll();

    void saveRoleAuthRelationship(Map<String, List<Integer>> map);

    List<String> getAssignedAuthIdByAdminId(Integer adminId);
}
