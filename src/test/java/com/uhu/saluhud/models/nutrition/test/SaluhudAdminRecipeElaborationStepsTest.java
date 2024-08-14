package com.uhu.saluhud.models.nutrition.test;

import com.uhu.saluhud.database.utils.models.nutrition.Allergenic;
import com.uhu.saluhud.database.utils.models.nutrition.Ingredient;
import com.uhu.saluhud.database.utils.models.nutrition.Recipe;
import com.uhu.saluhud.database.utils.models.nutrition.RecipeElaborationStep;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.nutrition.SaluhudAdminAllergenicRepository;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.nutrition.SaluhudAdminIngredientRepository;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.nutrition.SaluhudAdminRecipeElaborationStepRepository;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.nutrition.SaluhudAdminRecipeRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 *
 * @author Juan Alberto Dominguez Vazquez
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/saluhud",
    "spring.datasource.username=saluhud_admin",
    "spring.datasource.password=adminUHU495",
    "spring.datasource.driver-class-name=org.postgresql.Driver"
})
public class SaluhudAdminRecipeElaborationStepsTest {

    @Autowired
    private SaluhudAdminRecipeElaborationStepRepository recipeElaborationStepRepository;

    @Autowired
    private SaluhudAdminRecipeRepository recipeRepository;

    @Autowired
    private SaluhudAdminIngredientRepository ingredientRepository;

    @Autowired
    private SaluhudAdminAllergenicRepository allergenicRepository;

    @Test
    public void testRecipeElaborationStepsTestCRUD() {
        List<RecipeElaborationStep> elaborationSteps = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        Set<Allergenic> allergenics = new HashSet<>();

        Ingredient harina = new Ingredient("Harina", 364, 10, 73, 1);
        Ingredient huevo = new Ingredient("Huevo", 68, 6, 0, 5);
        Allergenic leche = new Allergenic("Leche");
        RecipeElaborationStep batirHuevos = new RecipeElaborationStep("Batir los huevos bien, a mano o con la batidora durante 5 min", 1);

        ingredientRepository.save(harina);
        ingredientRepository.save(huevo);
        allergenicRepository.save(leche);

        ingredients.add(huevo);
        ingredients.add(harina);
        allergenics.add(leche);
        elaborationSteps.add(batirHuevos);

        Recipe Bizcocho = new Recipe("Bizcocho", "Bizcocho de limón al horno", "Huevos, harina, sal, azucar, agua, aceite", ingredients, allergenics, elaborationSteps);

        recipeElaborationStepRepository.save(batirHuevos);
        recipeRepository.save(Bizcocho);
    }
}
