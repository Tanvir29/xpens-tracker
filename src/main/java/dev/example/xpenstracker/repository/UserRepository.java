package dev.example.xpenstracker.repository;

import dev.example.xpenstracker.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
    public UserInfo findByEmailAndPassword(String email, String password);
}
