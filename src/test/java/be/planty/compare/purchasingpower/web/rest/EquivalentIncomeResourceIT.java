package be.planty.compare.purchasingpower.web.rest;

import be.planty.compare.purchasingpower.PlantyComparePurchasingPowerApp;
import be.planty.compare.purchasingpower.repository.PurchasingPowerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the EquivalentIncomeResource REST controller.
 *
 * @see EquivalentIncomeResource
 */
@SpringBootTest(classes = PlantyComparePurchasingPowerApp.class)
public class EquivalentIncomeResourceIT {

    private MockMvc restMockMvc;

    @Autowired
    private PurchasingPowerRepository purchasingPowerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        EquivalentIncomeResource equivalentIncomeResource = new EquivalentIncomeResource(purchasingPowerRepository);
        restMockMvc = MockMvcBuilders
            .standaloneSetup(equivalentIncomeResource)
            .build();
    }

    /**
     * Test calculateEquivalentIncome
     */
    @Test
    public void testCalculateEquivalentIncome() throws Exception {
        //restMockMvc.perform(get("/api/equivalent-income/calculate-equivalent-income"))
        restMockMvc.perform(get("/api/equivalent-income"))
            .andExpect(status().isOk());
    }
}
