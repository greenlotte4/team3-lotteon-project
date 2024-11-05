const productId = document.getElementById("productId").value;
const point = parseInt(document.getElementById("point").value, 10) || 0;
const productName = document.getElementById("productName").value;
const originalPrice = parseFloat(document.getElementById("originalPrice").innerText.replace(/,/g, "")) || 0;
console.log("originalPrice", originalPrice);
const discount = parseInt(document.getElementById("discount").value, 10) || 0;
console.log("discount", discount);
const finalPrice = parseFloat(document.getElementById("finalPrice").innerText.replace(/,/g, ""));
console.log("finalPrice", finalPrice);
const file190 = document.getElementById("file190").value;
const shippingFee = parseInt(document.getElementById("shippingFee").getAttribute("data-shippingfee")) || 0;
let quantity = parseInt(document.getElementById("quantity").value, 10) || 1;
const shippingTerms = document.getElementById("shippingTerms").value;
const buyNowBtn = document.getElementById("buy-now-btn");
const addToCartBtn = document.getElementById("add-to-cart");

const byCart = document.getElementById('buyCart');

document.addEventListener('DOMContentLoaded', function () {
    // 필요한 요소들 정의
    const optionSelectElements = document.querySelectorAll('.option-select');
    const quantityInput = document.getElementById('quantity');
    const selectResult = document.querySelector('.selectResult');
    const originalTotalPriceElement = document.getElementById('originalTotalPrice');
    const additionalPriceElement = document.getElementById('additionalPrice');
    const totalPriceElement = document.getElementById('totalPrice');
    const totalShippingFeeElement = document.getElementById('totalShippingFee');
    const expectedPriceElement = document.getElementById('expectedPrice');
    const uidElement = document.getElementById("uid");
    const uid = uidElement ? uidElement.value : null;
    const stockStatusElement = document.getElementById("stockStatus"); // Element to show stock status


    let selectOptionGroup = [];
    let selectedOptions = []; // Array to store selected options
    let additionalPriceMatch = 0; // 추가 금액 초기화
    let additionalPrice = 0;
    let quantity = parseInt(quantityInput.value, 10) || 1;
    // 수량 증가/감소 버튼 이벤트
    document.getElementById('increase').addEventListener('click', () => {
        quantity++;
        quantityInput.value = quantity;
        updatePrices();
        updateSelectResult();
    });

    document.getElementById('decrease').addEventListener('click', () => {
        if (quantity > 1) {
            quantity--;
            quantityInput.value = quantity;
            updatePrices();
            updateSelectResult();
        }
    });
    // Listen for changes in the first option group
    optionSelectElements[0].addEventListener('change', function () {
        const selectedOptionValue = this.options[this.selectedIndex].value;

        // Filter combinations based on the selected option in the first group
        const matchingCombinations = optionCombinations.filter(combination =>
            combination.options.some(option => option.groupName === 'Group1' && option.optionName === selectedOptionValue)
        );

        // For each subsequent option group, update the additional price
        optionSelectElements.forEach((select, index) => {
            if (index === 0) return; // Skip the first group

            // Loop through each option in the current group and update additionalPrice based on matching combinations
            Array.from(select.options).forEach(option => {
                const matchingOption = matchingCombinations.find(combination =>
                    combination.options.some(opt => opt.groupName === select.getAttribute('data-groupname') && opt.optionName === option.value)
                );

                // Set the additionalPrice if a matching option is found
                if (matchingOption) {
                    const additionalPrice = matchingOption.options.find(opt => opt.optionName === option.value)?.additionalPrice || 0;
                    option.setAttribute('data-additionalprice', additionalPrice);
                    option.textContent = `${option.value} (+${additionalPrice}원)`;
                }
            });
        });
    });

    optionSelectElements.forEach((select, index) => {
        select.addEventListener('change', function () {

            const selectedOptionValue = this.options[this.selectedIndex].value;
            const itemId = this.options[this.selectedIndex].getAttribute('data-item-id');
            const groupName = this.getAttribute('data-groupname');

            selectedOptions[index] = {
                value: selectedOptionValue,
                itemId: itemId
            };
            selectOptionGroup[index] = `${groupName} ${selectedOptionValue}`;

            console.log(`Selected option (${index}):`, selectedOptionValue);

            // Check if all options are selected
            if (selectedOptions.length === optionSelectElements.length && selectedOptions.every(opt => opt.value)) {
                const matchingCombination = optionCombinations.find(combination => {
                    const optionValues = combination.combination
                        .split(" / ")
                        .map(opt => opt.split(":")[1].trim());
                    return selectedOptions.every(selectedOpt => optionValues.includes(selectedOpt.value));
                });

                additionalPriceMatch = matchingCombination ? parseInt(matchingCombination.additionalPrice) || 0 : 0;
                additionalPrice = additionalPriceMatch;

                if (matchingCombination) {
                    const combinationId = matchingCombination.combinationId;
                    const combinationString = matchingCombination.combination;

                    selectedOptions.forEach(opt => {
                        opt.combinationId = combinationId;
                        opt.combinationString = combinationString;
                    });
                    checkStock(matchingCombination);

                } else {
                    selectedOptions.forEach(opt => {
                        opt.combinationId = null;
                        opt.combinationString = null;
                    });
                }

                applyAdditionalPrice(additionalPriceMatch);


            }

            // Call these functions to ensure `totalPrice` and `selectResult` are updated
            updatePrices();
            updateSelectResult();
        });
    });

    function checkStock(combination) {
        const stock = combination.stock;

        // Display stock status
        if (stock > 0) {
            stockStatusElement.textContent = `주문가능 수량: ${stock} `;
            stockStatusElement.style.color = 'green';
            buyNowBtn.disabled = false;
            addToCartBtn.disabled = false;
        } else {
            stockStatusElement.textContent = "Out of Stock";
            stockStatusElement.style.color = 'red';
            buyNowBtn.disabled = true;
            addToCartBtn.disabled = true;
        }
    }

    function updateSelectResult() {
        const quantity = quantityInput.value;
        console.log("selectOptionGroup:", selectOptionGroup); // Log to check if options are populated
        console.log("Option length check:", selectOptionGroup.length === optionSelectElements.length);
        console.log("All options selected check:", selectOptionGroup.every(opt => opt));

        // 모든 옵션이 선택된 경우 조합과 수량을 표시
        if (selectOptionGroup.length === optionSelectElements.length && selectOptionGroup.every(opt => opt)) {
            const combinationString = selectOptionGroup.join(' / ');
            selectResult.textContent = `[선택한 옵션 조합] ${combinationString} (+${additionalPrice}),  수량: ${quantity}`;
            console.log("Updated selectResult:", selectResult.textContent);

        } else {
            // 옵션이 모두 선택되지 않은 경우 결과 초기화
            selectResult.textContent = '';
            console.log("Not all options selected, cleared selectResult.");

        }
    }


    // 추가 금액을 표시하는 함수
    function applyAdditionalPrice(price) {
        console.log('적용된 추가 금액:', price);
        additionalPrice = parseFloat(price);
        console.log(additionalPrice);
        additionalPriceElement.textContent = `${additionalPrice.toLocaleString()}원`;
    }

    // 가격 계산 및 업데이트 함수
    function updatePrices() {
        const originalPrice = parseFloat(document.getElementById('originalPrice').innerText.replace(/,/g, "")) || 0;
        const discount = parseInt(document.getElementById('discount').value, 10) || 0;

        const totalOriginalPrice = originalPrice * quantity;
        const discountAmount = Math.floor(((discount) * totalOriginalPrice) / 100 / 10) * 10; // 10원 단위 절삭
        console.log("discountAmount", discountAmount);
        console.log("additionalPrice!!!", additionalPrice);
        const finalTotalPrice = totalOriginalPrice + (additionalPrice * quantity) - discountAmount;

        // 배송비 계산 (예시로 3000원을 사용)
        const shippingTerms = parseInt(document.getElementById("shippingTerms").value, 10) || 0;
        const shippingFee = finalTotalPrice >= shippingTerms ? 0 : parseInt(document.getElementById('shippingFee').getAttribute('data-shippingfee')) || 3000;

        // 결제 예상금액 계산
        const expectedPrice = finalTotalPrice + shippingFee;

        // 각 <span> 요소에 값을 업데이트
        originalTotalPriceElement.textContent = `${totalOriginalPrice.toLocaleString()}원`;
        additionalPriceElement.textContent = `${(additionalPrice * quantity).toLocaleString()}원`;
        totalPriceElement.textContent = `-${discountAmount.toLocaleString()}원`;
        totalShippingFeeElement.textContent = `${shippingFee.toLocaleString()}원`;
        expectedPriceElement.textContent = `${expectedPrice.toLocaleString()}원`;
    }

    // 초기 가격 계산
    updatePrices();


    // Event listener for Buy Now button
    document.getElementById("buy-now-btn").addEventListener("click", function (e) {
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
            const productDataArray = [];

            if (optionSelectElements.length > 0 && selectedOptions.every(opt => opt)) {
                // Include options in the data if options are selected
                productDataArray.push({
                    productId: productId,
                    productName: productName,
                    originalPrice: originalPrice,
                    finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
                    quantity: quantity,
                    file190: file190,
                    options: selectedOptions.map(opt => ({
                        itemId: opt.itemId,
                        combinationId: opt.combinationId,
                        combinationString: opt.combinationString, // Include combinationString here
                        optionName: opt.value,
                        additionalPrice: additionalPrice
                    })),
                    point: point,
                    discount: discount,
                    shippingFee: shippingFee,
                    shippingTerms: shippingTerms
                });
            } else if (optionSelectElements.length === 0) {
                // If no options, just send productId and quantity
                productDataArray.push({
                    productId: productId,
                    productName: productName,
                    file190: file190,
                    finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
                    discount: discount,
                    quantity: quantity,
                    shippingFee: shippingFee,
                    shippingTerms: shippingTerms
                });
            }
            localStorage.setItem("productDataArray", JSON.stringify(productDataArray));

            console.log("productDataArray", productDataArray);

            fetch("/market/buyNow", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
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
                const productDataArray = [];

                if (optionSelectElements.length > 0 && selectedOptions.every(opt => opt)) {
                    // Include options in the data if options are selected
                    productDataArray.push({
                        productId: productId,
                        productName: productName,
                        originalPrice: originalPrice,
                        finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
                        quantity: quantity,
                        file190: file190,
                        options: selectedOptions.map(opt => ({
                            itemId: opt.itemId,
                            combinationId: opt.combinationId,
                            combinationString: opt.combinationString, // Include combinationString here
                            optionName: opt.value,
                            additionalPrice: additionalPrice
                        })),
                        point: point,
                        discount: discount,
                        shippingFee: shippingFee,
                        shippingTerms: shippingTerms
                    });
                } else if (optionSelectElements.length === 0) {
                    // If no options, just send productId and quantity
                    productDataArray.push({
                        productId: productId,
                        productName: productName,
                        file190: file190,
                        finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
                        discount: discount,
                        quantity: quantity,
                        shippingFee: shippingFee,
                        shippingTerms: shippingTerms
                    });
                }

                console.log("productDataArray", productDataArray);


                localStorage.setItem("productDataArray", JSON.stringify(productDataArray));
                fetch('/api/cart', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
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
                    headers: {'Content-Type': 'application/json'},
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

        document.addEventListener("DOMContentLoaded", function () {
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
        document.getElementById('leftArrow').addEventListener('click', function () {
            if (currentIndex > 0) {
                currentIndex--;
                updateSlide();
            }
        });

// 오른쪽 화살표 클릭 이벤트
        document.getElementById('rightArrow').addEventListener('click', function () {
            if (currentIndex < totalImages - 5) {
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
    btn.onclick = function () {
        modal.style.display = "flex";
        const productId = document.getElementById('productId').value;
        fetchCoupons(productId);

    }

// 닫기 버튼을 클릭하면 모달 숨기기
    span.onclick = function () {
        modal.style.display = "none";
    }

// 모달 바깥을 클릭하면 모달 숨기기
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }


    function fetchCoupons(productId) {

        let url = productId ? `/seller/coupon/${productId}` : '/seller/coupon/all/coupons';

        fetch(url)
            .then(resp => {
                if (!resp.ok) {
                    throw new Error('인터넷연결 문제')
                }
                return resp.json()
            })
            .then(data => {
                displayCoupons(data);
            })
            .catch(err => {
                console.error(err, '요청중 문제 발생함')
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
});