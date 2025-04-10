document.addEventListener("DOMContentLoaded", function () {
    const deleteButton = document.getElementById('deleteButton');
    const userId = deleteButton.getAttribute('data-user-id');

    // Acceder a los mensajes desde los elementos definidos en HTML
    const deleteConfirmTitle = document.getElementById('deleteConfirmTitle').textContent;
    const deleteConfirmMessage = document.getElementById('deleteConfirmMessage').textContent;
    const buttonYes = document.getElementById('buttonYes').textContent;
    const buttonNo = document.getElementById('buttonNo').textContent;

    // Acción del botón Eliminar
    deleteButton.addEventListener('click', function () {
        event.preventDefault();
        if (userId) {
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
                window.location.href = '/users/fitness/delete/' + userId;
                document.getElementById('deleteModal').remove(); // Cerrar el modal
            });

            document.getElementById('cancelButton').addEventListener('click', function () {
                document.getElementById('deleteModal').remove(); // Cerrar el modal
            });
        }
    });
});
