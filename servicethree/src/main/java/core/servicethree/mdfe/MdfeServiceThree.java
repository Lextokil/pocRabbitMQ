package core.servicethree.mdfe;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MdfeServiceThree extends MongoRepository<Mdfe, String> {
}
