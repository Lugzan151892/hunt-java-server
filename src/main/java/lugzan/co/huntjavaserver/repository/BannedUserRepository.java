package lugzan.co.huntjavaserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import lugzan.co.huntjavaserver.models.banned_users.BannedUser;
import lugzan.co.huntjavaserver.models.user.UserModel;

public interface BannedUserRepository extends JpaRepository<BannedUser, Integer> {
    BannedUser findById(int id);
    BannedUser findBySteamId(String steam_id);
    Optional<BannedUser> findBySteamIdAndUser(String steamId, UserModel user);
}
