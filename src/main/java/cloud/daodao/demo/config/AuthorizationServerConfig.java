package cloud.daodao.demo.config;

import cloud.daodao.demo.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;


/**
 * 认证服务器配置
 * <p>
 * implicit：简化模式，不推荐使用
 * authorization code：授权码模式
 * resource owner password credentials：密码模式
 * client credentials：客户端模式
 *
 * @author DaoDao
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private DataSource dataSource;

    @Resource
    private TokenStore tokenStore;

    /**
     * 注入权限验证控制器 来支持 password grant type
     */
    @Resource
    private AuthenticationManager authenticationManager;

    @Bean
    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
        /*
         * 基于内存的令牌存储
         */
        // return new InMemoryTokenStore();
        /*
         * 基于数据库的令牌存储
         */
        // return new JdbcTokenStore(dataSource());
        /*
         * 基于 redis 的令牌存储
         */
        final RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("paas::oauth2::");
        return redisTokenStore;
    }

//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource jdbcDataSource() {
//        // 配置数据源（注意，我使用的是 HikariCP 连接池），以上注解是指定数据源，否则会有冲突
//        return DataSourceBuilder.create().build();
//    }

//    @Bean
//    public TokenStore tokenStore() {
//        // 基于 JDBC 实现，令牌保存到数据
//        return new JdbcTokenStore(dataSource());
//    }

    @Bean
    public ClientDetailsService jdbcClientDetails() {
        // 基于 JDBC 实现，需要事先在数据库配置客户端信息
        //return new JdbcClientDetailsService(jdbcDataSource());
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 设置令牌
        //endpoints.tokenStore(tokenStore());
        endpoints.tokenStore(tokenStore);

        endpoints.authenticationManager(authenticationManager);

        endpoints.userDetailsService(userDetailsService);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 读取客户端配置
        clients.withClientDetails(jdbcClientDetails());
    }

}
