package selab.hanyang.ac.kr.platformmanager.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import selab.hanyang.ac.kr.platformmanager.util.RequestParser;
import selab.hanyang.ac.kr.platformmanager.database.repository.GroupMemberRepository;
import selab.hanyang.ac.kr.platformmanager.database.repository.PEPGroupRepository;
import selab.hanyang.ac.kr.platformmanager.database.repository.PEPRepository;
import selab.hanyang.ac.kr.platformmanager.database.repository.UserRepository;
import selab.hanyang.ac.kr.platformmanager.service.PEPRegistrationService;

import java.util.concurrent.ExecutionException;

@Controller
public class PEPRegistrationController {

    @Autowired
    PEPRegistrationService pepRegistrationService;

    private JsonObject failed(String reason){
        JsonObject response = new JsonObject();
        response.addProperty("success", false);
        response.addProperty("reason", reason);
        return response;
    }

    @CrossOrigin(origins = "http://localhost")
    @PostMapping(path = "/groups", consumes = "application/json", produces = "application/json")
    public @ResponseBody String addPEPtoPEPGroup(@RequestBody String request) {
        RequestParser parser = new RequestParser(request);
        JsonObject object = parser.getAsJsonObject();
        JsonObject response = null;
        Gson gson = new GsonBuilder().create();
        try {
            response = pepRegistrationService.addPEPtoPEPGroup(object).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            response = failed("In addPEPtoPEPGroup : Future execution failed");
        }
        return gson.toJson(response);
    }


    @CrossOrigin(origins = "http://localhost")
    @GetMapping(path="/groups/{userId}/{pepId}", produces = "application/json")
    public @ResponseBody String searchPEPGroup(@PathVariable String userId, @PathVariable String pepId) {
        Gson gson = new GsonBuilder().create();
        JsonObject response = null;
        try {
            response = pepRegistrationService.searchPEPGroup(userId, pepId).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            response = failed("In searchPEPGroup : Future execution failed");
        }
        return gson.toJson(response);
    }



}
