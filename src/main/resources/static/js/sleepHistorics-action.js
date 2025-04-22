document.addEventListener("DOMContentLoaded", function () {
    const radios = document.querySelectorAll('input[name="selectedEntry"]');
    const updateButton = document.getElementById('updateButton');
    const deleteButton = document.getElementById('deleteButton');
    let selectedId = '';

    const editConfirmTitle = document.getElementById('editConfirmTitle').textContent;
    const editConfirmMessage = document.getElementById('editConfirmMessage').textContent;
    const deleteConfirmTitle = document.getElementById('deleteConfirmTitle').textContent;
    const deleteConfirmMessage = document.getElementById('deleteConfirmMessage').textContent;
    const buttonYes = document.getElementById('buttonYes').textContent;
    const buttonNo = document.getElementById('buttonNo').textContent;
    const userId = document.getElementById('sleepData').getAttribute('data-user-id');

    radios.forEach(radio => {
        radio.addEventListener('change', () => {
            selectedId = radio.value;
            updateButton.disabled = false;
            deleteButton.disabled = false;
        });
    });

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

            document.getElementById('confirmButton').addEventListener('click', function () {
                 window.location.href = '/users/sleepHistorics/' + userId + '/entries/edit/' + selectedId;
            });

            document.getElementById('cancelButton').addEventListener('click', function () {
                document.getElementById('editModal').remove();
            });
        }
    });

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

            document.getElementById('confirmButton').addEventListener('click', function () {
                window.location.href = '/users/sleepHistorics/' + userId + '/entries/delete/' + selectedId;
            });

            document.getElementById('cancelButton').addEventListener('click', function () {
                document.getElementById('deleteModal').remove();
            });
        }
    });
});
