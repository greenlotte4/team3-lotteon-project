document.addEventListener('DOMContentLoaded', function () {
    // Script for toggling the category menu
    // const viewAllButton = document.getElementById('viewAllButton');
    // const categoryMenu = document.querySelector('.categoryMenu');
    // const header = document.querySelector('.category.on');

    // viewAllButton.addEventListener('click', function () {
    //     // Toggle the visibility of the menu and the button bar transformation
    //     header.classList.toggle('menuVisible');
    // });


    const viewAllButton = document.getElementById('viewAllButton');
    const categoryMenu = document.querySelector('.categoryMenu');
    const header = document.querySelector('.category.on');

    viewAllButton.addEventListener('click', function () {
        // Toggle the visibility of the menu with slide down/up animation
        header.classList.toggle('menuVisible');

        // If the menu is visible, expand the max-height to show content
        if (header.classList.contains('menuVisible')) {
            categoryMenu.style.maxHeight = categoryMenu.scrollHeight + 'px';
        } else {
            categoryMenu.style.maxHeight = '0'; // Collapse the menu
        }
    });
});