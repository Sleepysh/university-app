    const editModal = document.getElementById('editPostModal');
    editModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;

        const courseId = button.getAttribute('data-course-id');
        const postId = button.getAttribute('data-post-id');
        const title = button.getAttribute('data-post-title');
        const content = button.getAttribute('data-post-content');

        // Вставляємо дані в поля модального вікна
        document.getElementById('editPostId').value = postId;
        document.getElementById('editPostTitle').value = title;
        document.getElementById('editPostContent').value = content;

        // Формуємо динамічну URL-адресу для відправки форми
        const form = document.getElementById('editPostForm');
        form.action = `/courses/${courseId}/post/${postId}/update`;
    });