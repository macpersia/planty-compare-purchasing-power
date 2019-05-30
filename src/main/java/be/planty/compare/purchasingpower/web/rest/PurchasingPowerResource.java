package be.planty.compare.purchasingpower.web.rest;

import be.planty.compare.purchasingpower.domain.PurchasingPower;
import be.planty.compare.purchasingpower.repository.PurchasingPowerRepository;
import be.planty.compare.purchasingpower.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link be.planty.compare.purchasingpower.domain.PurchasingPower}.
 */
@RestController
@RequestMapping("/api")
public class PurchasingPowerResource {

    private final Logger log = LoggerFactory.getLogger(PurchasingPowerResource.class);

    private static final String ENTITY_NAME = "plantyComparePurchasingPowerPurchasingPower";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchasingPowerRepository purchasingPowerRepository;

    public PurchasingPowerResource(PurchasingPowerRepository purchasingPowerRepository) {
        this.purchasingPowerRepository = purchasingPowerRepository;
    }

    /**
     * {@code POST  /purchasing-powers} : Create a new purchasingPower.
     *
     * @param purchasingPower the purchasingPower to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchasingPower, or with status {@code 400 (Bad Request)} if the purchasingPower has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchasing-powers")
    public ResponseEntity<PurchasingPower> createPurchasingPower(@RequestBody PurchasingPower purchasingPower) throws URISyntaxException {
        log.debug("REST request to save PurchasingPower : {}", purchasingPower);
        if (purchasingPower.getId() != null) {
            throw new BadRequestAlertException("A new purchasingPower cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchasingPower result = purchasingPowerRepository.save(purchasingPower);
        return ResponseEntity.created(new URI("/api/purchasing-powers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchasing-powers} : Updates an existing purchasingPower.
     *
     * @param purchasingPower the purchasingPower to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasingPower,
     * or with status {@code 400 (Bad Request)} if the purchasingPower is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchasingPower couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchasing-powers")
    public ResponseEntity<PurchasingPower> updatePurchasingPower(@RequestBody PurchasingPower purchasingPower) throws URISyntaxException {
        log.debug("REST request to update PurchasingPower : {}", purchasingPower);
        if (purchasingPower.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PurchasingPower result = purchasingPowerRepository.save(purchasingPower);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchasingPower.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /purchasing-powers} : get all the purchasingPowers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchasingPowers in body.
     */
    @GetMapping("/purchasing-powers")
    public ResponseEntity<List<PurchasingPower>> getAllPurchasingPowers(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of PurchasingPowers");
        Page<PurchasingPower> page = purchasingPowerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchasing-powers/:id} : get the "id" purchasingPower.
     *
     * @param id the id of the purchasingPower to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchasingPower, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchasing-powers/{id}")
    public ResponseEntity<PurchasingPower> getPurchasingPower(@PathVariable String id) {
        log.debug("REST request to get PurchasingPower : {}", id);
        Optional<PurchasingPower> purchasingPower = purchasingPowerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(purchasingPower);
    }

    /**
     * {@code DELETE  /purchasing-powers/:id} : delete the "id" purchasingPower.
     *
     * @param id the id of the purchasingPower to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchasing-powers/{id}")
    public ResponseEntity<Void> deletePurchasingPower(@PathVariable String id) {
        log.debug("REST request to delete PurchasingPower : {}", id);
        purchasingPowerRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
