package selab.hanyang.ac.kr.platformmanager.service;

import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import selab.hanyang.ac.kr.platformmanager.controller.DeviceRegistrationController;
import selab.hanyang.ac.kr.platformmanager.util.OTP;
import selab.hanyang.ac.kr.platformmanager.database.model.*;
import selab.hanyang.ac.kr.platformmanager.database.repository.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class PEPRegistrationService {

    @Autowired
    PEPGroupRepository pepGroupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PEPRepository pepRepository;

    @Autowired
    PDPRepository pdpRepository;

    @Autowired
    GroupMemberRepository groupMemberRepository;

    @Autowired
    SessionKeyRespository sessionKeyRespository;

    @Async
    public Future<JsonObject> addPEPtoPEPGroup(JsonObject object) {
        System.out.println(object);
        String userId = object.get("userId").getAsString();
        JsonElement pepProfile = object.get("pepProfile");
        JsonElement pepGroupId = object.get("pepGroupId");
        User user = userRepository.findOne(userId);
        JsonObject response = new JsonObject();
        if (user == null) {
            response.addProperty("success", false);
            response.addProperty("reason","User not found");
        }
        // Step 7.a. PEP를 등록하는 경우
        else if (pepProfile != null) {

            System.out.println("Step 7.a");
            System.out.println("PEP Profile : "+pepProfile.getAsString());
            PEP pep = new PEP();
            JsonObject pep_json = pepProfile.getAsJsonObject();
            pep.setPepId(pep_json.get("pepId").getAsString());
            pep.setPepName(pep_json.get("pepName").getAsString());
            pep.setIp(pep_json.get("pepIp").getAsString());

            // 일단 Default PDP로 설정.
            PDP defaultPDP = pdpRepository.findOne(1);
            pep.setPdp(defaultPDP);

            PEPGroup pepGroup = null;
            // Step 7.a.1. 새 그룹 생성
            if(object.get("pepGroupId") == null) {
                String groupName = object.get("pepGroupName").getAsString();
                String groupPW = object.get("pepGroupPW").getAsString();
                pepGroup = createPEPGroup(user, groupName, groupPW); // PEPGroup save 됨.
                addGroupMember(user, pepGroup.getPepGroupId(), pepGroup.getGroupPW(), response); // GroupMember save 됨.
            }
            // Step 7.a.2. 기존 그룹에 추가
            else {
                long groupId = pepGroupId.getAsLong();
                pepGroup = pepGroupRepository.findOne(groupId);
            }

            JsonElement success = response.get("success"); // null이거나 true면 문제없음.
            if(success == null || success.getAsBoolean())
                addPEPToPEPGroup(pep, pepGroup, response); // PEP save 됨.

        }
        // Step 7.b. PEP가 이미 등록된 경우, 사용자 인증 절차 진행 후 그룹에 사용자 가입
        else if (pepGroupId != null) {
            System.out.println("Step 7.b");
            String pepGroupPW = object.get("pepGroupPW").getAsString();
            addGroupMember(user, pepGroupId.getAsLong(), pepGroupPW, response); // GroupMember save 됨.
        }
        else { // 페이로드에 pepProfile도 없고, pepGroupId도 없는 경우
            System.out.println("Bad Request");
            response.addProperty("success", false);
            response.addProperty("reason", "Bad Request");
        }

        return new AsyncResult<>(response);
    }

    @Async
    public Future<JsonObject> searchPEPGroup(String userId, String pepId) {
        User user = userRepository.findOne(userId);
        PEP pep = pepRepository.findOne(pepId);
        JsonObject response = new JsonObject();
        if (user == null) {
            response.addProperty("error", "No User info");
        } else if (pep != null && pep.getPepGroup() != null) { // TODO: NullPointerException 해결
            PEPGroup pepGroup = pepGroupRepository.findByOwnerAndPEP(user, pep);
            response.addProperty("hasGroup", true);
            response.addProperty("pepGroupId", pepGroup.getPepGroupId());
        } else {
            response.addProperty("hasGroup", false);
            // 아래 두줄은 필요 없을 듯.
//            List<PEPGroup> pepGroups = pepGroupRepository.findPEPGroupsByOwner(user);
//            response.addProperty("pepGroupList", gson.toJson(pepGroups.stream().map(pepGroup -> pepGroup.getPepGroupId()).toArray()));
        }
        return new AsyncResult<>(response);
    }

    private PEPGroup createPEPGroup(User user, String pepGroupName, String pepGroupPW) {
        PEPGroup pepGroup = new PEPGroup(pepGroupName, pepGroupPW, user);
        pepGroupRepository.saveAndFlush(pepGroup);
        return pepGroup;
    }

    private void addPEPToPEPGroup(PEP pep, PEPGroup pepGroup, JsonObject response) {
        pep.setPepGroup(pepGroup);
        pepRepository.saveAndFlush(pep);
        response.addProperty("success",true);
    }

    @Deprecated
    // Step 8 이후로 삭제했기 때문에 이제 필요없음.
    private void returnSessionKey(String pepId, JsonObject response){
        String otpKey = OTP.create(pepId);
        sessionKeyRespository.save(new SessionKey(pepId, otpKey));
        response.addProperty("sessionKey", otpKey);
    }

    private void addGroupMember(User user, long pepGroupId, String pepGroupPW, JsonObject response) {
        PEPGroup pepGroup = pepGroupRepository.findOne(pepGroupId);
        if (checkPEPGroupPW(pepGroup, pepGroupPW)) {
            GroupMember groupMember = new GroupMember(user, pepGroup);
            groupMemberRepository.saveAndFlush(groupMember);
            response.addProperty("success", true);
        } else {
            response.addProperty("success", false);
            response.addProperty("reason", "Authentication Failed : Wrong PEPGroup Password");
        }
    }

    //TODO: 비밀번호 인증 방법 변경 필요 ... ?
    private boolean checkPEPGroupPW(PEPGroup pepGroup, String pepGroupPW) {
        return pepGroup.getGroupPW().equals(pepGroupPW);
    }
}
