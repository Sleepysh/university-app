    // Create a DataTransfer object to hold the selected files
    const dataTransfer = new DataTransfer();
    const fileInput = document.getElementById('fileInput');
    const previewContainer = document.getElementById('file-preview');

    // Event listener for file input
    fileInput.addEventListener('change', function () {
        // Loop through the selected files and add them to DataTransfer
        for (const file of fileInput.files) {
            dataTransfer.items.add(file);
        }

        // Clear the original input
        fileInput.files = dataTransfer.files;

        // Display the files
        displayFiles();
    });

    // Function to display selected files
    function displayFiles() {
        previewContainer.innerHTML = ""; // Clear previous preview

        Array.from(dataTransfer.files).forEach((file, index) => {
            const fileDiv = document.createElement('div');
            fileDiv.classList.add('alert', 'alert-secondary', 'd-flex', 'justify-content-between', 'align-items-center');
            fileDiv.innerHTML = `
                <span>${file.name} (${(file.size / 1024).toFixed(2)} KB)</span>
                <button type="button" class="btn-close" onclick="removeFile(${index})"></button>
            `;
            previewContainer.appendChild(fileDiv);
        });
    }

    // Function to remove file from list
    function removeFile(index) {
        dataTransfer.items.remove(index);
        fileInput.files = dataTransfer.files;
        displayFiles(); // Refresh preview
    }

    $(document).ready(function() {
            $('#postContent').summernote({
                height: 200,
                placeholder: 'Write your post here...',
                toolbar: [
                    ['style', ['bold', 'italic', 'underline', 'clear']],
                    ['font', ['strikethrough', 'superscript', 'subscript']],
                    ['fontsize', ['fontsize']],
                    ['color', ['color']],
                    ['para', ['ul', 'ol', 'paragraph']],
                    ['insert', ['link', 'picture', 'video']],
                    ['view', ['fullscreen', 'codeview', 'help']]
                ]
            });
        });