package com.uhu.saluhud.mobileapp.controller.nutrition;

import com.uhu.saluhuddatabaseutils.localization.NutritionLocaleProvider;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeCardAllergenicDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeCardDTO;
import com.uhu.saluhud.mobileapp.service.MobileAppHttpRequestService;
import com.uhu.saluhuddatabaseutils.models.nutrition.Allergenic;
import com.uhu.saluhuddatabaseutils.models.nutrition.AllergenicEnum;
import com.uhu.saluhuddatabaseutils.models.nutrition.Recipe;
import com.uhu.saluhuddatabaseutils.services.mobileapp.nutrition.MobileAppRecipeService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
@RestController
@RequestMapping("/saluhud-mobile-app/recipe-data")
public class RecipeDataController 
{
    @Autowired
    private MobileAppRecipeService mobileAppRecipeService;
    
    @Autowired
    private MobileAppHttpRequestService mobileAppHttpRequestService;
    
    @Autowired
    private NutritionLocaleProvider nutritionLocaleProvider;
    
    @GetMapping("/recipe-card-data")
    public ResponseEntity<Object> getRecipeCardData(HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int minimumKilocalories,
            @RequestParam(defaultValue = "2147483647") int maximumKilocalories,
            @RequestParam(defaultValue = "") Integer[] excludedAllergenicCodes,
            @RequestParam(required = false) String filterRecipeName)
    {
        List<AllergenicEnum> excludedAllergenics = Collections.emptyList();
        
        if(excludedAllergenicCodes != null && excludedAllergenicCodes.length > 0)
        {
            excludedAllergenics = new ArrayList<>();

            for (int i = 0; i < excludedAllergenicCodes.length; i++)
            {
                AllergenicEnum allergenic = AllergenicEnum.fromAllergenicCode(excludedAllergenicCodes[i]);

                if (allergenic != null)
                {
                    excludedAllergenics.add(allergenic);
                }
            }
        }
                
        List<Recipe> allRecipes = mobileAppRecipeService.findRecipesPaginatedWithFilters(page, pageSize, 
                minimumKilocalories, maximumKilocalories, excludedAllergenics, filterRecipeName, 
                mobileAppHttpRequestService.getMobileAppLocale(request));
        
        List<RecipeCardDTO> allRecipeCards = new ArrayList<>(allRecipes.size());
        
        allRecipes.forEach(recipe -> 
        {
            Set<Allergenic> recipeAllergenics = recipe.getAllergenics();
            List<RecipeCardAllergenicDTO> recipeAllergenicsDTO = new ArrayList<>();
            
            for (Allergenic recipeAllergenic : recipeAllergenics)
            {
                RecipeCardAllergenicDTO recipeAllergenicDTO
                        = new RecipeCardAllergenicDTO(recipeAllergenic.getId(),
                                nutritionLocaleProvider.getTranslation(recipeAllergenic.getName(),
                                        NutritionLocaleProvider.ALLERGENIC_TRANSLATION_BUNDLE_PREFIX,
                                        mobileAppHttpRequestService.getMobileAppLocale(request)));
                recipeAllergenicsDTO.add(recipeAllergenicDTO);
            }
            
            RecipeCardDTO recipeCardDTO = new RecipeCardDTO(recipe.getId(), 
                    nutritionLocaleProvider.getTranslation(recipe.getName(), 
                            NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX, 
                            mobileAppHttpRequestService.getMobileAppLocale(request)), 
                    recipe.getKilocalories(), recipe.getImageSource(), recipeAllergenicsDTO);
            
            allRecipeCards.add(recipeCardDTO);
        });
        
        return new ResponseEntity<>(allRecipeCards, HttpStatus.OK);
    }
}
