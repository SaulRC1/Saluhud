document.addEventListener("DOMContentLoaded", function () {
    const ingredientsContainer = document.getElementById("ingredientsContainer");
    const addIngredientButton = document.getElementById("addIngredient");

    addIngredientButton.addEventListener("click", function () {
        addIngredientField();
    });

    const form = document.querySelector("form"); // Asegúrate de seleccionar el formulario correcto
    form.addEventListener("submit", function (event) {
        // Encuentra todas las entradas de ingredientes
        const ingredientEntries = document.querySelectorAll("[name^='recipeIngredients']");
        ingredientEntries.forEach(entry => {
            // Busca el select de ingredientId en la entrada
            const select = entry.closest(".ingredient-entry").querySelector("select[name$='.ingredientId']");
            if (select && !select.value) {
                // Si no se seleccionó ningún ingrediente, elimina toda la entrada
                select.closest(".ingredient-entry").remove();
            }
        });
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
});


