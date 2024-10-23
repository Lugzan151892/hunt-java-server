package lugzan.co.huntjavaserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import lugzan.co.huntjavaserver.models.refresh_token.RefreshToken;
import lugzan.co.huntjavaserver.models.user.UserModel;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByUser(UserModel user);
}
