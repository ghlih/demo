package cn.hwali.ex.mvc.config;

import cn.hwali.ex.entity.Admin;
import cn.hwali.ex.entity.Role;
import cn.hwali.ex.service.api.AdminService;
import cn.hwali.ex.service.api.AuthService;
import cn.hwali.ex.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hwa Li
 * @date 2021/10/19
 */
@Component
public class ExUserDetailsService implements UserDetailsService {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据账号名称查询Admin对象
        Admin admin = adminService.getAdminByLoginAcct(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户未找到");
        }
        //2.获取adminId
        Integer adminId = admin.getId();
        //3.根据adminId查询角色信息
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
        //4.根据adminId查询权限信息
        List<String> authNameList = authService.getAssignedAuthIdByAdminId(adminId);
        //5.创建集合对象用力啊存储GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();

        //6.遍历assignedRoleList存入角色信息
        for (Role role : assignedRoleList) {
            //不要忘了加前缀
            String roleName = "ROLE_" + role.getName();
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
            authorities.add(simpleGrantedAuthority);
        }

        //7.遍历authNameList存入权限信息
        for (String authName : authNameList) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authName);
            authorities.add(simpleGrantedAuthority);
        }

        //8.封装securityAdmin对象
        SecurityAdmin securityAdmin = new SecurityAdmin(admin, authorities);
        return securityAdmin;
    }
}
