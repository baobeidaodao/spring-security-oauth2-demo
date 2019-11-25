package cloud.daodao.demo.repository;

import cloud.daodao.demo.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author DaoDao
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

    /**
     * 查询用户权限
     *
     * @param userId Long
     * @return List
     */
    @Query("select p from User u left join UserRole as ur on u.id = ur.userId left join Role as r on ur.roleId = r.id left join RolePermission as rp on r.id = rp.roleId left join Permission as p on rp.permissionId = p.id where u.id = ?1")
    List<Permission> findAllByUserId(Long userId);

}
