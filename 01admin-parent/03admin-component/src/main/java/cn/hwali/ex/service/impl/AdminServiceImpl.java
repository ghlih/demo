package cn.hwali.ex.service.impl;

import cn.hwali.ex.constant.ExConstant;
import cn.hwali.ex.entity.Admin;
import cn.hwali.ex.entity.AdminExample;
import cn.hwali.ex.mapper.AdminMapper;
import cn.hwali.ex.service.api.AdminService;
import cn.hwali.ex.exception.LoginAcctAlreadyInUseException;
import cn.hwali.ex.exception.LoginAcctAlreadyInUseForUpdateException;
import cn.hwali.ex.exception.LoginFailedException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Lihua
 * @create 10/13/2021 20:16
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public Admin getAdminById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {

        AdminExample example = new AdminExample();

        example.createCriteria()
                .andLoginAcctEqualTo(username);

        List<Admin> list = adminMapper.selectByExample(example);

        if(list == null || list.size() != 1) {
            return null;
        }

        Admin admin = list.get(0);

        return admin;
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        //1.根据登录账号查询admin对象
        //①创建AdminExample对象
        AdminExample adminExample = new AdminExample();
        //②创建Criteria对象
        AdminExample.Criteria criteria = adminExample.createCriteria();
        //③在Criter对象中封装查询条件
        criteria.andLoginAcctEqualTo(loginAcct);
        //④调用AdminMapper的方法执行查询
        List<Admin> list = adminMapper.selectByExample(adminExample);

        //2.判断Admin对象是否为null
        if (list == null || list.size() == 0) {
            throw new LoginFailedException(ExConstant.MESSAGE_LOGIN_FAILED);
        }
        if (list.size() > 1) {
            throw new RuntimeException(ExConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        Admin admin = list.get(0);
        //3.如果Admin对象为null则抛出异常
        if (admin == null) {
            throw new LoginFailedException(ExConstant.MESSAGE_LOGIN_FAILED);
        }

        //4.如果Admin对象不为null则将数据库密码从Admin对象中取出
        String userPswdDB = admin.getUserPswd();
        //5.将表单提交的明文密码加密
        //String userPswdForm = ExUtil.md5(userPswd);
        if (!passwordEncoder.matches(userPswd, userPswdDB)) {
            throw new LoginFailedException(ExConstant.MESSAGE_LOGIN_FAILED);
        }
        //6.比较密码
    /*    if(!Objects.equals(userPswdForm,userPswdDB)){
            //7.结果不一致抛出异常
            throw new LoginFailedException(ExConstant.MESSAGE_LOGIN_FAILED);
        }*/
        //8.结果一致返回Admin对象
        return admin;
    }

    @Override
    public PageInfo<Admin> getAdminPage(String keyword, Integer pageNum, Integer pageSize) {
        //1.开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        //2.查询Admin数据
        List<Admin> adminList = adminMapper.selectAdminListByKeyword(keyword);
        //辅助代码：打印adminList的全类名
        logger.debug("adminList的全类名：" + adminList.getClass().getName());
        //3.为了方便使用将adminList封装为PageInfo
        PageInfo<Admin> pageInfo = new PageInfo<>(adminList);
        return pageInfo;
    }

    @Override
    public void update(Admin admin) {

        String source = admin.getUserPswd();
        String encoded = passwordEncoder.encode(source);
        //String encoded = Exutil.md5(source);
        admin.setUserPswd(encoded);
        try {
            //“selective”表示有选择地更新，对于null值地字段不更新
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseForUpdateException(ExConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public void remove(Integer adminId) {
      adminMapper.deleteByPrimaryKey(adminId);
      adminMapper.deleteOldRelationship(adminId);
    }

    @Override
    public void saveAdmin(Admin admin) {
        //生成当前的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(new Date());
        admin.setCreateTime(createTime);

        //针对登录密码进行加密
        String source = admin.getUserPswd();
        //String encoded = ExUtil.md5(source);
        String encoded = passwordEncoder.encode(source);
        admin.setUserPswd(encoded);

        //执行保存，如果账号被占用会被抛出异常
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            //检测当前捕获的异常对象，如果是DuplicateKeyException类型说明是账号重复导致的
            if (e instanceof DuplicateKeyException) {
                //抛出自定义的LoginAcctAlreadyInUseException
                throw new LoginAcctAlreadyInUseException(ExConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        //简化操作：先根据根据adminId删除旧的数据，再根据roleIdList保存全部新的数据
        //1.根据adminId删除旧的关联数据关系
        adminMapper.deleteOldRelationship(adminId);
        //2.根据roleIdList和adminIdList保存新的关联关系
        if (roleIdList !=null &&roleIdList.size()>0) {
            adminMapper.insertNewRelationship(adminId,roleIdList);
        }
    }
}
