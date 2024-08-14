package com.uhu.saluhud.models.nutrition.test;

import com.uhu.saluhud.database.utils.models.nutrition.Ingredient;
import com.uhu.saluhud.database.utils.repositories.saluhud.admin.nutrition.SaluhudAdminIngredientRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

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
public class SaluhudAdminIngredientsTest {

    @Autowired
    private SaluhudAdminIngredientRepository ingredientRepository;

    @Test
    public void testIngredientCRUD() {
        Ingredient harina = new Ingredient("Harina", 364, 10, 73, 1);
        Ingredient carneDeRes = new Ingredient("Carne de Res", 250, 26, 0, 17);
        Ingredient huevo = new Ingredient("Huevo", 68, 6, 0, 5);
        Ingredient lechuga = new Ingredient("Lechuga", 5, 1, 2, 0);

        ingredientRepository.save(harina);
        ingredientRepository.save(carneDeRes);
        ingredientRepository.save(huevo);
        ingredientRepository.save(lechuga);

        Ingredient ingredientSelectedById = ingredientRepository.findOne(harina.getId());
        assertEquals(ingredientSelectedById.getName(), "Harina");

        Ingredient ingredientSelectedByName = ingredientRepository.findByName("Huevo");
        assertEquals(ingredientSelectedByName.getName(), "Huevo");

        ingredientRepository.delete(lechuga);
        Assert.isNull(this.ingredientRepository.findByName("Lechuga"), "");
    }
}