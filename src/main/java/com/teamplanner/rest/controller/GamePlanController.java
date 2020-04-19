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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @GetMapping("/all")
    public List<GamePlanDto> myGames (Authentication authentication) {
        String googlesub = (String) authentication.getPrincipal();
        User user = userService.findById(googlesub);
        
        List<GamePlanDto> gamePlanDtos = entityDtoConverter.gameplansToDto(user.getGamePlans(), user);

        return gamePlanDtos;
    }

    @PatchMapping("/editById/{id}")
    public String editGamePlan(@PathVariable int id, @RequestBody Map<String, String> mainText, Authentication authentication){

        Pattern mainTextPattern = Pattern.compile("^[a-zA-Z0-9 ,:;'/!.\n\\[\\]!@_-]{1,3000}$");
        Matcher mainTextMatcher = mainTextPattern.matcher(mainText.get("mainText"));

        if(!mainTextMatcher.matches()){
            return "Incorrect text";
        }

        String userGooglesub = (String) authentication.getPrincipal();
        GamePlan gamePlan = gamePlanService.findById(id);

        if(userGooglesub.equals(gamePlan.getAuthor().getGooglesub())){
            gamePlan.setMainText(mainText.get("mainText"));
            gamePlanService.save(gamePlan);
            return "Game plan edited";
        }

        return "You are not the author of this plan";
    }

    @PutMapping("/create")
    public String createGamePlan(@RequestBody Map<String, String> gamePlanForm, Authentication authentication){

        Pattern titlePattern = Pattern.compile("^[a-zA-Z0-9 ,:\\[\\]!@_-]{1,50}$");
        Matcher titleMatcher = titlePattern.matcher(gamePlanForm.get("title"));

        if(!titleMatcher.matches()){
            return "Incorrect title";
        }

        Pattern mainTextPattern = Pattern.compile("^[a-zA-Z0-9 ,:;'/!.\n\\[\\]!@_-]{1,3000}$");
        Matcher mainTextMatcher = mainTextPattern.matcher(gamePlanForm.get("mainText"));

        if(!mainTextMatcher.matches()){
            return "Incorrect text";
        }

        User user = userService.findById((String) authentication.getPrincipal());

        GamePlan gameplan = new GamePlan(gamePlanForm.get("title"), (String) gamePlanForm.get("mainText"),
                ZonedDateTime.now());
        gameplan.setAuthor(user);

        try {
            gamePlanService.save(gameplan);
        }catch (DataIntegrityViolationException e){
            return "Game plan with this title already exists";
        }

        return "Game plan created";
    }

    @GetMapping("/getById/{id}")
    public GamePlanDto getGamePlan(@PathVariable int id, Authentication authentication){
        String googlesub = (String) authentication.getPrincipal();
        User user = userService.findById(googlesub);

        GamePlan gamePlan = gamePlanService.findById(id);

        String authorGooglesub = gamePlan.getAuthor().getGooglesub();

        if(user.getGooglesub().equals(authorGooglesub)){
            return entityDtoConverter.gameplanToDto(gamePlan, user);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
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