package org.ltboys.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.action.ActionResult;
import org.ltboys.dto.ro.QueryGamesRo;
import org.ltboys.service.GamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/games")
public class GamesController {

    @Autowired
    private GamesService gamesService;

    @PostMapping("/game")
    public ActionResult viewGame (@RequestBody @Validated int id) throws Exception {

        try {
            JSONObject vo = gamesService.viewGame(id);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询游戏失败");
        }
    }

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
