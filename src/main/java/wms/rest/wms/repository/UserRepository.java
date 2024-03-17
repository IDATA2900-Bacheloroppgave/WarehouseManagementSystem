package wms.rest.wms.repository;

import org.springframework.data.repository.CrudRepository;
import wms.rest.wms.user.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}
