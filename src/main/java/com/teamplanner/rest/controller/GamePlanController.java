package com.teamplanner.rest.controller;

import com.teamplanner.rest.model.EntityDtoConverter;
import com.teamplanner.rest.model.dto.GamePlanDto;
import com.teamplanner.rest.model.entity.GamePlan;
import com.teamplanner.rest.model.entity.GameplanMember;
import com.teamplanner.rest.model.entity.User;
import com.teamplanner.rest.service.GamePlanService;
import com.teamplanner.rest.service.GameplanMemberService;
import com.teamplanner.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    GameplanMemberService gameplanMemberService;
    SimpMessageSendingOperations stomp;

    @Autowired
    public GamePlanController(UserService userService, GamePlanService gamePlanService,
                              EntityDtoConverter entityDtoConverter, GameplanMemberService gameplanMemberService,
                              SimpMessageSendingOperations stomp) {
        this.userService = userService;
        this.entityDtoConverter = entityDtoConverter;
        this.gamePlanService = gamePlanService;
        this.gameplanMemberService = gameplanMemberService;
        this.stomp = stomp;
    }

    @GetMapping("/all")
    public List<GamePlanDto> myGames (Authentication authentication) {
        String googlesub = (String) authentication.getPrincipal();
        User user = userService.findById(googlesub);
        List<GamePlan> usersOwnGameplans = user.getGamePlans();

        List<GameplanMember> userGuestGameplanMembers = gameplanMemberService.findAcceptedGameInvites(user);
        List<GamePlan> userGuestGameplans = new ArrayList<>();
        for(GameplanMember gpm : userGuestGameplanMembers){
            userGuestGameplans.add(gpm.getGamePlan());
        }

        List<GamePlanDto> gamePlanDtos = entityDtoConverter.gameplansToDto(usersOwnGameplans, user, userGuestGameplans);

        return gamePlanDtos;
    }

    @PatchMapping("/editById/{id}")
    public String editGamePlan(@PathVariable int id, @RequestBody Map<String, String> mainText, Authentication authentication){

        Pattern mainTextPattern = Pattern.compile("^[a-zA-Z0-9 ,:;'/!.\n\\[\\]!@_-]{1,3000}$");
        Matcher mainTextMatcher = mainTextPattern.matcher(mainText.get("mainText"));

        if(!mainTextMatcher.matches()) return "Incorrect text";

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
    @Transactional
    public GamePlanDto getGamePlan(@PathVariable int id, Authentication authentication){
        String googlesub = (String) authentication.getPrincipal();
        User user = userService.findById(googlesub);

        GamePlan gamePlan = gamePlanService.findById(id);

        String authorGooglesub = null;
        if(gamePlan != null) authorGooglesub = gamePlan.getAuthor().getGooglesub();

        if(user.getGooglesub().equals(authorGooglesub)){
            List<GameplanMember> members = gameplanMemberService.findAllMembersByGameplan(gamePlan);
            return entityDtoConverter.gameplanToDto(gamePlan, members);
        }

        GameplanMember gameplanMember = gameplanMemberService.findExistingGameplanMember(user,gamePlan);
        if(gameplanMember != null && gameplanMember.getStatus() == 1){
            List<GameplanMember> members = gameplanMemberService.findAllMembersByGameplan(gamePlan);
            return entityDtoConverter.gameplanToDto(gamePlan,members);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public void deleteGamePlan(@PathVariable int id, Authentication authentication){
        String googlesub = (String) authentication.getPrincipal();

        GamePlan gamePlan = gamePlanService.findById(id);

        String authorGooglesub = gamePlan.getAuthor().getGooglesub();

        if(googlesub.equals(authorGooglesub)){

            gameplanMemberService.deleteAllByGameplan(gamePlan);

            gamePlanService.deleteById(id);
        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/inviteToGame/{gameId}")
    @Transactional
    public String addToGame(@PathVariable int gameId, @RequestBody String invitedNickname, Authentication authentication) {
        String googlesub = (String) authentication.getPrincipal();

        User user = userService.findById(googlesub);

        User invitee = userService.findByNickname(invitedNickname);

        GamePlan gamePlan = gamePlanService.findById(gameId);

        GameplanMember gameplanMember = gameplanMemberService.findExistingGameplanMember(invitee, gamePlan);
        if (gameplanMember != null) {
            if (gameplanMember.getStatus() == 0) return "Request is pending";
            if (gameplanMember.getStatus() == 1) return "This user is already in your game";
        }

        if (gamePlan.getAuthor().equals(user)) {

            GameplanMember gpm = gameplanMemberService.save(new GameplanMember(invitee, gamePlan, 0));

            stomp.convertAndSendToUser(invitee.getGooglesub(), "/queue/requests",
                    "Game invite");

            return "Game invitation sent";
        }

        return "Error";
    }

    @GetMapping("/incomingGameInvites")
    public List<String[]> getIncomingGameInvites(Authentication authentication){
        User user = userService.findById((String) authentication.getPrincipal());

        List<GameplanMember> incomingGameInvites = gameplanMemberService.findIncomingGameInvites(user);

        List<String[]> authorsAndGametitles = entityDtoConverter.gameplanMembersToAuthorAndTitleMap(incomingGameInvites);

        return authorsAndGametitles;
    }

    @DeleteMapping("/declineGameInvite/{gameplanMemberId}")
    public void declineGameInvite(@PathVariable int gameplanMemberId, Authentication authentication){
        User user = userService.findById((String) authentication.getPrincipal());

        GameplanMember gameplanMember = gameplanMemberService.findById(gameplanMemberId);

        if(gameplanMember!=null){
            if(user.equals(gameplanMember.getMember())){
                gameplanMemberService.declineGameInvite(gameplanMemberId);
            }
        }

    }

    @PatchMapping("/acceptGameInvite/{gameId}")
    @Transactional
    public void acceptGameInvite(@PathVariable int gameId, Authentication authentication){
        User user = userService.findById((String) authentication.getPrincipal());

        GameplanMember gameplanMember = gameplanMemberService.findById(gameId);

        if(gameplanMember!=null){
            if(user.equals(gameplanMember.getMember())){
               gameplanMember.setStatus(1);
            }
        }
    }

    @DeleteMapping("/removeGameMember/{gameId}")
    public void removeGameMember(@PathVariable int gameId, @RequestBody String nickname, Authentication authentication){
        User user = userService.findById((String) authentication.getPrincipal());
        User member = userService.findByNickname(nickname);

        GamePlan gamePlan = gamePlanService.findById(gameId);

        if(user.equals(gamePlan.getAuthor())){
            GameplanMember gameplanMember = gameplanMemberService.findExistingGameplanMember(member,gamePlan);
            if(gameplanMember != null && gameplanMember.getStatus()==1){
                gameplanMemberService.deleteById(gameplanMember.getId());
            }
        }else if(user.equals(member)){
            GameplanMember gameplanMember = gameplanMemberService.findExistingGameplanMember(user,gamePlan);
            if(gameplanMember != null && gameplanMember.getStatus()==1){
                gameplanMemberService.deleteById(gameplanMember.getId());
            }
        }
    }

}