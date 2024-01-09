package org.ltboys.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.action.ActionResult;
import org.ltboys.dto.ro.*;
import org.ltboys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.ltboys.context.utils.TokenUtil.getRequest;

/**
 * @author kralarklite
 */
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
    public ActionResult homepage(@RequestHeader("token") @Validated String token) throws Exception {
        //String token = getRequest().getHeader("token");
        try {
            //JSONObject vo = userService.homepage(getRequest().getHeader("token"));
            JSONObject vo = userService.homepage(token);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("error");
        }
    }


    /**
     * 更改用户信息
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/updateuser")
    public ActionResult updateUser(@RequestHeader("token") @Validated String token, @RequestBody @Validated UpdateUserRo ro) throws Exception {
        try {
            JSONObject vo = userService.updateUser(token, ro);
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
    public ActionResult myArticles(@RequestHeader("token") @Validated String token) throws Exception {
        try {
            JSONObject vo = userService.myArticles(token);
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
    public ActionResult myComments(@RequestHeader("token") @Validated String token) throws Exception {
        try {
            JSONObject vo = userService.myComments(token);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("error");
        }
    }

    /**
     * 查询个人收藏
     * @param token
     * @return
     * @throws Exception
     */
    @GetMapping("/mycollects")
    public ActionResult myCollects(@RequestHeader("token") @Validated String token) throws Exception{
        try {
            JSONObject vo = userService.myCollects(token);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("error");
        }
    }

    /**
     * 查询用户名称、头像、性别
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/brief")
    public ActionResult brief(@RequestBody @Validated IdRo ro) throws Exception {
        try {
            JSONObject vo = userService.brief(ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("登录失败");
        }
    }

    /**
     * 用户添加收藏游戏
     * @param token
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/addcollect")
    public ActionResult addCollect(@RequestHeader("token") @Validated String token, @RequestBody @Validated UserCollectRo ro) throws Exception {
        try {
            JSONObject vo = userService.addCollect(token, ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("登录失败");
        }
    }

    /**
     * 用户删除收藏游戏
     * @param token
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/deletecollect")
    public ActionResult deleteCollect(@RequestHeader("token") @Validated String token, @RequestBody @Validated UserCollectRo ro) throws Exception {
        try {
            JSONObject vo = userService.deleteCollect(token, ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("登录失败");
        }
    }

    /**
     * 用户评分游戏
     * @param token
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/rategame")
    public ActionResult rateGame(@RequestHeader("token") @Validated String token, @RequestBody @Validated RateGameRo ro) throws Exception {
        try {
            JSONObject vo = userService.rateGame(token, ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("登录失败");
        }
    }

    /**
     * 用户查看对某游戏的评分和收藏状态
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/viewgame_user")
    public ActionResult viewGameUser(@RequestBody @Validated UserCollectRo ro) throws Exception {
        try {
            JSONObject vo = userService.viewGameUser(ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("登录失败");
        }
    }

    /**
     * 接收用户首次推荐得到的游戏id并收藏
     * @param token
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/choosegame")
    public ActionResult chooseGame(@RequestHeader("token") @Validated String token, @RequestBody @Validated ChooseGameRo ro) throws Exception {
        try {
            JSONObject vo = userService.chooseGame(token, ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("登录失败");
        }
    }
}
