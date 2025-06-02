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
*/

document.addEventListener("DOMContentLoaded", function () {
    const ingredientsContainer = document.getElementById("ingredientsContainer");
    const addIngredientButton = document.getElementById("addIngredient");
    const ingredientsDescriptionTextarea = document.getElementById("ingredientsDescription");

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
            <select name="recipeIngredients[${index}].unit" required>
                <option value="">${i18n.selectUnit}</option>
                <option value="ml">ml</option>
                <option value="g">g</option>
                <option value="l">l</option>
                <option value="kg">kg</option>
            </select>
            <button type="button" class="remove-ingredient">${i18n.delete}</button>
        `;

        const selectIngredient = ingredientEntry.querySelector("select[name$='.ingredientId']");
        const nextPageButton = ingredientEntry.querySelector(".next-page");
        const quantityInput = ingredientEntry.querySelector(`input[name$='.quantity']`);
        const unitSelect = ingredientEntry.querySelector(`select[name$='.unit']`);
        const removeButton = ingredientEntry.querySelector(".remove-ingredient");

        let currentPage = 0;

        function loadIngredients(page) {
            fetch(`/recipes/ingredients/search?page=${page}`)
                .then(response => response.json())
                .then(ingredients => {
                    if (Array.isArray(ingredients) && ingredients.length > 0) {
                        selectIngredient.innerHTML = `<option value="">${i18n.selectIngredient}</option>`;
                        ingredients.forEach(ingredient => {
                            const option = document.createElement("option");
                            option.value = ingredient.id;
                            option.textContent = ingredient.name;
                            selectIngredient.appendChild(option);
                        });
                    } else {
                        console.warn('No hay más ingredientes disponibles.');
                    }
                })
                .catch(error => console.error('Error al obtener los ingredientes:', error));
        }

        loadIngredients(currentPage);

        nextPageButton.addEventListener("click", function () {
            currentPage++;
            loadIngredients(currentPage);
        });

        removeButton.addEventListener("click", function () {
            ingredientEntry.remove();
            updateIngredientsDescription();
        });

        // Actualizar la descripción al cambiar cualquier campo relevante
        [selectIngredient, quantityInput, unitSelect].forEach(element => {
            element.addEventListener("change", updateIngredientsDescription);
            element.addEventListener("input", updateIngredientsDescription);
        });

        ingredientsContainer.appendChild(ingredientEntry);
    }

    // Función para actualizar el campo de descripción con todas las líneas
    function updateIngredientsDescription() {
        const lines = [];
        const ingredientEntries = ingredientsContainer.querySelectorAll(".ingredient-entry");

        ingredientEntries.forEach(entry => {
            const selectIngredient = entry.querySelector("select[name$='.ingredientId']");
            const ingredientName = selectIngredient.options[selectIngredient.selectedIndex]?.text || "";
            const quantity = entry.querySelector("input[name$='.quantity']").value;
            const unit = entry.querySelector("select[name$='.unit']").value;

            if (ingredientName && quantity && unit) {
                lines.push(`${ingredientName}, ${quantity}, ${unit}`);
            }
        });

        ingredientsDescriptionTextarea.value = lines.join("\n");
    }

    // Si ya hay ingredientes cargados al inicio, actualizar la descripción
    updateIngredientsDescription();
});
