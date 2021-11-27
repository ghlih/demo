package cn.hwali.ex.mvc.handler;

import cn.hwali.ex.constant.ExConstant;
import cn.hwali.ex.entity.Admin;
import cn.hwali.ex.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Hwa Li
 * @date 2021/10/15
 */
@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin/do/login.html")
    public String doLogin(
            @RequestParam("loginAcct") String loginAcct,
            @RequestParam("userPswd") String userPswd,
            HttpSession session
    ) {
        //调用Service执行登录检查
        //这个方法如果能够返回admin对象说明登录成功，如果账号、密码不正确则会抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
        //将登录成功返回的admin对象存入Session域
        session.setAttribute(ExConstant.ATTR_NAME_LOGIN_ADMIN, admin);
        //return "admin-main";
        //为了避免跳转到后台主页面再刷新浏览器导致重复提交登录表单，重定向到目标页面，
        //handler方法需要做相应修改
        return "redirect:/admin/to/main/page.html";
    }

    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session) {
        //强制Session失效
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    @PreAuthorize("hasAuthority('user:get')")
    @RequestMapping("/admin/get/page.html")
    public String getAdminPage(
            //注意：页面上可能不提供关键词，要进行适配
            //在@RequestParam注解中设置defaultValue属性为空字符串
            //表示浏览器不提供关键词时，keyword变量赋值为空字符串
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            //浏览器未提供pageNum时，默认前往第一页
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            //浏览器未提供pageSize时，默认每页显示5条记录
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            ModelMap modelMap
    ) {
        //查询到分页数据
        PageInfo<Admin> pageInfo = adminService.getAdminPage(keyword, pageNum, pageSize);
        //将分页数据存入模型
        modelMap.addAttribute(ExConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        modelMap.addAttribute("keyword",keyword);
        return "admin-page";
    }

    @PreAuthorize("hasAuthority('user:add')")
    @RequestMapping("/admin/save.html")
    public String saveAdmin(Admin admin) {
        //执行保存
        adminService.saveAdmin(admin);
        //重定向到分页页面，使用重定向是为了避免刷新浏览器而重复提交表单
        return "redirect:/admin/get/page.html?pageNum=" + Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId") Integer adminId, ModelMap modelMap) {
        //1.根据id（主键）查询待更新的Admin对象
        Admin admin = adminService.getAdminById(adminId);
        //2.将Admin对象存入模型
        modelMap.addAttribute("admin", admin);
        return "admin-edit";
    }

    @PreAuthorize("hasAuthority('user:update')")
    @RequestMapping("/admin/update.html")
    public String update(Admin admin,
                         @RequestParam("pageNum") Integer pageNum,
                         @RequestParam("keyword") String keyword) throws UnsupportedEncodingException {
        adminService.update(admin);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" +  URLEncoder.encode(keyword,"UTF-8");
    }

    @PreAuthorize("hasAuthority('user:delete')")
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(@PathVariable("adminId") Integer adminId,
                         @PathVariable("pageNum") Integer pageNum,
                         @PathVariable("keyword") String keyword) throws UnsupportedEncodingException {
        adminService.remove(adminId);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" +   URLEncoder.encode(keyword,"UTF-8");
    }
}