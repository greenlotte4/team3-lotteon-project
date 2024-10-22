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



// 파일 검증 함수: 크기, 확장자, 이미지 세로 가로 크기 검증
function validateFile(file, maxSize, maxWidth,maxHeight,inputElement) {
    var allowedExtensions = /(\.jpg|\.jpeg|\.png)$/i; // 허용 파일 확장자

    // 파일 크기 확인
    if (file.size > maxSize) {
        alert('파일 ' + file.name + '의 크기는 최대 ' + (maxSize / (1024 * 1024)) + 'MB까지 허용됩니다.');
        inputElement.value = ''; // 선택된 파일 초기화
        return false;
    }

    // 파일 확장자 확인
    if (!allowedExtensions.exec(file.name)) {
        alert(file.name + ' 파일은 jpg, jpeg, png 형식만 허용됩니다.');
        inputElement.value = ''; // 선택된 파일 초기화
        return false;
    }

    // 이미지의 가로 크기 확인 (비동기 처리)
    var img = new Image();
    img.src = URL.createObjectURL(file);
    img.onload = function() {
        if (img.width > maxWidth || maxHeight==null) {
            alert(file.name + '의 이미지 가로 크기는 ' + maxWidth + 'px를 초과할 수 없습니다.');
            inputElement.value = ''; // 선택된 파일 초기화
            return false;
        }else if(img.width> maxWidth || img.height > maxHeight) {
            alert(file.name + '의 이미지  크기는 ' + maxWidth + 'X'+maxHeight+'px를 초과할 수 없습니다.');
            inputElement.value = ''; // 선택된 파일 초기화
            return false;
        }

    };
    return true; // 파일 크기와 확장자가 적합할 경우 true 반환
}
