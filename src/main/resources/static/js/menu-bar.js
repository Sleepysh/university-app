
function toggleMenu() {
    document.getElementById('menu').classList.toggle('show');
}

function toggleDropdown(event) {
    event.preventDefault();
    const dropdown = event.target.closest('.dropdown');
    const menu = dropdown.querySelector('.dropdown-menu');
    menu.style.display = menu.style.display === 'block' ? 'none' : 'block';

    // Close dropdown if clicked outside
    document.addEventListener('click', function handler(e) {
        if (!dropdown.contains(e.target)) {
            menu.style.display = 'none';
            document.removeEventListener('click', handler);
        }
    });
}

