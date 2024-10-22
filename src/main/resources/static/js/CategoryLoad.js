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

});