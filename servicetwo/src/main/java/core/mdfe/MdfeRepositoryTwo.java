package core.mdfe;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MdfeRepositoryTwo extends MongoRepository<Mdfe,String> {
}
