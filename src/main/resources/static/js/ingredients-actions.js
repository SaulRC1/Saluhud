document.addEventListener("DOMContentLoaded", function () {
    const radios = document.querySelectorAll('input[name="selectedIngredient"]');
    const updateButton = document.getElementById('updateButton');
    const deleteButton = document.getElementById('deleteButton');
    let selectedId = '';

    // Acceder a los mensajes desde los elementos definidos en HTML
    const editConfirmTitle = document.getElementById('editConfirmTitle').textContent;
    const editConfirmMessage = document.getElementById('editConfirmMessage').textContent;
    const deleteConfirmTitle = document.getElementById('deleteConfirmTitle').textContent;
    const deleteConfirmMessage = document.getElementById('deleteConfirmMessage').textContent;
    const buttonYes = document.getElementById('buttonYes').textContent;
    const buttonNo = document.getElementById('buttonNo').textContent;

    // Escucha el cambio de selección en los radios
    radios.forEach(radio => {
        radio.addEventListener('change', () => {
            selectedId = radio.value;
            updateButton.disabled = false;
            deleteButton.disabled = false;
        });
    });

    // Acción del botón Modificar
    updateButton.addEventListener('click', function () {
        if (selectedId) {
            const modalContent = `
                <div class="modal-overlay" id="editModal">
                    <div class="modal-content">
                        <h3>${editConfirmTitle}</h3>
                        <p>${editConfirmMessage}</p>
                        <button id="confirmButton">${buttonYes}</button>
                        <button id="cancelButton">${buttonNo}</button>
                    </div>
                </div>
            `;
            document.body.insertAdjacentHTML('beforeend', modalContent);

            // Eventos del modal
            document.getElementById('confirmButton').addEventListener('click', function () {
                window.location.href = '/ingredients/edit/' + selectedId;
                document.getElementById('editModal').remove(); // Cerrar el modal
            });

            document.getElementById('cancelButton').addEventListener('click', function () {
                document.getElementById('editModal').remove(); // Cerrar el modal
            });
        }
    });

    // Acción del botón Eliminar
    deleteButton.addEventListener('click', function () {
        if (selectedId) {
            const modalContent = `
                <div class="modal-overlay" id="deleteModal">
                    <div class="modal-content">
                        <h3>${deleteConfirmTitle}</h3>
                        <p>${deleteConfirmMessage}</p>
                        <button id="confirmButton">${buttonYes}</button>
                        <button id="cancelButton">${buttonNo}</button>
                    </div>
                </div>
            `;
            document.body.insertAdjacentHTML('beforeend', modalContent);

            // Eventos del modal
            document.getElementById('confirmButton').addEventListener('click', function () {
                window.location.href = '/ingredients/delete/' + selectedId;
                document.getElementById('deleteModal').remove(); // Cerrar el modal
            });

            document.getElementById('cancelButton').addEventListener('click', function () {
                document.getElementById('deleteModal').remove(); // Cerrar el modal
            });
        }
    });
});


