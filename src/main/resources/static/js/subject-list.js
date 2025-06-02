    function moveToTop(checkbox) {
        const item = checkbox.closest('.form-check');
        const parent = document.getElementById('subjectList');

        if (checkbox.checked) {
            parent.prepend(item);
        } else {
            // Append to bottom if unchecked
            parent.appendChild(item);
        }
    }

    function addNewSubjectField() {
        const container = document.getElementById("new-subjects-container");

        const inputGroup = document.createElement("div");
        inputGroup.className = "input-group input-group-sm mb-2";

        const input = document.createElement("input");
        input.type = "text";
        input.name = "newSubjects";  // Дуже важливо для передачі у DTO
        input.className = "form-control";
        input.placeholder = "Enter new subject name";

        const removeBtn = document.createElement("button");
        removeBtn.type = "button";
        removeBtn.className = "btn btn-outline-danger";
        removeBtn.innerText = "×";
        removeBtn.onclick = () => container.removeChild(inputGroup);

        inputGroup.appendChild(input);
        inputGroup.appendChild(removeBtn);

        container.appendChild(inputGroup);
    }
