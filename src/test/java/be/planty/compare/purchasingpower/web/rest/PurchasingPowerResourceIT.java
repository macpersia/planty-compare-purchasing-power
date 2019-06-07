package be.planty.compare.purchasingpower.web.rest;

import be.planty.compare.purchasingpower.PlantyComparePurchasingPowerApp;
import be.planty.compare.purchasingpower.config.SecurityBeanOverrideConfiguration;
import be.planty.compare.purchasingpower.domain.PurchasingPower;
import be.planty.compare.purchasingpower.repository.PurchasingPowerRepository;
import be.planty.compare.purchasingpower.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.util.List;

import static be.planty.compare.purchasingpower.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link PurchasingPowerResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PlantyComparePurchasingPowerApp.class})
public class PurchasingPowerResourceIT {

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    @Autowired
    private PurchasingPowerRepository purchasingPowerRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restPurchasingPowerMockMvc;

    private PurchasingPower purchasingPower;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchasingPowerResource purchasingPowerResource = new PurchasingPowerResource(purchasingPowerRepository);
        this.restPurchasingPowerMockMvc = MockMvcBuilders.standaloneSetup(purchasingPowerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasingPower createEntity() {
        PurchasingPower purchasingPower = new PurchasingPower()
            .year(DEFAULT_YEAR)
            .city(DEFAULT_CITY)
            .category(DEFAULT_CATEGORY)
            .value(DEFAULT_VALUE);
        return purchasingPower;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasingPower createUpdatedEntity() {
        PurchasingPower purchasingPower = new PurchasingPower()
            .year(UPDATED_YEAR)
            .city(UPDATED_CITY)
            .category(UPDATED_CATEGORY)
            .value(UPDATED_VALUE);
        return purchasingPower;
    }

    @BeforeEach
    public void initTest() {
        purchasingPowerRepository.deleteAll();
        purchasingPower = createEntity();
    }

    @Test
    public void createPurchasingPower() throws Exception {
        int databaseSizeBeforeCreate = purchasingPowerRepository.findAll().size();

        // Create the PurchasingPower
        restPurchasingPowerMockMvc.perform(post("/api/purchasing-powers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchasingPower)))
            .andExpect(status().isCreated());

        // Validate the PurchasingPower in the database
        List<PurchasingPower> purchasingPowerList = purchasingPowerRepository.findAll();
        assertThat(purchasingPowerList).hasSize(databaseSizeBeforeCreate + 1);
        PurchasingPower testPurchasingPower = purchasingPowerList.get(purchasingPowerList.size() - 1);
        assertThat(testPurchasingPower.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testPurchasingPower.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPurchasingPower.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testPurchasingPower.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    public void createPurchasingPowerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchasingPowerRepository.findAll().size();

        // Create the PurchasingPower with an existing ID
        purchasingPower.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchasingPowerMockMvc.perform(post("/api/purchasing-powers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchasingPower)))
            .andExpect(status().isBadRequest());

        // Validate the PurchasingPower in the database
        List<PurchasingPower> purchasingPowerList = purchasingPowerRepository.findAll();
        assertThat(purchasingPowerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllPurchasingPowers() throws Exception {
        // Initialize the database
        purchasingPowerRepository.save(purchasingPower);

        // Get all the purchasingPowerList
        restPurchasingPowerMockMvc.perform(get("/api/purchasing-powers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchasingPower.getId())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }
    
    @Test
    public void getPurchasingPower() throws Exception {
        // Initialize the database
        purchasingPowerRepository.save(purchasingPower);

        // Get the purchasingPower
        restPurchasingPowerMockMvc.perform(get("/api/purchasing-powers/{id}", purchasingPower.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchasingPower.getId()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    public void getNonExistingPurchasingPower() throws Exception {
        // Get the purchasingPower
        restPurchasingPowerMockMvc.perform(get("/api/purchasing-powers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePurchasingPower() throws Exception {
        // Initialize the database
        purchasingPowerRepository.save(purchasingPower);

        int databaseSizeBeforeUpdate = purchasingPowerRepository.findAll().size();

        // Update the purchasingPower
        PurchasingPower updatedPurchasingPower = purchasingPowerRepository.findById(purchasingPower.getId()).get();
        updatedPurchasingPower
            .year(UPDATED_YEAR)
            .city(UPDATED_CITY)
            .category(UPDATED_CATEGORY)
            .value(UPDATED_VALUE);

        restPurchasingPowerMockMvc.perform(put("/api/purchasing-powers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPurchasingPower)))
            .andExpect(status().isOk());

        // Validate the PurchasingPower in the database
        List<PurchasingPower> purchasingPowerList = purchasingPowerRepository.findAll();
        assertThat(purchasingPowerList).hasSize(databaseSizeBeforeUpdate);
        PurchasingPower testPurchasingPower = purchasingPowerList.get(purchasingPowerList.size() - 1);
        assertThat(testPurchasingPower.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testPurchasingPower.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPurchasingPower.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testPurchasingPower.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    public void updateNonExistingPurchasingPower() throws Exception {
        int databaseSizeBeforeUpdate = purchasingPowerRepository.findAll().size();

        // Create the PurchasingPower

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasingPowerMockMvc.perform(put("/api/purchasing-powers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchasingPower)))
            .andExpect(status().isBadRequest());

        // Validate the PurchasingPower in the database
        List<PurchasingPower> purchasingPowerList = purchasingPowerRepository.findAll();
        assertThat(purchasingPowerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deletePurchasingPower() throws Exception {
        // Initialize the database
        purchasingPowerRepository.save(purchasingPower);

        int databaseSizeBeforeDelete = purchasingPowerRepository.findAll().size();

        // Delete the purchasingPower
        restPurchasingPowerMockMvc.perform(delete("/api/purchasing-powers/{id}", purchasingPower.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<PurchasingPower> purchasingPowerList = purchasingPowerRepository.findAll();
        assertThat(purchasingPowerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasingPower.class);
        PurchasingPower purchasingPower1 = new PurchasingPower();
        purchasingPower1.setId("id1");
        PurchasingPower purchasingPower2 = new PurchasingPower();
        purchasingPower2.setId(purchasingPower1.getId());
        assertThat(purchasingPower1).isEqualTo(purchasingPower2);
        purchasingPower2.setId("id2");
        assertThat(purchasingPower1).isNotEqualTo(purchasingPower2);
        purchasingPower1.setId(null);
        assertThat(purchasingPower1).isNotEqualTo(purchasingPower2);
    }
}
