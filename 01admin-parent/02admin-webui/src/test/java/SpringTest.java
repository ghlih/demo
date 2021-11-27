import cn.hwali.ex.entity.Admin;
import cn.hwali.ex.service.api.AdminService;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.sql.visitor.functions.If;
import com.sun.org.apache.bcel.internal.generic.IFNONNULL;
import com.sun.org.apache.bcel.internal.generic.IFNULL;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.ibatis.transaction.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Lihua
 * @create 10/13/2021 18:50
 */

//创建 Spring 的 Junit 测试类，指定 Spring 的 Junit 提供的运行器类
@RunWith(SpringJUnit4ClassRunner.class)

//加载Spring配置文件的注解
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class SpringTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminService adminService;

    @Test
    public void testDataSource() throws SQLException {
        //1.通过数据源对象获取数据源信息
        Connection conn = dataSource.getConnection();

        //2.打印数据库连接
        System.out.println(conn);
    }

    @Test
    public void testAdminMapperAutoWired() {
        Admin admin = adminService.getAdminById(1);
        //获取日志对象
        Logger logger = LoggerFactory.getLogger(SpringTest.class);

        //按照debug级别打印日志
        logger.debug(admin.toString());
        System.out.println(admin);
    }

    @Test
    public void testDeclarationTransaction() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            //核心操作前：开启事务（关闭自动提交）
            //对应AOP的前置通知
            connection.setAutoCommit(false);


            //核心操作
            adminService.getAdminById(1);

            //核心操作成功：提交事务
            //对应AOP的返回通知
            connection.commit();
        } catch (SQLException e) {

            //核心操作失败：回滚事务
            //对应AOP的异常通知
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {

            //不论成功还是失败，核心操作终归是结束了
            //核心操作不管是怎么结束的，都需要释放数据库连接
            //对应的AOP后置通知
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
