    function addTeacher() {

        const container = document.getElementById('teacher-list');
        const index = container.children.length;

        const template = `
            <div class="mb-2 d-flex align-items-center">
                <div class="flex-grow-1 me-2">
                    <label class="form-label">Teacher ${index + 1}</label>
                    <div class="input-group">
                        <input type="text" name="teachers[${index}].email"
                               class="form-control" placeholder="Teacher's email"/>
                        <button type="button" class="btn btn-outline-danger" onclick="removeTeacher(this)">
                            <i class="fas fa-minus"></i>
                        </button>
                    </div>
                </div>
            </div>
        `;
        container.insertAdjacentHTML('beforeend', template);
    }

    function removeTeacher(button) {
        const teacherDiv = button.closest('.mb-2.d-flex');
        if (teacherDiv) {
            teacherDiv.remove();
            // Reindex remaining teachers
            const container = document.getElementById('teacher-list');
            const teachers = container.querySelectorAll('.mb-2.d-flex');
            teachers.forEach((div, index) => {
                const label = div.querySelector('label');
                const input = div.querySelector('input');
                if (label) label.textContent = `Teacher ${index + 1}`;
                if (input) input.name = `teachers[${index}].email`;
            });
        }
    }