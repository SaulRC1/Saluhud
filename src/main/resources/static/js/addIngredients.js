/*document.addEventListener("DOMContentLoaded", function () {
 const ingredientsContainer = document.getElementById("ingredientsContainer");
 const addIngredientButton = document.getElementById("addIngredient");
 
 addIngredientButton.addEventListener("click", function () {
 addIngredientField();
 });
 
 function addIngredientField() {
 const index = ingredientsContainer.children.length;
 const ingredientEntry = document.createElement("div");
 ingredientEntry.classList.add("ingredient-entry");
 
 ingredientEntry.innerHTML = `
 <select name="recipeIngredients[${index}].ingredientId" required>
 <option value="">${i18n.selectIngredient}</option>
 </select>
 <input type="number" step="0.01" name="recipeIngredients[${index}].quantity" placeholder="${i18n.quantity}" required/>
 <input type="text" name="recipeIngredients[${index}].unit" placeholder="${i18n.unit}" required/>
 <button type="button" class="remove-ingredient">${i18n.delete}</button>
 `;
 
 // Llamada AJAX para obtener los ingredientes
 fetch('/recipes/ingredients/search')
 .then(response => response.json())
 .then(ingredients => {
 if (Array.isArray(ingredients)) {
 const selectElement = ingredientEntry.querySelector("select");
 ingredients.forEach(ingredient => {
 const option = document.createElement("option");
 option.value = ingredient.id;
 option.textContent = ingredient.name;
 selectElement.appendChild(option);
 });
 } else {
 console.error('La respuesta no es un array:', ingredients);
 }
 })
 .catch(error => {
 console.error('Error al obtener los ingredientes:', error);
 });
 
 ingredientEntry.querySelector(".remove-ingredient").addEventListener("click", function () {
 ingredientEntry.remove();
 });
 
 ingredientsContainer.appendChild(ingredientEntry);
 }
 });*/

document.addEventListener("DOMContentLoaded", function () {
    const ingredientsContainer = document.getElementById("ingredientsContainer");
    const addIngredientButton = document.getElementById("addIngredient");

    addIngredientButton.addEventListener("click", function () {
        addIngredientField();
    });

    function addIngredientField() {
        const index = ingredientsContainer.children.length;
        const ingredientEntry = document.createElement("div");
        ingredientEntry.classList.add("ingredient-entry");

        ingredientEntry.innerHTML = `
            <div class="ingredient-selector">
                <select name="recipeIngredients[${index}].ingredientId" required>
                    <option value="">${i18n.selectIngredient}</option>
                </select>
                <button type="button" class="next-page">${i18n.next}</button>
            </div>
            <input type="number" step="0.01" name="recipeIngredients[${index}].quantity" placeholder="${i18n.quantity}" required/>
            <input type="text" name="recipeIngredients[${index}].unit" placeholder="${i18n.unit}" required/>
            <button type="button" class="remove-ingredient">${i18n.delete}</button>
        `;

        const selectElement = ingredientEntry.querySelector("select");
        const nextPageButton = ingredientEntry.querySelector(".next-page");
        let currentPage = 0; // Página inicial

        function loadIngredients(page) {
            fetch(`/recipes/ingredients/search?page=${page}`)
                .then(response => response.json())
                .then(ingredients => {
                    if (Array.isArray(ingredients) && ingredients.length > 0) {
                        selectElement.innerHTML = `<option value="">${i18n.selectIngredient}</option>`;
                        ingredients.forEach(ingredient => {
                            const option = document.createElement("option");
                            option.value = ingredient.id;
                            option.textContent = ingredient.name;
                            selectElement.appendChild(option);
                        });
                    } else {
                        console.warn('No hay más ingredientes disponibles.');
                    }
                })
                .catch(error => console.error('Error al obtener los ingredientes:', error));
        }

        // Cargar la primera página de ingredientes
        loadIngredients(currentPage);

        // Manejador del botón "Siguiente"
        nextPageButton.addEventListener("click", function () {
            currentPage++;
            loadIngredients(currentPage);
        });

        // Eliminar ingrediente
        ingredientEntry.querySelector(".remove-ingredient").addEventListener("click", function () {
            ingredientEntry.remove();
        });

        ingredientsContainer.appendChild(ingredientEntry);
    }
});




