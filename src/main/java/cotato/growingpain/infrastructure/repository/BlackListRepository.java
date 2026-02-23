package cotato.growingpain.infrastructure.repository;

import cotato.growingpain.domain.entity.redis.BlackList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListRepository extends CrudRepository<BlackList, String> {
}