package selab.hanyang.ac.kr.platformmanager.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import selab.hanyang.ac.kr.platformmanager.database.model.PDP;
import selab.hanyang.ac.kr.platformmanager.database.model.User;
import selab.hanyang.ac.kr.platformmanager.database.repository.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/*
 *    초기 가정.
 *   1. DB에 기본 PDP가 세팅되어 있다. (1, "pdp1")
 *   2. 유저는 회원가입을 이미 한 상태이다.(table User)
 *   3. 그 외 DB는 싹 다 비워놓을 것.
 *   (id:"frebern", pwd:SHA3-256("passwd"))
 *
 * */
@Component
public class Mockup implements ApplicationRunner {
    @Autowired DeviceActionRepository devActRepo;
    @Autowired DeviceRepository devRepo;
    @Autowired GroupMemberRepository grpMemRepo;
    @Autowired PDPRepository pdpRepo;
    @Autowired PEPGroupRepository pepGrpRepo;
    @Autowired PEPRepository pepRepo;
    @Autowired SessionKeyRespository sKeyRepo;
    @Autowired UserRepository userRepo;

    @Autowired
    public Mockup(){}

    public void init(){
        deleteAll();
        initPDP();
        initUser();
    }

    @Transactional
    public void deleteAll(){
        System.out.println("[SELab IoT - Delete All data in tables...]");
        devActRepo.deleteAll();
        devRepo.deleteAll();
        grpMemRepo.deleteAll();
        pepRepo.deleteAll();
        sKeyRepo.deleteAll();
        pepGrpRepo.deleteAll();
        pdpRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Transactional
    private void initUser(){
        System.out.println("[SELab IoT - insert mockup User in tables...]");
        User user1 = new User("frebern",
                "0ab9c05e449543077590b6e54da3b7781c43bf40a8bc66bd19d19340cb5936e8", // "passwd"
                "{\"userName\":\"안윤근\", \"role\":[\"family\", \"admin\"]}");

        List<User> userList = new ArrayList<>();
        userList.add(user1);

        userRepo.save(userList);
        userRepo.flush();
    }

    @Transactional
    private void initPDP(){
        System.out.println("[SELab IoT - insert PDP in tables...]");
        // ./conf/pdp/config.xml에 있는 pdp 이름만 넣을 것
        PDP pdp1 = new PDP(1, "pdp1");

        List<PDP> pdpList = new ArrayList<>();
        pdpList.add(pdp1);

        pdpRepo.save(pdpList);
        pdpRepo.flush();
    }

    @Override
    public void run(ApplicationArguments applicationArguments) {
        init();
    }

}
