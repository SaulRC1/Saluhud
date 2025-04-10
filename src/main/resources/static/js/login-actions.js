document.addEventListener("DOMContentLoaded", function () {
    const logoutButton = document.getElementById('logoutButton');
    const logoutForm = document.getElementById('logoutForm');
    
     // Acceder a los mensajes desde los elementos definidos en HTML
    const logoutConfirmTitle = document.getElementById('logoutConfirmTitle').textContent;
    const logoutConfirmMessage = document.getElementById('logoutConfirmMessage').textContent;
    const buttonYes = document.getElementById('buttonYes').textContent;
    const buttonNo = document.getElementById('buttonNo').textContent;

    if (logoutButton && logoutForm) {
        logoutButton.addEventListener('click', function () {
            // Crear modal de confirmación
            const modal = document.createElement('div');
            modal.classList.add('modal-overlay');
            modal.innerHTML = `
                <div class="modal-content">
                    <h3>${logoutConfirmTitle}</h3>
                    <p>${logoutConfirmMessage}</p>                   
                    <button id="confirmLogout">${buttonYes}</button>
                    <button id="cancelLogout">${buttonNo}</button>                
                </div>
            `;
            document.body.appendChild(modal);

            // Botón de confirmación
            document.getElementById('confirmLogout').addEventListener('click', function () {
                logoutForm.submit();
            });

            // Botón de cancelación
            document.getElementById('cancelLogout').addEventListener('click', function () {
                modal.remove();
            });
        });
    } else {
        console.warn('logoutButton o logoutForm no encontrados.');
    }
});


