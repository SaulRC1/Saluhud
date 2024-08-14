package com.uhu.saluhud.models.user.test;

import com.uhu.saluhud.database.utils.models.user.DailyStepsHistorical;
import com.uhu.saluhud.database.utils.models.user.DailyStepsHistoricalEntry;
import com.uhu.saluhud.database.utils.models.user.HistoricalEvaluation;
import com.uhu.saluhud.database.utils.models.user.SaluhudUser;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.user.SaluhudAdminDailyStepsHistoricalEntryRepository;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.user.SaluhudAdminDailyStepsHistoricalRepository;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.user.SaluhudAdminUserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 *
 * @author Juan Alberto Dominguez Vazquez
 */
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/saluhud",
    "spring.datasource.username=saluhud_admin",
    "spring.datasource.password=adminUHU495",
    "spring.datasource.driver-class-name=org.postgresql.Driver"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DailyStepsHistoricalTest {

    @Autowired
    private SaluhudAdminDailyStepsHistoricalRepository dailyStepsHistoricalRepository;

    @Autowired
    private SaluhudAdminDailyStepsHistoricalEntryRepository dailyStepsHistoricalEntryRepository;

    @Autowired
    private SaluhudAdminUserRepository saluhudUserRepository;

    @Test
    public void testDailyStepsHistoricalCRUD() {
        LocalDate now = LocalDate.now();

        HistoricalEvaluation stepsEvaluation = HistoricalEvaluation.EXCELLENT;
        DailyStepsHistorical dailyStepsHistorical = new DailyStepsHistorical();

        List<DailyStepsHistoricalEntry> historicalEntries = new ArrayList<>();
        DailyStepsHistoricalEntry entry = new DailyStepsHistoricalEntry(now, 9000, 400, stepsEvaluation, dailyStepsHistorical);
        historicalEntries.add(entry);

        dailyStepsHistorical.setEntries(historicalEntries);

        SaluhudUser user = new SaluhudUser("Juan2k2", "1235", "juan2@gmail.com");
        dailyStepsHistorical.setUser(user);

        saluhudUserRepository.save(user);
        dailyStepsHistoricalRepository.save(dailyStepsHistorical);
        dailyStepsHistoricalEntryRepository.save(entry);
        
        // Recuperar todas las entradas históricas del usuario "Juan2k2"
        List<DailyStepsHistoricalEntry> userEntries = dailyStepsHistoricalEntryRepository.findAllByUserUsername("Juan2k2");

        // Verificar si la entrada recién añadida está presente en el histórico
        assertTrue(userEntries.contains(entry), "La entrada recién añadida no se encuentra en el histórico del usuario.");       
    }
}
