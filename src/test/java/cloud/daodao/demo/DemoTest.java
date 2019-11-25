package cloud.daodao.demo;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DemoTest {

    @Test
    public void test() {
        System.out.println(new BCryptPasswordEncoder().encode("secret"));
    }
}
