package be.planty.compare.purchasingpower.web.rest;

import be.planty.compare.purchasingpower.domain.PurchasingPower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * EquivalentIncomeResource controller
 */
@RestController
@RequestMapping("/api/equivalent-income")
public class EquivalentIncomeResource {

    private final Logger log = LoggerFactory.getLogger(EquivalentIncomeResource.class);

    /**
    * GET calculateEquivalentIncome
    */
    //@GetMapping("/calculate-equivalent-income")
    @GetMapping
    public ResponseEntity<Double> calculateEquivalentIncome(
        @RequestParam String targetCity, @RequestParam String targetCurrency,
        @RequestParam String baseCity, @RequestParam Double baseIncomeAmount, @RequestParam String baseCurrency
    ) {
        var currencies = new String[] { targetCurrency, baseCurrency };
        // TODO: Add support for more currencies!
        if (Stream.of(currencies).anyMatch(c -> "USD".equals(c))) {
            //throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            throw new ResponseStatusException(BAD_REQUEST, "Currently, only USD is supported!");
        }

        var purchasingPowers = new PurchasingPower[2];
//        var purchasingPowers = await Task
//            .WhenAll(
//                _context.PurchasingPower
//                    .Where(row => row.Category == "N" && row.City == targetCity)
//            .OrderByDescending(row => row.Year)
//                    .FirstAsync(),
//            _context.PurchasingPower
//                .Where(row => row.Category == "N" && row.City == baseCity)
//                    .OrderByDescending(row => row.Year)
//                    .FirstAsync());

        if (Stream.of(purchasingPowers).anyMatch(x -> x == null)) {
            throw new ResponseStatusException(NOT_FOUND);
        }

        var targetPurchasingPower = purchasingPowers[0];
        var basePurchasingPower = purchasingPowers[1];
        var result = (baseIncomeAmount / basePurchasingPower.getValue()) * targetPurchasingPower.getValue();

        return ResponseEntity.ok().body(result);
    }
}
