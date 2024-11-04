// Global variables for product details
let selectedOptionValue = "";
let selectedOptionText = "";
let selectedOptionDesc = "";
let selectedOptions = []; // Array to store selected options

const productId = document.getElementById("productId").value;
const point = parseInt(document.getElementById("point").value, 10) || 0;
const productName = document.getElementById("productName").value;
const originalPrice = parseFloat(document.getElementById("originalPrice").innerText.replace(/,/g, "")) || 0;
const discount = parseInt(document.getElementById("discount").value, 10) || 0;
const file190 = document.getElementById("file190").value;
const shippingFee = parseInt(document.getElementById("shippingFee").getAttribute("data-shippingfee")) || 0;
let quantity = parseInt(document.getElementById("quantity").value, 10) || 1;
const shippingTerms = document.getElementById("shippingTerms").value;

const byCart = document.getElementById('buyCart');
console.log("byCart:", byCart);
console.log("shippingFee:", shippingFee);
console.log("shippingTerms:",shippingTerms)

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
    let totalOriginalPrice = 0; // 총 상품 금액 (할인 전)
    let totalDiscountedPrice = 0; // 할인 적용 금액 (할인 후)
    let totalDiscount = 0;
    // 기본 상품 1개의 가격 반영 (할인 전)
    if (selectedOptions.length === 0) {
        totalOriginalPrice = originalPrice * quantity;
        totalDiscount = Math.floor((originalPrice * discount) / 100) * quantity;
        totalDiscountedPrice = Math.floor(originalPrice * (100 - discount) / 100) * quantity;
    } else {
        // 선택된 옵션에 따라 가격 계산
        selectedOptions.forEach(option => {
            const discountedPricePerItem = Math.floor(originalPrice * (100 - discount) / 100);
            totalDiscount += Math.floor((originalPrice * discount) / 100) * option.quantity;
            totalOriginalPrice += originalPrice * option.quantity;
            totalDiscountedPrice += discountedPricePerItem * option.quantity;
        });
    }

    // Update HTML elements with calculated prices
    document.getElementById("originalTotalPrice").innerText = `${totalOriginalPrice.toLocaleString()}원`;
    document.getElementById("totalPrice").innerText = `-${totalDiscount.toLocaleString()}원`;

    // 배송비 계산 및 결제 예상금액 업데이트
    const totalShippingFee = calculateShippingFee(totalDiscountedPrice);
    updateExpectedTotal(totalDiscountedPrice, totalShippingFee);
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
        if (selectedOptionValue) {
            addOrUpdateSelection(selectedOptionValue, selectedOptionText, selectedOptionDesc, quantity);
        }
    }
});

// price 클래스를 가진 모든 요소를 선택
const priceElements = document.querySelectorAll('.price');

// 각 price 요소에 대해 반복하여 처리
priceElements.forEach(priceElement => {
    let priceValue = priceElement.textContent.trim().replace(/[^0-9]/g, ''); // 숫자가 아닌 문자를 제거
    priceValue = parseInt(priceValue, 10); // 정수로 변환

    if (!isNaN(priceValue)) { // 변환된 값이 NaN이 아닌 경우에만 적용
        priceElement.textContent = priceValue.toLocaleString();  // 천단위로 쉼표 추가
    }
});

document.getElementById('increase').addEventListener('click', function () {
    quantity += 1;
    document.getElementById("quantity").value = quantity;
    if (selectedOptionValue) {
        addOrUpdateSelection(selectedOptionValue, selectedOptionText, selectedOptionDesc, quantity);
    }
});

// Update total price and selected result if user manually changes quantity
document.getElementById("quantity").addEventListener("input", function () {
    quantity = parseInt(this.value, 10) || 1; // Set to 1 if invalid input
    this.value = quantity; // Correct the input if needed
    if (selectedOptionValue) {
        addOrUpdateSelection(selectedOptionValue, selectedOptionText, selectedOptionDesc, quantity);
    }
});

// Initial total price calculation on page load
updateTotalPrice();
updateSelectedResult();
const uidElement = document.getElementById("uid");
const uid = uidElement ? uidElement.value : null;



// Event listener for Buy Now button
document.getElementById("buy-now-btn").addEventListener("click", function(e) {
    if (!uid) {
        alert('로그인 후 이용해 주세요');
        window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
        return;
    } else if (selectedOptions.length === 0) {
        alert("옵션을 선택해주세요.");
        return;
    }
    const isConfirmed = confirm("구매하시겠습니까?");
    if (isConfirmed) {
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
            discount: discount,
            shippingFee: shippingFee,
            shippingTerms: shippingTerms,
        }));


        fetch("/market/buyNow", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(productDataArray)
        })
            .then(response => response.json())
            .then(data => {
                if (data.result === "success") {
                    localStorage.setItem("productDataArray", JSON.stringify(productDataArray));
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

        if (!uid) {
            alert('로그인 후 이용해 주세요');
            window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
            return;
        } else if (quantity <= 0) {
            alert('수량을 1 이상으로 설정해 주세요.');
            return;
        } else if (!selectedOptionValue) {
            alert("옵션을 선택해 주세요");
            return;
        }

        if (confirm("장바구니에 추가 하시겠습니까.")) {
            const finalPrice = Math.floor(originalPrice * (100 - discount) / 100);
            const productCart = {
                productId: parseInt(productId, 10),
                cartItemId: 0,
                productName: productName,
                originalPrice: originalPrice,
                finalPrice: finalPrice,
                quantity: quantity,
                file190: file190,
                shippingFee: shippingFee,
                optionId: parseInt(selectedOptionValue, 10),
                optionName: selectedOptionText,
                point: point,
                discount: discount,
                totalShippingFee: totalShippingFee
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

    document.querySelectorAll('.rating-display').forEach(display => {
        const rating = parseInt(display.textContent);
        let stars = '';

        for (let i = 1; i <= 5; i++) {
            if (i <= rating) {
                stars += '<span class="star-selected">&#9733;</span>'; // 선택된 별
            } else {
                stars += '<span class="star">&#9734;</span>'; // 선택되지 않은 별
            }
        }

        display.innerHTML = stars; // 별 모양으로 업데이트
    });


    let currentIndex = 0; // 현재 보여주는 이미지 인덱스
    const images = document.querySelector('.review-images');
    const totalImages = document.querySelectorAll('.reviewImg').length; // 전체 이미지 수

    document.addEventListener("DOMContentLoaded", function() {
        const reviewImagesContainer = document.querySelector(".review-images");
        const leftArrow = document.getElementById("leftArrow");
        const rightArrow = document.getElementById("rightArrow");

        // 이미지가 있을 때만 화살표를 표시
        if (reviewImagesContainer && reviewImagesContainer.querySelector(".reviewImg")) {
            leftArrow.style.display = "block";
            rightArrow.style.display = "block";
        }
    });

// 왼쪽 화살표 클릭 이벤트
    document.getElementById('leftArrow').addEventListener('click', function() {
        if (currentIndex > 0) {
            currentIndex--;
            updateSlide();
        }
    });

// 오른쪽 화살표 클릭 이벤트
    document.getElementById('rightArrow').addEventListener('click', function() {
        if (currentIndex < totalImages - 5 ) {
            currentIndex++;
            updateSlide();
        }
    });

// 슬라이드 업데이트 함수
    function updateSlide() {
        const offset = currentIndex * (152 + 10); // 이미지 너비 + 마진을 고려 (150px + 10px)
        images.style.transform = `translateX(${-offset}px)`; // 이미지 슬라이드

        // 오른쪽 화살표 비활성화 효과
        if (currentIndex === totalImages - 5) {
            document.getElementById('rightArrow').classList.add('disabled');
        } else {
            document.getElementById('rightArrow').classList.remove('disabled');
        }

        // 왼쪽 화살표 비활성화 효과
        if (currentIndex === 0) {
            document.getElementById('leftArrow').classList.add('disabled');
        } else {
            document.getElementById('leftArrow').classList.remove('disabled');
        }
    }

// 초기 버튼 상태 설정
    updateSlide();


// 초기화 및 각 상품 선택 이벤트에 따른 업데이트 실행
    updateTotalPrice();
    updateSelectedResult();

});
function calculateShippingFee(totalPrice) {
    // totalPrice와 shippingTerms 비교하여 shippingFee 결정
    const totalShippingFee = totalPrice >= parseInt(shippingTerms) ? 0 : shippingFee;
    document.getElementById("totalShippingFee").innerText = `${totalShippingFee.toLocaleString()}원`;
    return totalShippingFee;
}

function updateExpectedTotal(totalPrice, totalShippingFee) {
    // totalPrice와 totalShippingFee 합산하여 결제 예상금액 설정
    const expectedPrice = totalPrice + totalShippingFee;
    document.getElementById("expectedPrice").innerText = `${expectedPrice.toLocaleString()}원`;
}
/*document.addEventListener("DOMContentLoaded", function () {
    fetch("/seller/coupon/coupons")
        .then(resp => {
            if(!resp.ok){
                throw new Error("네트워크 응답 오류")
            }
            return
        })
        .then(data =>{
            console.log("쿠폰 목록:"data);
            const couponSelect = document.getElementById()
        })
})*/

/*document.getElementById("couponBtn").addEventListener("click", function () {

    alert("클릭됨")
    const couponData ={
        couponType
    }
})*/
// 모달 엘리먼트와 버튼, 닫기 버튼 가져오기
const modal = document.getElementById("discountModal");
const btn = document.getElementById("openDiscountModalBtn");
const span = document.getElementsByClassName("discount-modal-close")[0];

// 버튼을 클릭하면 모달 표시
btn.onclick = function() {
    modal.style.display = "flex";
    const productId = document.getElementById('productId').value;
    fetchCoupons(productId);

}

// 닫기 버튼을 클릭하면 모달 숨기기
span.onclick = function() {
    modal.style.display = "none";
}

// 모달 바깥을 클릭하면 모달 숨기기
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}


function fetchCoupons(productId) {

    let url = productId ? `/seller/coupon/${productId}` : '/seller/coupon/all/coupons';

    fetch(url)
        .then(resp => {
            if(!resp.ok){
                throw  new Error('인터넷연결 문제')
            }
            return resp.json()
        })
        .then(data => {
            displayCoupons(data);
        })
        .catch(err => {
            console.error(err,'요청중 문제 발생함')
        })
}








// 쿠폰 목록 표시하기
function displayCoupons(coupons) {
    const couponContainer = document.getElementById('discountCouponItems');
    couponContainer.innerHTML = '';

    if (coupons.length === 0) {
        const noCoupon = document.createElement('div');
        noCoupon.className = 'no-coupon';
        noCoupon.textContent = '등록된 쿠폰이 없습니다.';
        couponContainer.appendChild(noCoupon);
    } else {
        coupons.forEach(coupon => {
            const couponItem = document.createElement('div');
            couponItem.className = 'discount-coupon-item';
            couponItem.innerHTML = `
               <div class="discount-coupon-info">
                    <div class="discount-amount">${coupon.couponName} (${coupon.benefit})</div>
                    <div class="discount-description">${coupon.notes}</div> <!-- notes로 변경 -->
                    <div class="discount-dates">
                        <span>유효기간: ${coupon.startDate} ~ ${coupon.endDate}</span>
                    </div>
                </div>
                <button class="discount-apply-btn" onclick="applyCoupon('${coupon.couponId}')">발급받기</button>
            `;
            couponContainer.appendChild(couponItem);
        });
    }
}