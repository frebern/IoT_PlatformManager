package selab.hanyang.ac.kr.platformmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import selab.hanyang.ac.kr.platformmanager.database.repository.UserRepository;
import selab.hanyang.ac.kr.platformmanager.util.OTP;
import selab.hanyang.ac.kr.platformmanager.util.RequestParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class LoginController {

    // 세션관리를 위한 User 클래스 (* DB를 위한 객체는 database.User)
    private class User{
        String userId;
        public String sessionKey;
        public Date timestamp; // 차후 필요하면 인증이나 세션관리에 사용.
        public User(String userId, String sessionKey){
            this.userId = userId;
            this.sessionKey = sessionKey;
            this.timestamp = new Date();
        }

        @Override
        public String toString() {
            return "["+userId+", "+sessionKey+"]";
        }
    }

    private List<User> loginUsers = new ArrayList<>();

    @Autowired
    UserRepository userRepo;

    @CrossOrigin(origins = "http://localhost")
    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public @ResponseBody
    String login(HttpServletRequest request, HttpServletResponse response) {

        response.setStatus(HttpServletResponse.SC_OK);

        RequestParser parser = new RequestParser(request);
        String userId = parser.getAsString("userId");
        String userPW = parser.getAsString("userPW"); //클라이언트로부터 SHA3-256 해시되서 넘어옴.

        // 인증 절차.
        // 해당 유저가 있는가?
        boolean isUserExist = userRepo.exists(userId);
        if(!isUserExist) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "{\"error\" : \"no_user\"}";
        }
        // 패스워드가 맞는가?
        boolean isPasswdValid = userRepo.checkUserIdAndPW(userId, userPW);
        if(!isPasswdValid){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "{\"error\" : \"wrong_password\"}";
        }

        // 로그인 성공시, 세션키를 생성해서 반환한다.
        String sessionKey = OTP.create(userId);
        loginUsers.add(new User(userId, sessionKey));
        return "{\"sessionKey\" : \""+sessionKey+"\"}";

    }

    @CrossOrigin(origins = "http://localhost")
    @PostMapping(path = "/session", consumes = "application/json", produces = "application/json")
    public @ResponseBody
    String checkSessionKey(HttpServletRequest request, HttpServletResponse response) {

        removeAllSessionExpiredUsers();

        RequestParser parser = new RequestParser(request);
        String userId = parser.getAsString("userId");
        String sessionKey = parser.getAsString("sessionKey");

        boolean isValid = loginUsers.stream().filter(user -> user.userId.equals(userId))
                                               .filter(user -> checkTimeStamp(user.timestamp))
                                               .anyMatch(user -> user.sessionKey.equals(sessionKey));

        return "{\"authenticated\" : "+isValid+"}";
    }

    // 세션이 지났다면 제거.
    private void removeAllSessionExpiredUsers(){
        loginUsers.removeIf(user -> checkTimeStamp(user.timestamp));
    }

    // 로그인한지 1시간이 넘었으면 세션 만료된 것으로 판별
    // 아직 유효하다면 true, 지났다면 false
    private boolean checkTimeStamp(Date loginTime){
        final long expire = 1000 * 60 * 60; // 1Hour
        return (new Date().getTime() - loginTime.getTime())/expire == 0;
    }

    //TODO: 디버깅용. 다 끝나면 지울 것.
    // 현재 로그인된 유저 목록을 보여준다.
    @CrossOrigin(origins = "http://localhost")
    @GetMapping(path = "/users")
    public @ResponseBody
    String getLoggedInUserList() {
        return Arrays.toString(loginUsers.toArray());
    }

}
