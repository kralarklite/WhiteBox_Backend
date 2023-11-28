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
            return ActionResult.failure("注册失败");
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
            return ActionResult.failure("登录失败");
        }
    }

    /**
     * 个人主页
     * @return
     * @throws Exception
     */
    @GetMapping("/homepage")
    public ActionResult homepage() throws Exception {
        //String token = getRequest().getHeader("token");
        try {
            JSONObject vo = userService.homepage(getRequest().getHeader("token"));
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("error");
        }
    }

    /**
     * 查询个人文章
     * @return
     * @throws Exception
     */
    @GetMapping("/myarticles")
    public ActionResult myArticles() throws Exception {
        try {
            JSONObject vo = userService.myArticles(getRequest().getHeader("token"));
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("error");
        }
    }

    /**
     * 查询个人评论
     * @return
     * @throws Exception
     */
    @GetMapping("/mycomments")
    public ActionResult myComments() throws Exception {
        try {
            JSONObject vo = userService.myComments(getRequest().getHeader("token"));
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("error");
        }
    }
}
