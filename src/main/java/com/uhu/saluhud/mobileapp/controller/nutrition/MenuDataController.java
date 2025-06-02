package com.uhu.saluhud.mobileapp.controller.nutrition;

import com.uhu.saluhud.mobileapp.dto.nutrition.AddMenuDayToMenuDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.AddRecipeToMenuDayDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.CreateNewMenuDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.EditMenuDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.MenuCardDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.MenuDayDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.MenuDayDetailDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.MenuDayRecipeDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeCardAllergenicDTO;
import com.uhu.saluhud.mobileapp.dto.nutrition.RecipeCardDTO;
import com.uhu.saluhud.mobileapp.localization.MobileAppLocaleProvider;
import com.uhu.saluhud.mobileapp.response.ApiErrorResponse;
import com.uhu.saluhud.mobileapp.response.ApiInformationResponse;
import com.uhu.saluhud.mobileapp.service.JWTService;
import com.uhu.saluhud.mobileapp.service.MobileAppHttpRequestService;
import com.uhu.saluhuddatabaseutils.localization.NutritionLocaleProvider;
import com.uhu.saluhuddatabaseutils.models.nutrition.Allergenic;
import com.uhu.saluhuddatabaseutils.models.nutrition.Menu;
import com.uhu.saluhuddatabaseutils.models.nutrition.MenuDay;
import com.uhu.saluhuddatabaseutils.models.nutrition.MenuDayRecipe;
import com.uhu.saluhuddatabaseutils.models.nutrition.Recipe;
import com.uhu.saluhuddatabaseutils.models.nutrition.WeekDayEnum;
import com.uhu.saluhuddatabaseutils.models.user.SaluhudUser;
import com.uhu.saluhuddatabaseutils.services.mobileapp.nutrition.MobileAppMenuService;
import com.uhu.saluhuddatabaseutils.services.mobileapp.nutrition.MobileAppMenuUtilsService;
import com.uhu.saluhuddatabaseutils.services.mobileapp.nutrition.MobileAppRecipeService;
import com.uhu.saluhuddatabaseutils.services.mobileapp.user.MobileAppSaluhudUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Saúl Rodríguez Naranjo
 */
@RestController
@RequestMapping("/saluhud-mobile-app/menu-data")
public class MenuDataController 
{

    private static final Logger LOGGER = Logger.getLogger(MenuDataController.class.getName());
    
    @Autowired
    private MobileAppMenuService mobileAppMenuService;
    
    @Autowired
    private MobileAppHttpRequestService mobileAppHttpRequestService;
    
    @Autowired
    private NutritionLocaleProvider nutritionLocaleProvider;
    
    @Autowired
    private JWTService jwtService;
    
    @Autowired
    private MobileAppLocaleProvider mobileAppLocaleProvider;
    
    @Autowired
    private MobileAppSaluhudUserService mobileAppSaluhudUserService;
    
    @Autowired
    private MobileAppMenuUtilsService mobileAppMenuUtilsService;
    
    @Autowired
    private MobileAppRecipeService mobileAppRecipeService;
    
    @GetMapping("/menu-card-data")
    public ResponseEntity<Object> getMenuCardData(HttpServletRequest request)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        List<Menu> userMenus = mobileAppMenuService.findByUsername(username);
        
        if(userMenus.isEmpty())
        {
            ApiErrorResponse errorResponse =  new ApiErrorResponse(request.getServletPath(), 
                    "The user \"" + username + "\" has no menus created.");
            
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        
        List<MenuCardDTO> menuCards = new ArrayList<>();
        
        for (Menu userMenu : userMenus)
        {
            MenuCardDTO menuCard = new MenuCardDTO(userMenu.getId(), userMenu.getName(), userMenu.isFavourite());
            
            menuCards.add(menuCard);
        }
        
        menuCards.sort(Comparator.comparing(menuCard -> menuCard.getMenuName().toLowerCase()));
        
        return new ResponseEntity<>(menuCards, HttpStatus.OK);
    }
    
    @GetMapping("/menu-detail-data")
    public ResponseEntity<Object> getMenuDetailData(HttpServletRequest request, @RequestParam Long menuID)
    {        
        List<MenuDay> menuDays = mobileAppMenuService.findMenuDaysByMenuId(menuID);
        
        if(menuDays.isEmpty())
        {
            ApiErrorResponse errorResponse =  new ApiErrorResponse(request.getServletPath(), 
                    "This menu has no data inside.");
            
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        
        List<MenuDayDTO> menuDaysDTOList = new ArrayList<>();
        
        for (MenuDay menuDay : menuDays)
        {
            MenuDayDTO menuDayDTO = new MenuDayDTO();
            menuDayDTO.setId(menuDay.getId());
            menuDayDTO.setWeekDay(menuDay.getWeekDay().getWeekDayName());
            
            menuDaysDTOList.add(menuDayDTO);
        }
        
        return new ResponseEntity<>(menuDaysDTOList, HttpStatus.OK);
    }
    
    @GetMapping("/menu-day-detail-data")
    public ResponseEntity<Object> getMenuDayDetailData(HttpServletRequest request, @RequestParam Long menuDayID)
    {        
        Optional<MenuDay> menuDayOptional = mobileAppMenuService.findMenuDayById(menuDayID);
        
        if(menuDayOptional.isEmpty())
        {
            ApiErrorResponse errorResponse =  new ApiErrorResponse(request.getServletPath(), 
                    "The menu day requested does not exist.");
            
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        
        MenuDay menuDay = menuDayOptional.get();
        
        MenuDayDetailDTO menuDayDetailDTO = new MenuDayDetailDTO();
        menuDayDetailDTO.setId(menuDay.getId());
        menuDayDetailDTO.setWeekDay(menuDay.getWeekDay().getWeekDayName());
        
        List<MenuDayRecipeDTO> menuDayRecipeDTOList = new ArrayList<>();
        List<MenuDayRecipe> menuDayRecipes = menuDay.getMenuDayRecipes();
        
        menuDayRecipes.sort(Comparator.comparing(MenuDayRecipe::getStartTime));
        
        for (MenuDayRecipe menuDayRecipe : menuDayRecipes)
        {
            MenuDayRecipeDTO menuDayRecipeDTO = new MenuDayRecipeDTO();
            menuDayRecipeDTO.setId(menuDayRecipe.getId());
            menuDayRecipeDTO.setStartTime(menuDayRecipe.getStartTime().toString());
            menuDayRecipeDTO.setEndTime(menuDayRecipe.getEndTime().toString());
            
            Set<Allergenic> recipeAllergenics = menuDayRecipe.getRecipe().getAllergenics();
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
            
            RecipeCardDTO recipeCardDTO = new RecipeCardDTO(menuDayRecipe.getRecipe().getId(), 
                    nutritionLocaleProvider.getTranslation(menuDayRecipe.getRecipe().getName(), 
                            NutritionLocaleProvider.RECIPES_TRANSLATION_BUNDLE_PREFIX, 
                            mobileAppHttpRequestService.getMobileAppLocale(request)), 
                    menuDayRecipe.getRecipe().getKilocalories(), menuDayRecipe.getRecipe().getImageSource(), 
                    recipeAllergenicsDTO);
            
            menuDayRecipeDTO.setRecipe(recipeCardDTO);
            
            menuDayRecipeDTOList.add(menuDayRecipeDTO);
        }
        
        menuDayDetailDTO.setMenuDayRecipes(menuDayRecipeDTOList);
        
        return new ResponseEntity<>(menuDayDetailDTO, HttpStatus.OK);
    }
    
    @PostMapping("/create-new-menu")
    public ResponseEntity<Object> postCreateNewMenu(HttpServletRequest request, @RequestBody @Valid CreateNewMenuDTO createNewMenuDTO)
    {        
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        if(this.mobileAppMenuService.existsMenuByNameForUsername(createNewMenuDTO.getMenuName(), username))
        {
            String errorResponseMessage = mobileAppLocaleProvider.getTranslation("menuCreation.menuAlreadyExistsForThisUser", 
                        MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), errorResponseMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }
        
        Menu menu = new Menu();
        menu.setName(createNewMenuDTO.getMenuName());
        
        try
        {
            this.mobileAppMenuService.saveMenuForUser(menu, username);
            
            String successResponseMessage = mobileAppLocaleProvider.getTranslation("menuCreation.succesfullyCreatedMenu", 
                        MobileAppLocaleProvider.MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiInformationResponse successResponse = new ApiInformationResponse(request.getServletPath(), successResponseMessage);
            
            return new ResponseEntity<>(successResponse, HttpStatus.OK);  
        } 
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            String errorResponseMessage = mobileAppLocaleProvider.getTranslation("general.unknownError", 
                        MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), errorResponseMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/add-menu-day-to-menu")
    public ResponseEntity<Object> postAddMenuDayToMenu(HttpServletRequest request, @RequestBody @Valid AddMenuDayToMenuDTO addMenuDayToMenuDTO)
    {        
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        try
        {
            Optional<Menu> menuOptional = this.mobileAppMenuService.findMenuById(addMenuDayToMenuDTO.getMenuID());
            
            if(menuOptional.isEmpty())
            {
                ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), "The menu does not exist.");
                
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            
            Menu menu = menuOptional.get();
            
            Optional<SaluhudUser> userOptional = this.mobileAppSaluhudUserService.findByUsername(username);
            
            if (userOptional.isEmpty())
            {
                ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), "User trying to do this operation does not exist.");
                
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            SaluhudUser user = userOptional.get();
            
            if(menu.getUserId() != user.getId())
            {
                ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), "This menu cannot be modified by the current user.");
                
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            }
            
            if(mobileAppMenuUtilsService.menuContainsMenuDay(menu, WeekDayEnum.fromWeekDayName(addMenuDayToMenuDTO.getWeekDay())))
            {
                String errorResponseMessage = mobileAppLocaleProvider.getTranslation("menuModification.menuDayAlreadyExistsInsideMenu", 
                        MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
                ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), errorResponseMessage);
                
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            this.mobileAppMenuService.saveMenuDayForMenu(menu, WeekDayEnum.fromWeekDayName(addMenuDayToMenuDTO.getWeekDay()));
            
            String successResponseMessage = mobileAppLocaleProvider.getTranslation("menuModification.addedMenuDaySuccesfully", 
                        MobileAppLocaleProvider.MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiInformationResponse successResponse = new ApiInformationResponse(request.getServletPath(), successResponseMessage);
            
            return new ResponseEntity<>(successResponse, HttpStatus.OK);  
        } 
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            String errorResponseMessage = mobileAppLocaleProvider.getTranslation("general.unknownError", 
                        MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), errorResponseMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/add-recipe-to-menu-day")
    public ResponseEntity<Object> postAddRecipeToMenuDay(HttpServletRequest request, @RequestBody @Valid AddRecipeToMenuDayDTO addRecipeToMenuDayDTO)
    {                
        try
        {
            Optional<MenuDay> menuDayOptional = this.mobileAppMenuService.findMenuDayById(addRecipeToMenuDayDTO.getMenuDayId());
            
            if(menuDayOptional.isEmpty())
            {
                return this.buildApiErrorResponse("menuDayModification.menuDayDoesNotExist", request, HttpStatus.NOT_FOUND);
            }
            
            Optional<Recipe> recipeOptional = this.mobileAppRecipeService.findById(addRecipeToMenuDayDTO.getRecipeId());
            
            if(recipeOptional.isEmpty())
            {
                return this.buildApiErrorResponse("menuDayModification.recipeDoesNotExist", request, HttpStatus.NOT_FOUND);
            }
            
            MenuDay menuDay = menuDayOptional.get();
            Recipe recipe = recipeOptional.get();
            
            MenuDayRecipe newMenuDayRecipe = new MenuDayRecipe();
            newMenuDayRecipe.setRecipe(recipe);
            newMenuDayRecipe.setMenuDay(menuDay);
            newMenuDayRecipe.setStartTime(LocalTime.parse(addRecipeToMenuDayDTO.getStartTime()));
            newMenuDayRecipe.setEndTime(LocalTime.parse(addRecipeToMenuDayDTO.getEndTime()));
            
            if(!this.mobileAppMenuUtilsService.menuDayRecipeDoesNotOverlapOtherRecipeTimeFrame(menuDay, newMenuDayRecipe))
            {
                return this.buildApiErrorResponse("menuDayModification.recipeOverlapsWithAnotherRecipeTimeFrame", request, HttpStatus.BAD_REQUEST);
            }
            
            this.mobileAppMenuService.saveMenuDayRecipeForMenuDay(menuDay, newMenuDayRecipe);
            
            String successResponseMessage = mobileAppLocaleProvider.getTranslation("menuDayModification.addedMenuDayRecipeSuccesfully", 
                        MobileAppLocaleProvider.MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiInformationResponse successResponse = new ApiInformationResponse(request.getServletPath(), successResponseMessage);
            
            return new ResponseEntity<>(successResponse, HttpStatus.OK);  
        } 
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            String errorResponseMessage = mobileAppLocaleProvider.getTranslation("general.unknownError", 
                        MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), errorResponseMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/remove-recipe-from-menu-day/{menuDayId}/{menuDayRecipeId}")
    public ResponseEntity<Object> removeRecipeFromMenuDay(HttpServletRequest request, @PathVariable(name = "menuDayId") Long menuDayId,
            @PathVariable(name = "menuDayRecipeId") Long menuDayRecipeId)
    {
        try
        {
            Optional<MenuDay> menuDayOptional = this.mobileAppMenuService.findMenuDayById(menuDayId);

            if (menuDayOptional.isEmpty())
            {
                return this.buildApiErrorResponse("menuDayModification.menuDayDoesNotExist", request, HttpStatus.NOT_FOUND);
            }

            Optional<MenuDayRecipe> menuDayRecipeOptional = this.mobileAppMenuService.findMenuDayRecipeById(menuDayRecipeId);

            if (menuDayRecipeOptional.isEmpty())
            {
                return this.buildApiErrorResponse("menuDayModification.recipeDoesNotExist", request, HttpStatus.NOT_FOUND);
            }

            MenuDay menuDay = menuDayOptional.get();
            MenuDayRecipe menuDayRecipe = menuDayRecipeOptional.get();
            
            if (menuDayRecipe.getMenuDay().getId() != menuDay.getId())
            {
                return this.buildApiErrorResponse("menuDayModification.recipeTargetedForRemovalDoesNotBelongToMenuDay", request, HttpStatus.BAD_REQUEST);
            }

            this.mobileAppMenuService.removeRecipeFromMenuDay(menuDay, menuDayRecipe);

            String successResponseMessage = mobileAppLocaleProvider.getTranslation("menuDayModification.removedMenuDayRecipeSuccesfully",
                    MobileAppLocaleProvider.MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiInformationResponse successResponse = new ApiInformationResponse(request.getServletPath(), successResponseMessage);

            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            String errorResponseMessage = mobileAppLocaleProvider.getTranslation("general.unknownError",
                    MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), errorResponseMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/remove-menu-day-from-menu/{menuId}/{menuDayId}")
    public ResponseEntity<Object> removeMenuDayFromMenu(HttpServletRequest request, @PathVariable(name = "menuDayId") Long menuDayId,
            @PathVariable(name = "menuId") Long menuId)
    {
        try
        {
            Optional<Menu> menuOptional = this.mobileAppMenuService.findMenuById(menuId);

            if (menuOptional.isEmpty())
            {
                return this.buildApiErrorResponse("menuModification.menuDoesNotExist", request, HttpStatus.NOT_FOUND);
            }
            
            Optional<MenuDay> menuDayOptional = this.mobileAppMenuService.findMenuDayById(menuDayId);

            if (menuDayOptional.isEmpty())
            {
                return this.buildApiErrorResponse("menuDayModification.menuDayDoesNotExist", request, HttpStatus.NOT_FOUND);
            }

            Menu menu = menuOptional.get();
            MenuDay menuDay = menuDayOptional.get();
            
            if (menuDay.getMenu().getId() != menu.getId())
            {
                return this.buildApiErrorResponse("menuModification.menuDayTargetedForRemovalDoesNotBelongToMenu", request, HttpStatus.BAD_REQUEST);
            }

            this.mobileAppMenuService.removeMenuDayFromMenu(menu, menuDay);

            String successResponseMessage = mobileAppLocaleProvider.getTranslation("menuModification.removedMenuDaySuccesfully",
                    MobileAppLocaleProvider.MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiInformationResponse successResponse = new ApiInformationResponse(request.getServletPath(), successResponseMessage);

            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            String errorResponseMessage = mobileAppLocaleProvider.getTranslation("general.unknownError",
                    MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), errorResponseMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/remove-menu/{menuId}")
    public ResponseEntity<Object> removeMenu(HttpServletRequest request, @PathVariable(name = "menuId") Long menuId)
    {
        try
        {
            Optional<Menu> menuOptional = this.mobileAppMenuService.findMenuById(menuId);

            if (menuOptional.isEmpty())
            {
                return this.buildApiErrorResponse("menuModification.menuDoesNotExist", request, HttpStatus.NOT_FOUND);
            }

            Menu menu = menuOptional.get();

            this.mobileAppMenuService.removeMenu(menu);

            String successResponseMessage = mobileAppLocaleProvider.getTranslation("menuModification.removedMenuSuccesfully",
                    MobileAppLocaleProvider.MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiInformationResponse successResponse = new ApiInformationResponse(request.getServletPath(), successResponseMessage);

            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            String errorResponseMessage = mobileAppLocaleProvider.getTranslation("general.unknownError",
                    MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), errorResponseMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/edit-menu")
    public ResponseEntity<Object> editMenu(HttpServletRequest request, @RequestBody @Valid EditMenuDTO editMenuDTO)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        try
        {
            Optional<Menu> menuOptional = this.mobileAppMenuService.findMenuById(editMenuDTO.getMenuId());

            if (menuOptional.isEmpty())
            {
                return this.buildApiErrorResponse("menuModification.menuDoesNotExist", request, HttpStatus.NOT_FOUND);
            }
            
            if (this.mobileAppMenuService.existsMenuByNameForUsername(editMenuDTO.getMenuName(), username))
            {
                return this.buildApiErrorResponse("menuCreation.menuAlreadyExistsForThisUser", request, HttpStatus.CONFLICT);
            }

            Menu menu = menuOptional.get();
            menu.setName(editMenuDTO.getMenuName());

            this.mobileAppMenuService.updateMenu(menu);

            String successResponseMessage = mobileAppLocaleProvider.getTranslation("menuModification.editMenuSuccesfully",
                    MobileAppLocaleProvider.MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiInformationResponse successResponse = new ApiInformationResponse(request.getServletPath(), successResponseMessage);

            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            String errorResponseMessage = mobileAppLocaleProvider.getTranslation("general.unknownError",
                    MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), errorResponseMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/set-menu-as-favourite/{menuId}")
    public ResponseEntity<Object> setMenuAsFavourite(HttpServletRequest request, @PathVariable(name = "menuId") Long menuId)
    {
        String jwt = mobileAppHttpRequestService.getJsonWebToken(request);
        
        String username = jwtService.extractUsername(jwt);
        
        try
        {
            Optional<Menu> menuOptional = this.mobileAppMenuService.findMenuById(menuId);

            if (menuOptional.isEmpty())
            {
                return this.buildApiErrorResponse("menuModification.menuDoesNotExist", request, HttpStatus.NOT_FOUND);
            }
            
            List<Menu> userMenus = this.mobileAppMenuService.findByUsername(username);
            Menu menu = menuOptional.get();
            
            if(userMenus.isEmpty() || !userMenus.contains(menu))
            {
                return this.buildApiErrorResponse("menuCreation.menuDoesNotExistForThisUser", request, HttpStatus.NOT_FOUND);
            }

            this.mobileAppMenuService.setMenuAsFavouriteForUser(username, menuId);

            String successResponseMessage = mobileAppLocaleProvider.getTranslation("menuModification.settedMenuAsFavouriteSuccesfully",
                    MobileAppLocaleProvider.MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiInformationResponse successResponse = new ApiInformationResponse(request.getServletPath(), successResponseMessage);

            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            String errorResponseMessage = mobileAppLocaleProvider.getTranslation("general.unknownError",
                    MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
            ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), errorResponseMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private ResponseEntity<Object> buildApiErrorResponse(String errorMessageTranslationKey, HttpServletRequest request, HttpStatus status)
    {
        String errorResponseMessage = mobileAppLocaleProvider.getTranslation(errorMessageTranslationKey, 
                        MobileAppLocaleProvider.ERROR_MESSAGES_RESOURCE_BUNDLE_KEY, mobileAppHttpRequestService.getMobileAppLocale(request));
        ApiErrorResponse errorResponse = new ApiErrorResponse(request.getServletPath(), errorResponseMessage);
            
        return new ResponseEntity<>(errorResponse, status);
    }
}
