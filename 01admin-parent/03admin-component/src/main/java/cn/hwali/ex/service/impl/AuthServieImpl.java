package cn.hwali.ex.service.impl;

import cn.hwali.ex.entity.Auth;
import cn.hwali.ex.entity.AuthExample;
import cn.hwali.ex.mapper.AuthMapper;
import cn.hwali.ex.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Hwa Li
 * @date 2021/10/16
 */
@Service
public class AuthServieImpl implements AuthService {
    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {
        //1.获取roleId的值
        List<Integer> roleIdList = map.get("roleId");
        Integer roleId = roleIdList.get(0);
        //2.删除旧关联关系数据
        authMapper.deleteOldRelationship(roleId);
        //3.获取authIdList
        List<Integer> authIdList = map.get("authIdArray");
        //4.判断authIdList是否有效
        if (authIdList != null && authIdList.size() > 0) {
            authMapper.insertNewRelationship(roleId,authIdList);
        }
    }

    @Override
    public List<String> getAssignedAuthIdByAdminId(Integer adminId) {
        return authMapper.selectAssignedAuthIdByAdminId(adminId);
    }
}
