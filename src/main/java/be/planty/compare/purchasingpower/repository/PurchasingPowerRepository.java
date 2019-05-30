package be.planty.compare.purchasingpower.repository;

import be.planty.compare.purchasingpower.domain.PurchasingPower;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the PurchasingPower entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchasingPowerRepository extends MongoRepository<PurchasingPower, String> {

}
