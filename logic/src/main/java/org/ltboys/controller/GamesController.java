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
     * 根据条件查询游戏列表（分页）
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
}
