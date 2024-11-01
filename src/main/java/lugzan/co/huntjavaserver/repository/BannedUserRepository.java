package lugzan.co.huntjavaserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import lugzan.co.huntjavaserver.models.banned_users.BannedUser;

public interface BannedUserRepository extends JpaRepository<BannedUser, Integer> {
    BannedUser findById(int id);
    BannedUser findBySteamId(String steam_id);
}
