package springcource.PP_3_1_4_Bootstrap.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springcource.PP_3_1_4_Bootstrap.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    @Query("Select u from User u left join fetch u.roles where u.email=:userName")
    Optional<User> findByUserName (@Param("userName") String userName);
}
