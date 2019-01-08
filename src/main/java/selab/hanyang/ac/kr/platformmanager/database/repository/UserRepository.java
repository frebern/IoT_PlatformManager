package selab.hanyang.ac.kr.platformmanager.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import selab.hanyang.ac.kr.platformmanager.database.model.User;

public interface UserRepository extends JpaRepository<User, String>{

    // TODO:얘는 또 멀쩡하던게 왜이래..
    @Query("select count(*)>0 from User where user_id = :userId AND user_pw = :userPW")
    boolean checkUserIdAndPW(@Param("userId")String userId, @Param("userPW")String userPW);

}
