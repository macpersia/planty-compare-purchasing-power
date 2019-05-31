package be.planty.compare.purchasingpower.web.rest;

import be.planty.compare.purchasingpower.repository.PurchasingPowerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * EquivalentIncomeResource controller
 */
@RestController
@RequestMapping("/api/equivalent-income")
public class EquivalentIncomeResource {

    private final Logger log = LoggerFactory.getLogger(EquivalentIncomeResource.class);

    private final PurchasingPowerRepository purchasingPowerRepository;

    public EquivalentIncomeResource(PurchasingPowerRepository purchasingPowerRepository) {
        this.purchasingPowerRepository = purchasingPowerRepository;
    }

    /**
    * GET calculateEquivalentIncome
    */
    //@GetMapping("/calculate-equivalent-income")
    @GetMapping
    public ResponseEntity<Double> calculateEquivalentIncome(
        @RequestParam String targetCity, @RequestParam String targetCurrency,
        @RequestParam String baseCity, @RequestParam Double baseIncomeAmount, @RequestParam String baseCurrency
    ) {
        final var currencies = Stream.of(targetCurrency, baseCurrency);
        // TODO: Add support for more currencies!
        if (currencies.noneMatch(c -> c.equals("USD"))) {
            //throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            throw new ResponseStatusException(BAD_REQUEST, "Currently, only USD is supported!");
        }
        final var repo = purchasingPowerRepository;
        final var futureTargetMatch = repo.findFirstByCategoryAndCityOrderByYearDesc("N", targetCity);
        final var futureBaseMatch = repo.findFirstByCategoryAndCityOrderByYearDesc("N", baseCity);

        final var purchasingPowers =
            Stream.of(futureTargetMatch, futureBaseMatch)
                .parallel()
                .map(CompletableFuture::join)
                .collect(toList());

        if (purchasingPowers.stream().anyMatch(Objects::isNull)) {
            throw new ResponseStatusException(NOT_FOUND);
        }
        final var targetPurchasingPower = purchasingPowers.get(0);
        final var basePurchasingPower = purchasingPowers.get(1);
        final var baseValue = basePurchasingPower.getValue();
        final var targetValue = targetPurchasingPower.getValue();
        final var result = (baseIncomeAmount / baseValue) * targetValue;

        return ResponseEntity.ok().body(result);
    }
}
