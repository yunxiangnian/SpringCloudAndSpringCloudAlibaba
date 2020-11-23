package cloud.scalibaba.controller;

import com.cloud.springcloud.entities.CommonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.runtime.NewThreadAction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lisw
 * @create 2020/11/11
 */
@RestController
public class ProviderController {
    private static final HashMap<Integer,Object> MAP = new HashMap<Integer,Object>();

    @Value("${server.port}")
    private String serverPort;

    static {
        MAP.put(1,"usr:'1',serial:'1111111111111111111'");
        MAP.put(2,"usr:'1',serial:'2222222222222222222'");
        MAP.put(3,"usr:'1',serial:'3333333333333333333'");
    }

    @GetMapping("/nacos/ribbon/{id}")
    public CommonResult providerRibbon(@PathVariable("id") Integer id){
        return new CommonResult(100, "请求成功" + serverPort,MAP.get(id));
    }
}
