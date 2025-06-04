package com.uhu.saluhud.mobileapp.controller.nutrition;

import com.uhu.saluhud.administrationportal.dto.nutrition.AllergenicDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.IngredientDTO;
import com.uhu.saluhuddatabaseutils.localization.NutritionLocaleProvider;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeCardAllergenicDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeCardDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeDetailDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeElaborationStepDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeIngredientDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeNameAndIdDataDTO;
import com.uhu.saluhud.mobileapp.localization.MobileAppLocaleProvider;
import com.uhu.saluhud.mobileapp.response.ApiErrorResponse;
import com.uhu.saluhud.mobileapp.service.JWTService;
import com.uhu.saluhud.mobileapp.service.MobileAppHttpRequestService;
import com.uhu.saluhuddatabaseutils.models.nutrition.Allergenic;
import com.uhu.saluhuddatabaseutils.models.nutrition.AllergenicEnum;
import com.uhu.saluhuddatabaseutils.models.nutrition.Recipe;
import com.uhu.saluhuddatabaseutils.models.nutrition.RecipeElaborationStep;
import com.uhu.saluhuddatabaseutils.models.nutrition.RecipeIngredient;
import static com.uhu.saluhuddatabaseutils.models.user.FitnessTargetEnum.MAINTENANCE;
import static com.uhu.saluhuddatabaseutils.models.user.FitnessTargetEnum.WEIGHT_GAIN;
import static com.uhu.saluhuddatabaseutils.models.user.FitnessTargetEnum.WEIGHT_LOSS;
import com.uhu.saluhuddatabaseutils.models.user.SaluhudUserFitnessData;
import com.uhu.saluhuddatabaseutils.services.mobileapp.nutrition.MobileAppRecipeService;
import com.uhu.saluhuddatabaseutils.services.mobileapp.user.MobileAppSaluhudUserService;
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
    
    @Autowired
    private MobileAppSaluhudUserService mobileAppSaluhudUserService;
    
    @Autowired
    private JWTService jwtService;
    
    @Autowired
    private MobileAppLocaleProvider mobileAppLocaleProvider;
    
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
    
    @GetMapping("/recipes-name-and-id-data")
    public ResponseEntity<Object> getRecipesNameAndIdData(HttpServletRequest request)
    {           
        List<Recipe> allRecipes = this.mobileAppRecipeService.findAll();
        
        List<RecipeNameAndIdDataDTO> recipesNameAndIdData
                = allRecipes.stream().map((recipe) -> new RecipeNameAndIdDataDTO(recipe.getId(),
                nutritionLocaleProvider.getTranslation(recipe.getName(),
                        NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX,
                        mobileAppHttpRequestService.getMobileAppLocale(request)))).toList();
        
        return new ResponseEntity<>(recipesNameAndIdData, HttpStatus.OK);
    }
    
    @GetMapping("/recipe-recommendations")
    public ResponseEntity<Object> getRecipeRecommendations(HttpServletRequest request)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);

        String username = jwtService.extractUsername(jwt);

        try
        {
            Optional<SaluhudUserFitnessData> saluhudUserFitnessDataOptional
                    = this.mobileAppSaluhudUserService.getSaluhudUserFitnessData(username);

            if (saluhudUserFitnessDataOptional.isEmpty())
            {
                List<Recipe> recipes = this.mobileAppRecipeService.findRecipesPaginated(0, 10);

                List<RecipeCardDTO> recipeCardDTOs = this.mapRecipeToRecipeCardDTO(recipes,
                        this.mobileAppHttpRequestService.getMobileAppLocale(request));

                return new ResponseEntity<>(recipeCardDTOs, HttpStatus.OK);
            }

            List<Recipe> recipes = Collections.emptyList();

            switch (saluhudUserFitnessDataOptional.get().getFitnessTarget())
            {
                case WEIGHT_GAIN:
                    recipes = this.mobileAppRecipeService.findMostKaloricRecipesPaginated(0, 10);
                    break;
                case WEIGHT_LOSS:
                    recipes = this.mobileAppRecipeService.findLessKaloricRecipesPaginated(0, 10);
                    break;
                case MAINTENANCE:
                    recipes = this.mobileAppRecipeService.findRecipesPaginated(0, 10);
                    break;
            }

            return new ResponseEntity<>(this.mapRecipeToRecipeCardDTO(recipes,
                    this.mobileAppHttpRequestService.getMobileAppLocale(request)), HttpStatus.OK);
        } 
        catch (Exception e)
        {
            return this.buildApiErrorResponse("recipeRecommendations.unknownError", request, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private ResponseEntity<Object> buildApiErrorResponse(String errorMessageTranslationKey, HttpServletRequest request, HttpStatus status)
    {
        String errorResponseMessage = mobileAppLocaleProvider.getTranslation(errorMessageTranslationKey, 
                        MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
        ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), errorResponseMessage);
            
        return new ResponseEntity<>(errorResponse, status);
    }
    
    private List<RecipeCardDTO> mapRecipeToRecipeCardDTO(List<Recipe> recipes, Locale locale)
    {
        List<RecipeCardDTO> recipeCardList = new ArrayList<>();
        
        for (Recipe recipe : recipes)
        {
            Set<Allergenic> allergenics = recipe.getAllergenics();
            List<RecipeCardAllergenicDTO> recipeCardAllergenics = new ArrayList<>();
            
            for (Allergenic allergenic : allergenics)
            {
                RecipeCardAllergenicDTO allergenicDTO = new RecipeCardAllergenicDTO(allergenic.getId(), allergenic.getName());
                recipeCardAllergenics.add(allergenicDTO);
            }
            
            String localizedRecipeName = this.nutritionLocaleProvider.getTranslation(recipe.getName(), 
                    NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX, 
                    locale);
            
            RecipeCardDTO recipeCardDTO = new RecipeCardDTO(recipe.getId(), localizedRecipeName, 
                    recipe.getKilocalories(), recipe.getImageSource(), recipeCardAllergenics);
            
            recipeCardList.add(recipeCardDTO);
        }
        
        return recipeCardList;
    }
}
