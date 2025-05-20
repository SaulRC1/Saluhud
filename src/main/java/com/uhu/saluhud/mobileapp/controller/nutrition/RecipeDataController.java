package com.uhu.saluhud.mobileapp.controller.nutrition;

import com.uhu.saluhud.mobileapp.dto.nutrition.IngredientDTO;
import com.uhu.saluhuddatabaseutils.localization.NutritionLocaleProvider;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeCardAllergenicDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeCardDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeDetailDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeElaborationStepDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeIngredientDTO;
import com.uhu.saluhud.mobileapp.response.ApiErrorResponse;
import com.uhu.saluhud.mobileapp.service.MobileAppHttpRequestService;
import com.uhu.saluhuddatabaseutils.models.nutrition.Allergenic;
import com.uhu.saluhuddatabaseutils.models.nutrition.AllergenicEnum;
import com.uhu.saluhuddatabaseutils.models.nutrition.Recipe;
import com.uhu.saluhuddatabaseutils.models.nutrition.RecipeElaborationStep;
import com.uhu.saluhuddatabaseutils.models.nutrition.RecipeIngredient;
import com.uhu.saluhuddatabaseutils.services.mobileapp.nutrition.MobileAppRecipeService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import kotlin.collections.ArrayDeque;
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
    
    @GetMapping("/recipe-detail-data")
    public ResponseEntity<Object> getRecipeDetailData(HttpServletRequest request, 
            @RequestParam Long recipeID)
    {   
        RecipeDetailDTO recipeDetailDTO = new RecipeDetailDTO();
        
        Optional<Recipe> recipeOptional = this.mobileAppRecipeService.findById(recipeID);
        
        if(recipeOptional.isEmpty())
        {
            ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), 
                    "Recipe with recipe ID: " + recipeID + " not found");
            
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        
        Recipe recipe = recipeOptional.get();
        Locale mobileAppLocale = mobileAppHttpRequestService.getMobileAppLocale(request);
        
        recipeDetailDTO.setName(nutritionLocaleProvider.getTranslation(recipe.getName(), 
                NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX, mobileAppLocale));
        
        recipeDetailDTO.setDescription(nutritionLocaleProvider.getTranslation(recipe.getDescription(), 
                NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX, mobileAppLocale));
        
        recipeDetailDTO.setIngredientsDescription(nutritionLocaleProvider.getTranslation(recipe.getIngredientsDescription(), 
                NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX, mobileAppLocale));
        
        recipeDetailDTO.setKilocalories(recipe.getKilocalories());
        recipeDetailDTO.setImageSource(recipe.getImageSource());
        
        List<RecipeCardAllergenicDTO> recipeAllergenicsDTO = new ArrayList<>();
        
        for (Allergenic allergenic : recipe.getAllergenics())
        {
            RecipeCardAllergenicDTO allergenicDTO = new RecipeCardAllergenicDTO(allergenic.getId(), 
                    nutritionLocaleProvider.getTranslation(allergenic.getName(),
                                        NutritionLocaleProvider.ALLERGENIC_TRANSLATION_BUNDLE_PREFIX,
                                        mobileAppHttpRequestService.getMobileAppLocale(request)));
            
            recipeAllergenicsDTO.add(allergenicDTO);
        }
        
        recipeDetailDTO.setRecipeAllergenics(recipeAllergenicsDTO);
        
        List<RecipeElaborationStepDTO> recipeElaborationStepsDTO = new ArrayList<>();
        
        for (RecipeElaborationStep elaborationStep : recipe.getElaborationSteps())
        {
            RecipeElaborationStepDTO recipeElaborationStepDTO = new RecipeElaborationStepDTO();
            
            recipeElaborationStepDTO.setStepNumber(elaborationStep.getStepNumber());
            recipeElaborationStepDTO.setStepDescription(nutritionLocaleProvider.getTranslation(elaborationStep.getStepDescription(),
                                        NutritionLocaleProvider.RECIPE_ELABORATION_STEPS_TRANSLATION_BUNDLE_PREFIX,
                                        mobileAppHttpRequestService.getMobileAppLocale(request)));
            
            recipeElaborationStepsDTO.add(recipeElaborationStepDTO);
        }
        
        recipeDetailDTO.setElaborationSteps(recipeElaborationStepsDTO);
        
        List<RecipeIngredientDTO> recipeIngredientsDTO = new ArrayList<>();
        
        for (RecipeIngredient recipeIngredient : recipe.getRecipeIngredients())
        {
            RecipeIngredientDTO recipeIngredientDTO = new RecipeIngredientDTO();
            
            recipeIngredientDTO.setUnit(nutritionLocaleProvider.getTranslation(recipeIngredient.getUnit(),
                                        NutritionLocaleProvider.RECIPE_INGREDIENT_TRANSLATION_BUNDLE_PREFIX,
                                        mobileAppHttpRequestService.getMobileAppLocale(request)));
            recipeIngredientDTO.setQuantity(recipeIngredient.getQuantity());
            
            IngredientDTO ingredientDTO = new IngredientDTO();
            
            ingredientDTO.setName(nutritionLocaleProvider.getTranslation(recipeIngredient.getIngredient().getName(),
                                        NutritionLocaleProvider.INGREDIENTS_TRANSLATION_BUNDLE_PREFIX,
                                        mobileAppHttpRequestService.getMobileAppLocale(request)));
            ingredientDTO.setKilocalories(recipeIngredient.getIngredient().getKilocalories());
            ingredientDTO.setProteinAmount(recipeIngredient.getIngredient().getProteinAmount());
            ingredientDTO.setCarbohydratesAmount(recipeIngredient.getIngredient().getCarbohydratesAmount());
            ingredientDTO.setFatAmount(recipeIngredient.getIngredient().getFatAmount());
            
            recipeIngredientDTO.setIngredient(ingredientDTO);
            
            recipeIngredientsDTO.add(recipeIngredientDTO);
        }
        
        recipeDetailDTO.setRecipeIngredients(recipeIngredientsDTO);
        
        return new ResponseEntity<>(recipeDetailDTO, HttpStatus.OK);
    }
}
