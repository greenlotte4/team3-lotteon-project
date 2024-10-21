document.addEventListener('DOMContentLoaded', function() {
    // Fetch 1st level categories when the page loads
    fetchCategoriesByLevel(1, document.querySelector('select[name="firstLevelCategory"]'));

    // Event listener for 1st level category change
    document.querySelector('select[name="firstLevelCategory"]').addEventListener('change', function() {
        const parentId = this.value;
        if (parentId) {
            fetchCategoriesByParent(parentId, document.querySelector('select[name="secondLevelCategory"]'));
        }
    });

    // Event listener for 2nd level category change
    document.querySelector('select[name="secondLevelCategory"]').addEventListener('change', function() {
        const parentId = this.value;
        if (parentId) {
            fetchCategoriesByParent(parentId, document.querySelector('select[name="thirdLevelCategory"]'));
        }
    });
});