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


    // Array to store options temporarily
    let options = [];

    // Function to add a new option input field
    function addOptionField() {
        // Create a new option div
        const optionItem = document.createElement('div');
        optionItem.classList.add('optionItem');
        // Create new input elements for option and stock
        const optionNameInput = document.createElement('input');
        optionNameInput.setAttribute('type', 'text');
        optionNameInput.setAttribute('placeholder', '옵션 이름 예) R100');
        optionNameInput.setAttribute('name', 'optionName');

        const optionInput = document.createElement('input');
        optionInput.setAttribute('type', 'text');
        optionInput.setAttribute('placeholder', '옵션 내용 예) red/100size');
        optionInput.setAttribute('name', 'optionDesc');

        const stockInput = document.createElement('input');
        stockInput.setAttribute('type', 'number');
        stockInput.setAttribute('placeholder', '해당 옵션 재고수량');
        stockInput.setAttribute('name', 'optionStock');

        // Append the inputs to the optionItem div
        optionItem.appendChild(optionNameInput);
        optionItem.appendChild(optionInput);
        optionItem.appendChild(stockInput);

        // Add the new optionItem to the optionList div
        document.getElementById('optionList').appendChild(optionItem);
    }

    // Event listener for adding a new option field
    document.getElementById('addOptionBtn').addEventListener('click', addOptionField);


    //파일 크기 조정
    var productImg1 = document.querySelector('input[name="files[0]"]');
    var productImg2 = document.querySelector('input[name="files[1]"]');
    var productImg3 = document.querySelector('input[name="files[2]"]');
    var productDesc = document.querySelector('input[name="files[3]"]');

    // // 파일 선택 시 검증 함수 호출
    // productImg1.addEventListener('change', function(event) {
    //     var file = this.files[0];
    //     if (file) {
    //         validateFile(file, 1024 * 1024, 190, 190, this); // 가로 190px, 세로 제한 없음, 1MB
    //     }
    // });
    //
    // productImg2.addEventListener('change', function(event) {
    //     var file = this.files[0];
    //     if (file) {
    //         validateFile(file, 1024 * 1024, 230, 230, this); // 가로 230px, 세로 제한 없음, 1MB
    //     }
    // });
    //
    // productImg3.addEventListener('change', function(event) {
    //     var file = this.files[0];
    //     if (file) {
    //         validateFile(file, 1024 * 1024, 456, 456, this); // 가로 456px, 세로 456px, 1MB
    //     }
    // });
    //
    // productDesc.addEventListener('change', function(event) {
    //     var file = this.files[0];
    //     if (file) {
    //         validateFile(file, 1024 * 1024, 940, null, this); // 가로 940px, 세로 제한 없음, 1MB
    //     }
    // });


});