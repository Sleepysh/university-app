  // Move selected subjects to top
  function moveToTop(checkbox) {
    const item = checkbox.closest('.form-check');
    const parent = document.getElementById('subjectList');

    if (checkbox.checked) {
      parent.prepend(item);
    }
  }

  // Add new subject field
  function addNewSubjectField() {
    const container = document.getElementById("new-subjects-container");
    const inputGroup = document.createElement("div");
    inputGroup.className = "input-group input-group-sm mb-2";

    const input = document.createElement("input");
    input.type = "text";
    input.name = "newSubjectNames";
    input.className = "form-control";
    input.placeholder = "Enter new subject name";
    input.required = true;

    const removeBtn = document.createElement("button");
    removeBtn.type = "button";
    removeBtn.className = "btn btn-outline-danger";
    removeBtn.innerHTML = "<i class='fas fa-times'></i>";
    removeBtn.onclick = () => container.removeChild(inputGroup);

    inputGroup.appendChild(input);
    inputGroup.appendChild(removeBtn);
    container.appendChild(inputGroup);
  }

    document.addEventListener("DOMContentLoaded", function () {
      const form = document.querySelector("#editProfileModal form");
      const newPassword = document.getElementById("newPassword");
      const confirmPassword = document.getElementById("confirmPassword");
      const searchInput = document.getElementById("subjectSearch");
      const checkboxes = document.querySelectorAll("#subjectList .form-check");

      const firstName = document.getElementById("firstName");
      const lastName = document.getElementById("lastName");
      const age = document.getElementById("age");
      const phone = document.getElementById("telephoneNumber");
      const email = document.getElementById("email");

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

        // FirstName.length
        if (!/^.{2,20}$/.test(firstName.value.trim())) {
          showToast("Enter a valid first name (min 2 letters, max 20 letters).", "danger");
          valid = false;
        }

        //  LastName.length
        if (!/^.{2,20}$/.test(lastName.value.trim())) {
          showToast("Enter a valid last name (min 2 letters, max 20 letters).", "danger");
          valid = false;
        }

        //Age
        const ageVal = parseInt(age.value, 10);
        if (isNaN(ageVal) || ageVal < 18 || ageVal > 100) {
          showToast("Age must be between 18 and 100.", "danger");
          valid = false;
        }

        // Phone
        if (!/^\+?[0-9\s\-]{7,15}$/.test(phone.value.trim())) {
          showToast("Enter a valid phone number.", "danger");
          valid = false;
        }

        //  Email
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value.trim())) {
          showToast("Enter a valid email address.", "danger");
          valid = false;
        }

        // password
        const newVal = newPassword.value.trim();
        const confirmVal = confirmPassword.value.trim();
        if (newVal !== "" || confirmVal !== "") {
          if (newVal.length < 6) {
            showToast("Password must be at least 6 characters.", "danger");
            valid = false;
          }

          if (newVal !== confirmVal) {
            showToast("Passwords do not match!", "danger");
            valid = false;
          }

          const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/;
          if (!passwordRegex.test(newVal)) {
            showToast("Password must be at least 8 characters and include uppercase, lowercase, digit, and special character.", "danger");
            valid = false;
          }
        }


        if (!valid) {
          e.preventDefault();
        }
      });

      // Subject search
      if (searchInput) {
        searchInput.addEventListener("input", function () {
          const searchTerm = this.value.toLowerCase();
          checkboxes.forEach(div => {
            const label = div.querySelector("label").textContent.toLowerCase();
            div.style.display = label.includes(searchTerm) ? "block" : "none";
          });
        });
      }

      // 🔔 Toast повідомлення
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

    // ➕ Додати новий предмет (опціонально)
    function addNewSubjectField() {
      const container = document.getElementById("new-subjects-container");
      const input = document.createElement("input");
      input.type = "text";
      input.name = "newSubjects";
      input.placeholder = "Enter new subject";
      input.className = "form-control mt-2 bg-secondary text-light";
      container.appendChild(input);
    }