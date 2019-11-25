package cloud.daodao.demo.repository;

import cloud.daodao.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author DaoDao
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 查询 User
     *
     * @param username String
     * @return User
     */
    User findUserByUsername(String username);

}
