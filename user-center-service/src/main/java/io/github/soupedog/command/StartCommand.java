package io.github.soupedog.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.soupedog.domain.enums.UserSexEnum;
import io.github.soupedog.domain.po.User;
import io.github.soupedog.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 初始化表记录
 *
 * @author Xavier
 * @date 2021/9/21
 */
@Component
public class StartCommand implements CommandLineRunner {
    @Autowired
    private UserDao userDao;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void run(String... args) {
        User user = new User();
        user.setName("张三");
        // 对应密码 aaa
        user.setPassword("c1a818a88adb66de711940dae5d22a2213c5353b72bf1b0c776626b8f3739087");
        user.setUserSex(UserSexEnum.SECRET);

        userDao.save(user);

        printUser();
    }

    private void printUser() {
        User user = new User();
        user.setUid(1L);

        Pageable pageable = PageRequest.of(0, 10);

        Page<User> queryResult = userDao.findAll(Example.of(user), pageable);
        queryResult.forEach((item) -> {
            try {
                System.out.println(mapper.writeValueAsString(item));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
