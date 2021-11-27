package cn.hwali.ex.mvc.config;

import cn.hwali.ex.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * @author Hwa Li
 * @date 2021/10/19
 */
public class SecurityAdmin extends User {
    //原始的Admin对象，包含Admin对象的全部属性
    private Admin originalAdmin;

    public SecurityAdmin(
            Admin originalAdmin, //传入原始的Admin对象
            //创建角色、权限信息的集合
            List<GrantedAuthority> authorities
    ) {
        //调用父类构造器
        super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
        //给本类的this.originalAdmin赋值
        this.originalAdmin = originalAdmin;
        //将原始Admin对象中的密码擦除
        this.originalAdmin.setUserPswd(null);
    }

    //对外界提供获取原始Admin对象的getXxx()方法
    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
