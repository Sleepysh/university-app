 // Initialize toast
    const flashToast = new bootstrap.Toast(document.getElementById('flashToast'));

    // Function to show flash message
    function showFlashMessage(title, message, type = 'success') {
        const toastEl = document.getElementById('flashToast');
        const toastTitle = document.getElementById('toastTitle');
        const toastMessage = document.getElementById('toastMessage');

        // Set content
        toastTitle.textContent = title;
        toastMessage.textContent = message;

        // Set style based on type
        toastEl.className = 'toast ' + type;

        // Show the toast
        flashToast.show();

        // Auto-hide after 5 seconds
        setTimeout(() => flashToast.hide(), 5000);
    }

    // Check for flash messages on page load
    document.addEventListener('DOMContentLoaded', function() {
        const flashMessage = /*[[${flashMessage}]]*/ null;
        const flashType = /*[[${flashType}]]*/ 'success';

        if (flashMessage) {
            const title = flashType === 'error' ? 'Error' :
                         flashType === 'warning' ? 'Warning' :
                         flashType === 'info' ? 'Info' : 'Success';
            showFlashMessage(title, flashMessage, flashType);
        }
    });