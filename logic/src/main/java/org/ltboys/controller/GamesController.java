package org.ltboys.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.action.ActionResult;
import org.ltboys.dto.ro.IdRo;
import org.ltboys.dto.ro.QueryGamesRo;
import org.ltboys.service.GamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author kralarklite
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/games")
public class GamesController {

    @Autowired
    private GamesService gamesService;

    /**
     * 查询单个游戏信息及其tag信息
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/viewgame")
    public ActionResult viewGame (@RequestBody @Validated IdRo ro) throws Exception {

        try {
            JSONObject vo = gamesService.viewGame(ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询游戏失败");
        }
    }

    /**
     * 获取tag列表
     * @return
     * @throws Exception
     */
    @GetMapping("/gettags")
    public ActionResult getTags () throws Exception {
        try {
            JSONObject vo = gamesService.getTags();
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询tag失败");
        }
    }

    /**
     * 根据条件查询游戏列表（分页），根据ro中的字段有多种用法，支持按热度查询、随机排序查询
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/querygames")
    public ActionResult queryGames (@RequestBody @Validated QueryGamesRo ro) throws Exception {
        try {
            JSONObject vo = gamesService.queryGames(ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询游戏失败");
        }
    }

    /**
     * 根据用户打分推荐游戏
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/recommendgames")
    public ActionResult recommendGames(@RequestBody @Validated IdRo ro) throws  Exception {
        try {
            JSONObject vo = gamesService.recommendGames(ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询游戏失败");
        }
    }

    /**
     * 根据用户收藏推荐游戏
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/collectrecommend")
    public ActionResult collectRecommend(@RequestBody @Validated IdRo ro) throws  Exception {
        try {
            JSONObject vo = gamesService.collectRecommend(ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询游戏失败");
        }
    }

}
