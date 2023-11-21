package org.ltboys.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.action.ActionResult;
import org.ltboys.dto.ro.LoginRo;
import org.ltboys.dto.ro.RegisterRo;
import org.ltboys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.ltboys.context.utils.TokenUtil.getRequest;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/register")
    public ActionResult register(@RequestBody @Validated RegisterRo ro) throws Exception {

        try {
            JSONObject vo = userService.register(ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询游戏失败");
        }
    }

    /**
     * 用户登录
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public ActionResult login(@RequestBody @Validated LoginRo ro) throws Exception {
        try {
            JSONObject vo = userService.login(ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询游戏失败");
        }
    }

    @PostMapping("/homepage")
    public ActionResult homepage() throws Exception {
        String token = getRequest().getHeader("token");
        try {
            JSONObject vo = userService.homepage(token);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询游戏失败");
        }
    }
}
