package selab.hanyang.ac.kr.platformmanager.database.model;

import javax.persistence.*;

// TODO: 의존성 남아있는지 확인하고 지울 것.
@Entity @IdClass(SessionKeyId.class)
@Table(name = "session_key")
public class SessionKey{

    @Id
    @Column
    public String pepId;

    @Id
    @Column
    public String sessionKey;

    public SessionKey() {}

    public SessionKey(String pepId, String sessionKey) {
        this.pepId = pepId;
        this.sessionKey = sessionKey;
    }
}
