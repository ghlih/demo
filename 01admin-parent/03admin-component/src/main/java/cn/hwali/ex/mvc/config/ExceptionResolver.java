package cn.hwali.ex.mvc.config;

import cn.hwali.ex.constant.ExConstant;
import cn.hwali.ex.exception.LoginAcctAlreadyInUseException;
import cn.hwali.ex.exception.LoginAcctAlreadyInUseForUpdateException;
import cn.hwali.ex.exception.LoginFailedException;
import cn.hwali.ex.util.ExUtil;
import cn.hwali.ex.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 基于注解的异常处理类
 *
 * @author Hwa Li
 * @date 2021/10/15
 */

//表示当前类是一个异常处理的类
@ControllerAdvice
public class ExceptionResolver {
    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveException(
            Exception exception,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName, exception, request, response);
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(
            LoginAcctAlreadyInUseForUpdateException e,
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName, e, req, resp);
    }

    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(
            LoginFailedException e,
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        String viewName = "admin-login";
        return commonResolve(viewName, e, req, resp);
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(
            LoginAcctAlreadyInUseException e,
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        String viewName = "admin-add";
        return commonResolve(viewName, e, req, resp);
    }

    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView resolveMthException(
            ArithmeticException e,
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName, e, req, resp);
    }

    //@ExceptionHandler将一个具体的异常类型与一个方法关联起来
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(
            NullPointerException e,
            HttpServletRequest req,
            HttpServletResponse resp) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName, e, req, resp);
    }


    private ModelAndView commonResolve(
            //异常处理后要去的页面
            String viewName,
            //实际捕获的异常
            Exception e,
            //当前请求对象
            HttpServletRequest req,
            //当前响应对象
            HttpServletResponse resp) throws IOException {
        //1.判断当前请求类型
        boolean judgeResult = ExUtil.judgeRequestType(req);

        //2.如果是Ajax请求
        if (judgeResult) {
            //3.创建ResultEntity对象
            ResultEntity<Object> resultEntity = ResultEntity.failed(e.getMessage());
            //4.创建Gson对象
            Gson gson = new Gson();
            //5.将ReusltEntity对象转换成JSON字符串
            String json = gson.toJson(resultEntity);
            //6.将JSON字符串作为响应体返回给浏览器
            resp.getWriter().write(json);
            //7.由于上面已经通过原生的response对象返回了响应，所以不提供ModelAndView对象
            return null;
        }

        //8.如果不是Ajax请求则创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();
        //9.将Exception对象存入模型
        modelAndView.addObject(ExConstant.ATTR_NAME_EXCEPTION, e);
        //10.设置目标视图名称
        modelAndView.setViewName(viewName);
        //11.返回ModelAndView对象
        return modelAndView;
    }
}

