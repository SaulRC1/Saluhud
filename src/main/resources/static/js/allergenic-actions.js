document.addEventListener("DOMContentLoaded", function () {
    const radios = document.querySelectorAll('input[name="selectedAllergenic"]');
    const updateButton = document.getElementById('updateButton');
    let selectedId = '';

    // Acceder a los mensajes desde los elementos definidos en HTML
    const editConfirmTitle = document.getElementById('editConfirmTitle').textContent;
    const editConfirmMessage = document.getElementById('editConfirmMessage').textContent;
    const buttonYes = document.getElementById('buttonYes').textContent;
    const buttonNo = document.getElementById('buttonNo').textContent;

    // Escucha el cambio de selección en los radios
    radios.forEach(radio => {
        radio.addEventListener('change', () => {
            selectedId = radio.value;
            updateButton.disabled = false;
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
                window.location.href = '/allergenic/edit/' + selectedId;
                document.getElementById('editModal').remove(); // Cerrar el modal
            });

            document.getElementById('cancelButton').addEventListener('click', function () {
                document.getElementById('editModal').remove(); // Cerrar el modal
            });
        }
    });
});
