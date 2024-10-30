document.addEventListener('DOMContentLoaded', function () {

    // Format prices with thousands separator for all elements with class 'price'
    const priceElements = document.querySelectorAll('.price');
    priceElements.forEach(priceElement => {
        let priceValue = priceElement.textContent.trim().replace(/[^0-9]/g, '');
        priceValue = parseInt(priceValue, 10);
        if (!isNaN(priceValue)) {
            priceElement.textContent = priceValue.toLocaleString();
        }
    });

    // Define necessary elements for scrolling functionality
    const aside = document.querySelector('aside');
    const sectionWrapper = document.querySelector('.sectionWrapper');
    const headerHeight = 188;
    const footer = document.querySelector('footer');
    const asideHeight = aside.offsetHeight;
    const sectionHeight = sectionWrapper.offsetHeight;
    const topHeight = headerHeight + sectionHeight;

    function handleAsideScroll() {
        const scrollPosition = window.scrollY;
        const footerTop = footer.getBoundingClientRect().top + window.scrollY;
        if (scrollPosition <= topHeight && (scrollPosition + asideHeight + 50) < footerTop) {
            aside.classList.remove('scroll');
            aside.style.bottom = '';
        } else if ((scrollPosition + asideHeight + 50) >= footerTop) {
            aside.classList.add('scroll');
            aside.style.top = `${footerTop - asideHeight - 50}px`;
        } else {
            aside.classList.add('scroll');
            aside.style.position = '';
            aside.style.top = '';
        }
    }

    window.addEventListener('scroll', handleAsideScroll);
    window.addEventListener('resize', handleAsideScroll);

    // Modal control for address selection
    const addressSelectModal = document.getElementById("addressSelectModal");
    const addressRegisterModal = document.getElementById("addressRegisterModal");
    const closeAddressSelect = addressSelectModal.querySelector(".close");
    const closeAddressRegister = addressRegisterModal.querySelector(".close");
    const editButtons = document.querySelectorAll(".edit-btn");

    document.querySelector('.address_change').addEventListener('click', () => {
        addressSelectModal.style.display = "block";
    });

    closeAddressSelect.addEventListener('click', () => {
        addressSelectModal.style.display = "none";
    });

    closeAddressRegister.addEventListener('click', () => {
        addressRegisterModal.style.display = "none";
    });

    window.addEventListener('click', event => {
        if (event.target == addressSelectModal) addressSelectModal.style.display = "none";
        if (event.target == addressRegisterModal) addressRegisterModal.style.display = "none";
    });

    editButtons.forEach(button => {
        button.addEventListener('click', () => {
            addressSelectModal.style.display = "none";
            addressRegisterModal.style.display = "block";
        });
    });

    document.querySelector('.new-address-btn').addEventListener('click', () => {
        addressSelectModal.style.display = "none";
        addressRegisterModal.style.display = "block";
    });

    // Retrieve product data from localStorage and display
    const productDataArray = JSON.parse(localStorage.getItem("productDataArray")) || [];

    try {
        // const productDataArray = JSON.parse(localStorage.getItem("productDataArray")) || [];
        if (productDataArray.length > 0) {
            const orderTable = document.querySelector(".productOrder");
            productDataArray.forEach(data => {
                orderTable.insertAdjacentHTML("beforeend", createProductRow(data));
            });
        } else {

        }
    } catch (error) {
        console.error("Error parsing product data:", error);
    }

    // Function to create a new product row in the table
    function createProductRow(data) {
        return `
            <tr class="order-row">
                <td>
                    <div><img src="/uploads/productimg/${data.file190}" alt="${data.productName}"></div>
                    <div>
                        <span>${data.productName}</span>
                        <p class="product_option"> [ 옵션 : ${data.optionName} ]</p>
                    </div>
                </td>
                <td>
                    <div class="qnt">
                        <input type="number" class="T_quantity" value="${data.quantity}" readonly>
                    </div>
                </td>
                <td><span class="T_originalPrice price" data-original="${data.originalPrice}">${(data.originalPrice).toLocaleString()}</span></td>
                <td><span class="T_discount">${data.discount}</span>%</td>
                <td><span class="T_point">${data.point}</span></td>
                <td><span class="T_shippingFee" data-ship="${data.shippingFee}">${data.shippingFee}</span></td>
                <td><span class="T_finalPrice price">${(data.finalPrice * data.quantity).toLocaleString()}</span></td>
            </tr>
        `;
    }

    // Elements for final order calculations
    const currentPoint = parseInt(document.getElementById("currentPoint").value);
    const usedPointInput = document.getElementById("used_point");
    const couponSelect = document.querySelector("select[name='coupons']");
    const usedPointResult = document.querySelector(".usedPointResult");
    const usedCouponResult = document.querySelector(".usedCouponResult");
    const discountResult = document.querySelector(".DiscountResult");
    const finalOrderQuantity = document.getElementById("finalOrderQuantity");
    const finalOrderProductPrice = document.getElementById("finalOrderProductPrice");
    const finalOrderDiscount = document.getElementById("finalOrderDiscount");
    const finalOrderDeliveryFee = document.getElementById("finalOrderDeliveryFee");
    const finalOrderTotal = document.getElementById("finalOrderTotal");
    const finalOrderPoint = document.getElementById("finalOrderPoint");

    // Calculate and display discount results based on used points and coupons
    usedPointInput.addEventListener("input", function () {
        const usedPoint = parseInt(usedPointInput.value) || 0;
        if (usedPoint > currentPoint) {
            alert("사용가능한 포인트가 부족합니다.");
            usedPointInput.value = 0;
        } else {
            updateDiscountResult();
        }
    });

    couponSelect.addEventListener("change", updateDiscountResult);

    function updateDiscountResult() {
        const usedPoint = parseInt(usedPointInput.value) || 0;

        // Get coupon discount based on the selected coupon
        const selectedCouponValue = couponSelect.options[couponSelect.selectedIndex].value;
        let couponDiscount = 0;
        if (selectedCouponValue === "1") {
            couponDiscount = Math.floor(totalProductPrice() * 0.03); // Example: 3% discount
        }

        // Calculate initial order total before points
        const initialTotalOrderAmount = totalProductPrice() - couponDiscount + totalShippingFee();

        // If used points exceed the total order amount, limit them
        const limitedUsedPoint = Math.min(usedPoint, initialTotalOrderAmount);

        usedPointInput.value = limitedUsedPoint;
        currentIn.textContent = currentPoint - limitedUsedPoint;

        // Update discount and final order information
        usedPointResult.textContent = limitedUsedPoint;
        usedCouponResult.textContent = couponDiscount;

        const totalDiscount = limitedUsedPoint + couponDiscount;
        discountResult.textContent = totalDiscount;

        finalOrderQuantity.textContent = totalQuantity();
        finalOrderProductPrice.textContent = totalProductPrice().toLocaleString();
        finalOrderDiscount.textContent = totalDiscount.toLocaleString();
        finalOrderDeliveryFee.textContent = totalShippingFee().toLocaleString();

        const orderTotal = initialTotalOrderAmount - limitedUsedPoint;
        finalOrderTotal.textContent = orderTotal.toLocaleString();
        finalOrderPoint.textContent = Math.floor(orderTotal * 0.01).toLocaleString();
    }

    // Calculate the total values based on all selected options
    function totalQuantity() {
        return Array.from(document.querySelectorAll('.T_quantity'))
            .reduce((total, elem) => total + parseInt(elem.value || 0), 0);
    }

    function totalProductPrice() {
        return Array.from(document.querySelectorAll('.T_originalPrice'))
            .reduce((total, elem) => total + parseInt(elem.dataset.original || 0), 0);
    }

    function totalShippingFee() {
        return Array.from(document.querySelectorAll('.T_shippingFee'))
            .reduce((total, elem) => total + parseInt(elem.dataset.ship || 0), 0);
    }

    calculateTotals();

    function calculateTotals() {
        // 총 수량, 상품 금액, 배송비 계산
        const totalQuantity = Array.from(document.querySelectorAll('.T_quantity'))
            .reduce((total, elem) => total + parseInt(elem.value || 0), 0);

        const totalProductPrice = Array.from(document.querySelectorAll('.T_originalPrice'))
            .reduce((total, elem) => total + parseInt(elem.dataset.original || 0), 0);

        const totalShippingFee = Array.from(document.querySelectorAll('.T_shippingFee'))
            .reduce((total, elem) => total + parseInt(elem.dataset.ship || 0), 0);

        const totalDiscountAmount = Array.from(document.querySelectorAll('.T_discount'))
            .reduce((total, elem, index) => {
                const originalPrice = parseInt(document.querySelectorAll('.T_originalPrice')[index].dataset.original || 0);
                const discountPercentage = parseInt(elem.innerText || 0);
                return total + Math.floor((originalPrice * discountPercentage) / 100);
            }, 0);

        // 최종 결과 업데이트
        finalOrderQuantity.textContent = totalQuantity;
        finalOrderProductPrice.textContent = totalProductPrice.toLocaleString();
        finalOrderDiscount.textContent = totalDiscountAmount.toLocaleString();
        finalOrderDeliveryFee.textContent = totalShippingFee.toLocaleString();
        finalOrderPoint.textContent = Math.floor((totalProductPrice - totalDiscountAmount) * 0.01).toLocaleString(); // 포인트 예제
    }




    // 구매하기 버튼 클릭 시
    document.querySelector('.order-Btn').addEventListener('click', function (event) {
        event.preventDefault();  // 기본 제출 동작 방지

        if (productDataArray.length > 0) {
            // 서버에 데이터를 전송
            fetch('/order/saveOrder', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(productDataArray),
            })
                .then(response => response.json())
                .then(data => {
                    if (data.result === 'success') {
                        alert('주문이 완료되었습니다!');
                        localStorage.removeItem('productDataArray');  // 성공 시 로컬 데이터 삭제
                        window.location.href = '/order/confirmation'; // 완료 후 페이지 이동
                    } else {
                        alert('주문 처리 중 오류가 발생했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('서버와의 통신 중 오류가 발생했습니다.');
                });
        } else {
            alert('주문할 상품이 없습니다.');
        }
    });

});



// Detect unload event for refreshing
window.addEventListener("beforeunload", (event) => {
    if (sessionStorage.getItem("page_reload") === "true") {
        sessionStorage.removeItem("page_reload");
    } else {
        localStorage.removeItem("productDataArray");
    }
});
