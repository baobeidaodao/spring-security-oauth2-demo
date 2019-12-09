package cloud.daodao.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author DaoDao
 */
@RestController
@RequestMapping
public class IndexController {

    @RequestMapping(path = {""})
    public Mono<String> index() {
        return Mono.just("hello world!");
    }

}
