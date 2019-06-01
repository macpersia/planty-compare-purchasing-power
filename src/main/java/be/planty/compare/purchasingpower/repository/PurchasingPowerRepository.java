package be.planty.compare.purchasingpower.repository;

import be.planty.compare.purchasingpower.domain.PurchasingPower;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;


/**
 * Spring Data MongoDB repository for the PurchasingPower entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchasingPowerRepository extends MongoRepository<PurchasingPower, String> {

    @Async
    CompletableFuture<PurchasingPower> 
        findFirstByCategoryAndCityOrderByYearDesc(String category, String city);
}
