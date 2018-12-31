package selab.hanyang.ac.kr.platformmanager.database.model;

import com.google.gson.JsonObject;
import selab.hanyang.ac.kr.platformmanager.database.converter.UserProfileConverter;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_pw")
    private String userPW;

    @Column(name = "profile")
    @Convert(converter = UserProfileConverter.class)
    private UserProfile userProfile;

    public User(){}

    public User(String userId, String userPW, String userProfile){
        this.userId = userId;
        // 이미 SHA3-256 해시돼서 들어옴.
        this.userPW = userPW;
        // Json 포맷으로 들어옴.
        this.userProfile = new UserProfileConverter().convertToEntityAttribute(userProfile);
    }

    public String getUserId() {
        return userId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

}
