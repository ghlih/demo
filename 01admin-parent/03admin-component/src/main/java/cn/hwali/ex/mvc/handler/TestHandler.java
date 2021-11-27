package cn.hwali.ex.mvc.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Lihua
 * @create 10/14/2021 14:58
 */
@Controller
public class TestHandler {

    @RequestMapping("/test.html")
    public String toTest(){
        return "target";
    }

    /**
     * 1.开启SpringMVC的注解驱动
     * <mvc:annotation-driven/>
     * 2.必须有jackon依赖
     *       jackson-core
     *       jackson-databind
     * 3.扩展名需要和实际返回的数据格式一致
     *   响应体返回JSON
     *   请求扩展名*.json
     *   web.xml中DispatcherServlet必须映射*.json扩展名
     * @return
     */
    @ResponseBody
    @RequestMapping("/get/emp/by/ajax")
    public Employee getEmployeeAjax(){

        return new Employee(1,"hwali",100.0f);
    }
    @RequestMapping("send/array/one.html")
    public void testSendArrayOne(@RequestParam("empidArray") Integer[] empidArray){

        for (Integer id: empidArray){
            System.out.println(id);
        }

    }

    @ResponseBody
    @RequestMapping("send/array.json")
    public String testSendArray(@RequestBody Integer[] empidArray){

        for (Integer id: empidArray){
            System.out.println(id);
        }

        return "success";
    }

    private static class Employee{

        Integer stuId;
        String name;
        Float score;

        public Employee() {
        }

        public Employee(Integer stuId, String name, Float score) {
            this.stuId = stuId;
            this.name = name;
            this.score = score;
        }
    }
}
