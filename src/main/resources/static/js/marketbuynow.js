// Global variables for product details
let selectedOptionValue = "";
let selectedOptionText = "";
let selectedOptionDesc = "";
let selectedOptions = []; // Array to store selected options
const productId = document.getElementById("productId").value;
const point = document.getElementById("point").value;
const productName = document.getElementById("productName").value;
const originalPrice = parseFloat(document.getElementById("originalPrice").innerText.replace(/,/g, ""));
const discount = parseInt(document.getElementById("discount").value);
const file190 = document.getElementById("file190").value;
const shippingFee = parseInt(document.getElementById("shippingFee").value) || 0;
let quantity = parseInt(document.getElementById("quantity").value); // Default quantity

const byCart = document.getElementById('buyCart');
console.log(byCart);

// Function to add a new selection or update an existing one
function addOrUpdateSelection(optionId, optionText, optionDesc, quantity) {
    const existingOption = selectedOptions.find(option => option.optionId === optionId);

    if (existingOption) {
        existingOption.quantity = quantity;
    } else {
        selectedOptions.push({ optionId, optionText, optionDesc, quantity });
    }

    updateSelectedResult(); // Refresh the display
}


// Function to update the selectResult section with all selected options, including quantity controls and a remove button
function updateSelectedResult() {
    const selectResult = document.querySelector(".selectResult");
    selectResult.innerHTML = ""; // Clear previous content

    selectedOptions.forEach((option, index) => {
        const optionDetail = document.createElement("div");
        optionDetail.classList.add("option-detail");

        optionDetail.innerHTML = `
            ${option.optionId} ${option.optionText} ${option.optionDesc}
            <div class="btnArea">
                <div class="quantity-control">
                    <button type="button" class="decrease" data-index="${index}">-</button>
                    <input type="number" class="quantity" value="${option.quantity}" min="1" data-index="${index}">
                    <button type="button" class="increase" data-index="${index}">+</button>
                </div>
            </div>
        `;

        // Create a remove button for each option
        const removeButton = document.createElement("button");
        removeButton.innerText = "제거";
        removeButton.classList.add("remove-option");
        removeButton.addEventListener("click", () => removeOption(index));

        // Append the remove button to the option detail
        const btnArea = optionDetail.querySelector(".btnArea");
        btnArea.appendChild(removeButton);

        selectResult.appendChild(optionDetail);
    });

    updateTotalPrice(); // Recalculate the total price
    addQuantityListeners(); // Add event listeners to new quantity controls
}

// Function to add event listeners to quantity controls
function addQuantityListeners() {
    document.querySelectorAll('.increase').forEach(button => {
        button.addEventListener('click', (event) => {
            const index = event.target.dataset.index;
            selectedOptions[index].quantity += 1;
            updateSelectedResult(); // Refresh the display
        });
    });

    document.querySelectorAll('.decrease').forEach(button => {
        button.addEventListener('click', (event) => {
            const index = event.target.dataset.index;
            if (selectedOptions[index].quantity > 1) {
                selectedOptions[index].quantity -= 1;
                updateSelectedResult(); // Refresh the display
            }
        });
    });

    document.querySelectorAll('.quantity').forEach(input => {
        input.addEventListener('input', (event) => {
            const index = event.target.dataset.index;
            selectedOptions[index].quantity = parseInt(event.target.value) || 1;
            updateTotalPrice(); // Recalculate the total price
        });
    });
}


// Function to calculate and update the total price for all selected options
function updateTotalPrice() {
    let totalPrice = 0;

    selectedOptions.forEach(option => {
        const discountedPrice = Math.floor(originalPrice * (100 - discount) / 100);
        totalPrice += discountedPrice * option.quantity;
    });

    document.querySelector(".total-price").innerText = `총 상품금액: ${totalPrice.toLocaleString()}원`;
}

// Option change listener for dropdown selection
document.getElementById("option").addEventListener("change", function() {
    selectedOptionValue = this.value;
    selectedOptionText = this.options[this.selectedIndex].text.split(" - ")[0];
    selectedOptionDesc = this.options[this.selectedIndex].text.split(" - ")[1] || "No description";

    addOrUpdateSelection(selectedOptionValue, selectedOptionText, selectedOptionDesc, quantity);
});



// Initial setup for total price and selected options display
updateTotalPrice();
updateSelectedResult();
// Function to remove an option from selectedOptions by index
function removeOption(index) {
    selectedOptions.splice(index, 1); // Remove option from array
    updateSelectedResult(); // Refresh the display
    updateTotalPrice(); // Update the total price
}





// Event listeners for the increase and decrease buttons
document.getElementById('decrease').addEventListener('click', function () {
    if (quantity > 1) {
        quantity -= 1;
        document.getElementById("quantity").value = quantity;

        // Update the last selected option's quantity in the array
        if (selectedOptionValue) {
            addOrUpdateSelection(selectedOptionValue, selectedOptionText, selectedOptionDesc, quantity);
        }
    }
});

document.getElementById('increase').addEventListener('click', function () {
    quantity += 1;
    document.getElementById("quantity").value = quantity;

    // Update the last selected option's quantity in the array
    if (selectedOptionValue) {
        addOrUpdateSelection(selectedOptionValue, selectedOptionText, selectedOptionDesc, quantity);
    }
});

// Update total price and selected result if user manually changes quantity
document.getElementById("quantity").addEventListener("input", function () {
    quantity = parseInt(this.value) || 1; // Set to 1 if invalid input
    this.value = quantity; // Correct the input if needed

    // Update the last selected option's quantity in the array
    if (selectedOptionValue) {
        addOrUpdateSelection(selectedOptionValue, selectedOptionText, selectedOptionDesc, quantity);
    }
});



// Initial total price calculation on page load
updateTotalPrice();
updateSelectedResult();

// Event listener for Buy Now button
document.getElementById("buy-now-btn").addEventListener("click", function(e) {
    if (selectedOptions.length === 0) {
        alert("옵션을 선택해주세요.");
        return;
    }

    const isConfirmed = confirm("구매하시겠습니까?");
    if (isConfirmed) {
        // Create an array to hold all selected product data
        const productDataArray = selectedOptions.map(option => ({
            productId: productId,
            productName: productName,
            originalPrice: originalPrice,
            finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
            quantity: option.quantity,
            file190: file190,
            optionId: option.optionId,
            optionName: option.optionText,
            optionDesc: option.optionDesc,
            point: point,
            discount: discount
        }));

        console.log

        // Store the array in localStorage (optional)
        localStorage.setItem("productDataArray", JSON.stringify(productDataArray));

        fetch("/market/buyNow", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(productDataArray) // Send as an array
        })
            .then(response => response.json())
            .then(data => {
                if (data.result === "success") {
                    const uid = document.getElementById("uid").value;
                    window.location.href = `/market/order/${uid}`;
                } else if (data.result === "login_required") {
                    if (confirm("로그인이 필요합니다. 로그인 하시겠습니까?")) {
                        window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
                    } else {
                        location.reload();
                    }
                } else if (data.result === "auth") {
                    alert("구매 권한이 없는 계정입니다. 관리자 또는 판매자는 구매할 수 없습니다.");
                } else {
                    alert("구매 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("구매 처리 중 오류가 발생했습니다.");
            });
    }
});

// Initial setting for Add to Cart buttons and functionality
document.querySelectorAll('.add-to-cart').forEach(btn => {
    btn.addEventListener('click', function () {
        const user = localStorage.getItem('user');
        if (!user) {
            alert('로그인 후 사용 가능합니다. 로그인 페이지로 이동합니다.');
            window.location.href = '/user/login';
            return;
        }

        if (quantity <= 0) {
            alert('수량을 1 이상으로 설정해 주세요.');
            return;
        } else if (!selectedOptionValue) {
            alert("옵션을 선택해 주세요");
            return;
        }

        if (confirm("장바구니에 추가 하시겠습니까.")) {
            const productCart = {
                productId: parseInt(productId, 10),
                productName: productName,
                originalPrice: originalPrice,
                finalPrice: finalPrice,
                quantity: quantity,
                file190: file190,
                shippingFee: shippingFee,
                optionId: parseInt(selectedOptionValue, 10),
                optionName: selectedOptionText,
                point: parseInt(point, 10),
                discount: discount
            };

            localStorage.setItem("productCart", JSON.stringify(productCart));
            fetch('/api/cart', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(productCart)
            })
                .then(resp => {
                    if (!resp.ok) throw new Error(`서버 응답 오류: ${resp.status}`);
                    return resp.json();
                })
                .then(data => {
                    if (data.status === 200) {
                        alert('장바구니에 추가 되었습니다!');
                    } else if (data.status === 401) {
                        alert('로그인 없이 이곳은 접근 금지! 빨리 로그인해 주세요');
                        window.location.href = '/user/login';
                    } else {
                        alert('장바구니에 추가하는 데 실패하셨습니다.');
                    }
                })
                .catch(error => {
                    console.error('장바구니에 추가하는 데 실패함:', error);
                    alert('장바구니에 추가하는 데 실패하셨습니다.');
                });
        }
    });
});
