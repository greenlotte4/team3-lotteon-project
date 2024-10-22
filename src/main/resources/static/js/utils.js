// 전화번호 뒷부분을 잘라서 표시하는 함수
function hidePhoneNumber(phoneNumber) {
    return phoneNumber.slice(0, 3) + '...';
}

// Function to fetch categories by level (for 1st level)
function fetchCategoriesByLevel(level, dropdown) {
    fetch(`/api/categories/level/${level}`)
        .then(response => response.json())
        .then(data => populateDropdown(data, dropdown))
        .catch(error => console.error('Error fetching categories by level:', error));
}

// Function to fetch categories by parent ID (for 2nd and 3rd levels)
function fetchCategoriesByParent(parentId, dropdown) {
    fetch(`/api/categories/parent/${parentId}`)
        .then(response => response.json())
        .then(data => populateDropdown(data, dropdown))
        .catch(error => console.error('Error fetching categories by parent:', error));
}

// Function to populate dropdown with fetched categories
function populateDropdown(categories, dropdown) {
    dropdown.innerHTML = '<option value="">분류 선택</option>';  // Reset dropdown
    categories.forEach(category => {
        const option = document.createElement('option');
        option.value = category.id;
        option.text = category.name;
        dropdown.appendChild(option);
    });
}



// Example of collecting data (before form submission)
function collectOptions() {
    const optionItems = document.querySelectorAll('.optionItem');
    options = [];

    // Loop through each optionItem and collect values
    optionItems.forEach(item => {
        const optionName = item.querySelector('input[name="option"]').value;
        const optionStock = item.querySelector('input[name="optionStock"]').value;

        // Store each option as an object
        options.push({
            name: optionName,
            stock: optionStock
        });
    });

    console.log(options); // This will show the collected options in the console
    // You can now send 'options' via form submission or AJAX
}