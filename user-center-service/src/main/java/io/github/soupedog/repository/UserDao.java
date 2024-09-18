package io.github.soupedog.repository;

import io.github.soupedog.domain.po.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * @author Xavier
 * @date 2021/9/21
 */
@Repository
public interface UserDao extends JpaRepository<User, Long> {
    /**
     * 局部更新名字,更新、删除操作被要求在事务中进行
     * 此处是 HQL
     */
    @Modifying
    @Transactional
    @Query(value = "update User as u set u.name=:userName,u.lastUpdateTs=:updateTs where u.uid=:uid and u.lastUpdateTs<:updateTs")
    int updateUserName(@Param("updateTs") long updateTs, @Param("uid") long uid, @Param("userName") String userName);

    /**
     * 局部更新名字,更新、删除操作被要求在事务中进行
     * 此处是 HQL
     */
    @Modifying
    @Transactional
    @Query(value = "update User as u set u.name=?2,u.lastUpdateTs=?3 where u.uid=?1 and u.lastUpdateTs<?3")
    int updateUserName(long uid, String userName, long updateTs);
}
