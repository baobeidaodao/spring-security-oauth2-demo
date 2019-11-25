package cloud.daodao.demo.service;

import cloud.daodao.demo.model.Permission;
import cloud.daodao.demo.model.User;
import cloud.daodao.demo.repository.PermissionRepository;
import cloud.daodao.demo.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author DaoDao
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        User user = new User();
        user.setUsername(username);
        Example<User> example = Example.of(user);
        Optional<User> optional = userRepository.findOne(example);
        if (!optional.isPresent()) {
            return null;
        } else {
            user = optional.get();
            /*
             * 获取用户授权
             */
            List<Permission> permissions = permissionRepository.findAllByUserId(user.getId());

            /*
             * 声明用户授权
             */
            permissions.forEach(permission -> {
                if (permission != null && permission.getCode() != null) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getCode());
                    grantedAuthorityList.add(grantedAuthority);
                }
            });
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorityList);
    }
}
