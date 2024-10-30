package io.github.soupedog.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.soupedog.domain.enums.UserTypeEnum;
import io.github.soupedog.domain.po.User;
import io.github.soupedog.repository.UserDao;
import io.github.soupedog.util.AESUtil;
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
        user.setUno("U0001");
        user.setName("张三");
        user.setAccount("zhzs");
        user.setPassword(AESUtil.encrypt("mm"));
        user.setUserType(UserTypeEnum.ROOT);

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
