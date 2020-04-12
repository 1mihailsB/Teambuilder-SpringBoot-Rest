package com.teamplanner.rest.controller;

import com.teamplanner.rest.model.EntityDtoConverter;
import com.teamplanner.rest.model.dto.GamePlanDto;
import com.teamplanner.rest.model.entity.GamePlan;
import com.teamplanner.rest.model.entity.User;
import com.teamplanner.rest.service.GamePlanService;
import com.teamplanner.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("gameplans")
public class GamePlanController {

    UserService userService;
    EntityDtoConverter entityDtoConverter;
    GamePlanService gamePlanService;

    @Autowired
    public GamePlanController(UserService userService, GamePlanService gamePlanService, EntityDtoConverter entityDtoConverter) {
        this.userService = userService;
        this.entityDtoConverter = entityDtoConverter;
        this.gamePlanService = gamePlanService;
    }

    @GetMapping("/my")
    @Transactional
    public List<GamePlanDto> about (Authentication authentication) {
        String googlesub = (String) authentication.getPrincipal();
        User user = userService.findById(googlesub);
        
        List<GamePlanDto> gamePlanDtos = entityDtoConverter.gameplansToDto(user.getGamePlans(), user);

        return gamePlanDtos;
    }

    @PutMapping("/create")
    public String createGamePlan(@RequestBody Map<String, Object> gamePlanForm, Authentication authentication){

        User user = userService.findById((String) authentication.getPrincipal());

        GamePlan gameplan = new GamePlan((String) gamePlanForm.get("title"), (String) gamePlanForm.get("mainText"),
                ZonedDateTime.now());
        gameplan.setAuthor(user);

        try {
            gamePlanService.save(gameplan);
        }catch (DataIntegrityViolationException e){
            return "Game plan with this title already exists";
        }

        return "Game plan created";
    }

    @DeleteMapping("/delete/{id}")
    public void deleteGamePlan(@PathVariable int id, Authentication authentication){
        String googlesub = (String) authentication.getPrincipal();

        GamePlan gamePlan = gamePlanService.findById(id);
        String authorGooglesub = gamePlan.getAuthor().getGooglesub();

        if(googlesub.equals(authorGooglesub)){
            gamePlanService.deleteById(id);
        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
