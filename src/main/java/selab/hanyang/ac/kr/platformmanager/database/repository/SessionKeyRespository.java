package selab.hanyang.ac.kr.platformmanager.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selab.hanyang.ac.kr.platformmanager.database.model.SessionKey;

// TODO: 의존성 남아있는지 확인하고 지울 것.
public interface SessionKeyRespository extends JpaRepository<SessionKey, String>{
}
