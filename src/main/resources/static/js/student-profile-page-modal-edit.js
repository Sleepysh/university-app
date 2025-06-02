  document.addEventListener("DOMContentLoaded", function () {
  const form = document.querySelector("#editProfileModal form");

  const firstName = document.getElementById("firstName");
  const lastName = document.getElementById("lastName");
  const age = document.getElementById("age");
  const phone = document.getElementById("telephoneNumber");

  const oldPassword = document.getElementById("oldPassword");
  const newPassword = document.getElementById("newPassword");

  const flashError = document.getElementById("flash-error");
  const flashSuccess = document.getElementById("flash-success");
  const validationError = document.getElementById("validation-error");

  if (flashError && flashError.textContent.trim()) {
    showToast(flashError.textContent.trim(), "danger");
    const editModal = new bootstrap.Modal(document.getElementById("editProfileModal"));
    editModal.show();
  }

  if (validationError && validationError.textContent.trim()) {
    showToast(validationError.textContent.trim(), "danger");
    const editModal = new bootstrap.Modal(document.getElementById("editProfileModal"));
    editModal.show();
  }

  if (flashSuccess && flashSuccess.textContent.trim()) {
    showToast(flashSuccess.textContent.trim(), "success");
  }

  form.addEventListener("submit", function (e) {
    let valid = true;

    if (!/^.{2,20}$/.test(firstName.value.trim())) {
      showToast("Enter a valid first name (2-20 characters).", "danger");
      valid = false;
    }

    if (!/^.{2,20}$/.test(lastName.value.trim())) {
      showToast("Enter a valid last name (2-20 characters).", "danger");
      valid = false;
    }

    const ageVal = parseInt(age.value, 10);
    if (isNaN(ageVal) || ageVal < 18 || ageVal > 100) {
      showToast("Age must be between 18 and 100.", "danger");
      valid = false;
    }

    if (!/^\+?[0-9\s\-]{7,15}$/.test(phone.value.trim())) {
      showToast("Enter a valid phone number.", "danger");
      valid = false;
    }

    // Validate new password if filled
    const newVal = newPassword.value.trim();
    if (newVal !== "") {
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/;
      if (!passwordRegex.test(newVal)) {
        showToast("New password must be at least 8 characters and include uppercase, lowercase, digit, and special character.", "danger");
        valid = false;
      }

      if (oldPassword.value.trim() === "") {
        showToast("Please enter your current password to set a new one.", "danger");
        valid = false;
      }
    }

    if (!valid) {
      e.preventDefault();
    }
  });

  function showToast(message, type = "info") {
    const toast = document.createElement("div");
    toast.className = `toast align-items-center text-bg-${type} border-0 show`;
    toast.role = "alert";
    toast.ariaLive = "assertive";
    toast.ariaAtomic = "true";
    toast.innerHTML = `
      <div class="d-flex">
        <div class="toast-body">${message}</div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
      </div>
    `;
    const container = document.querySelector(".toast-container");
    if (container) {
      container.appendChild(toast);
    } else {
      const wrapper = document.createElement("div");
      wrapper.className = "toast-container position-fixed top-0 end-0 p-3";
      wrapper.appendChild(toast);
      document.body.appendChild(wrapper);
    }
    setTimeout(() => toast.remove(), 5000);
  }
});
