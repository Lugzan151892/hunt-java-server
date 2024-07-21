package lugzan.co.huntjavaserver.repository;

import lugzan.co.huntjavaserver.models.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
    UserModel findByUsername(String username);
    UserModel findById(int id);
}
