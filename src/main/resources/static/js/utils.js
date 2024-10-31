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

//유저 등급 변경
function confirmGradeChange(selectElement) {
    const selectedGrade = selectElement.value;
    const memberId = selectElement.dataset.id; // 데이터 속성에서 멤버 ID 가져오기

    const confirmChange = confirm(`해당 등급으로 변경하시겠습니까? (${selectedGrade})`);
    if (confirmChange) {
        // 등급 수정 요청을 서버로 보냄
        const updatedData = {
            id: memberId,
            grade: selectedGrade
        };

        fetch(`/admin/user/member/updateGrade`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedData), // 수정할 데이터 전송
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then(data => {
                alert(data.message); // 성공 메시지 표시
                // 여기에서 필요에 따라 페이지를 새로 고치거나 UI를 업데이트할 수 있습니다.
                // 예: location.reload(); // 페이지 새로 고침
            })
            .catch(error => console.error("Error updating grade:", error));
    } else {
        // 변경 취소 시
        // 선택된 옵션을 원래 등급으로 재설정하려면 추가 로직이 필요할 수 있음
        // 원래 등급 값을 저장하고 그 값을 사용하여 재설정할 수 있습니다.
    }
}


function loginredirect(url){
    const uidElement = document.getElementById("uid");
    const uid = uidElement ? uidElement.value : null;
    if(!uid){
        alert('로그인 후 이용해 주세요');
        window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
        return;
    }else{
        window.location.href= url;
        return;
    }
}

