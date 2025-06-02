const setupAutocomplete = (inputId, suggestionsId, containerId, endpointUrl) => {
    const input = document.getElementById(inputId);
    const suggestionsBox = document.getElementById(suggestionsId);
    const container = document.getElementById(containerId);

    const selectedItems = new Set();

    input.addEventListener("input", async () => {
        const query = input.value.trim();
        if (query.length < 2) {
            suggestionsBox.innerHTML = "";
            return;
        }

        const res = await fetch(`${endpointUrl}?query=${query}`);
        const data = await res.json();

        suggestionsBox.innerHTML = "";
        data.forEach(item => {
            const option = document.createElement("div");
            option.textContent = item.name || item.fullName || item.title;
            option.addEventListener("click", () => {
                if (!selectedItems.has(item.id)) {
                    selectedItems.add(item.id);

                    const tag = document.createElement("div");
                    tag.className = "selected-item";
                    tag.innerHTML = `
                        <span>${option.textContent}</span>
                        <span class="remove-btn" title="Remove">&times;</span>
                    `;

                    tag.querySelector(".remove-btn").addEventListener("click", () => {
                        tag.remove();
                        selectedItems.delete(item.id);
                    });

                    container.insertBefore(tag, input);
                }

                input.value = "";
                suggestionsBox.innerHTML = "";
            });
            suggestionsBox.appendChild(option);
        });
    });

    input.addEventListener("blur", () => {
        setTimeout(() => {
            suggestionsBox.innerHTML = "";
        }, 200);
    });
};

// Setup for teacher, student, and subject
setupAutocomplete("teacher-search", "teacher-suggestions", "teacher-container", "/api/teachers/search");
setupAutocomplete("student-search", "student-suggestions", "student-container", "/api/students/search");
setupAutocomplete("subject-search", "subject-suggestions", "subject-container", "/api/subjects/search");
