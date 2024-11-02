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
        if (productDataArray.length > 0) {
            const orderTable = document.querySelector(".productOrder");
            productDataArray.forEach(data => {
                orderTable.insertAdjacentHTML("beforeend", createProductRow(data));
            });
        }
    } catch (error) {
        console.error("Error parsing product data:", error);
    }

    // Function to calculate total shipping fee based on grouped product data
    function calculateShippingFee(dataArray) {
        const groupedShippingFees = {};

        // Group products by productId and calculate the total final price for each group
        dataArray.forEach(data => {
            const productId = data.productId;
            const finalPrice = data.finalPrice * data.quantity;

            // Initialize or accumulate total price for the product group
            if (!groupedShippingFees[productId]) {
                groupedShippingFees[productId] = {
                    totalFinalPrice: 0,
                    shippingFee: parseInt(data.shippingFee || 0),
                    shippingTerms: parseInt(data.shippingTerms || 0),
                };
            }

            groupedShippingFees[productId].totalFinalPrice += finalPrice;
        });

        // Set shipping fee to 0 if total price exceeds shipping terms
        for (const productId in groupedShippingFees) {
            const productGroup = groupedShippingFees[productId];
            if (productGroup.totalFinalPrice >= productGroup.shippingTerms) {
                productGroup.shippingFee = 0;
            }
        }

        return groupedShippingFees;
    }

    // Function to create a new product row in the table
    function createProductRow(data) {
        // Get grouped shipping fees for all products
        const shippingFees = calculateShippingFee(productDataArray);
        const shippingFee = shippingFees[data.productId].shippingFee;

        return `
        <tr class="order-row">
            <td>
                <div><img src="/uploads/productImg/${data.file190}" alt="${data.productName}"></div>
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
            <td><span class="T_shippingFee" data-ship="${shippingFee}">${shippingFee.toLocaleString()}</span></td>
            <td><span class="T_finalPrice price">${(data.finalPrice * data.quantity).toLocaleString()}</span></td>
            <td><input type="hidden" class="shippingTerms" value="${data.shippingTerms}"></td>
        </tr>
    `;
    }

    // Elements for final order calculations

    let orderItem= {};
    let totalDiscount = 0;
    let totalDiscountPandC=0;
    let orderTotal=0;
    const currentPoint = parseInt(document.getElementById("currentPoint").value);
    const usedPointInput = document.getElementById("used_point");
    const couponSelect = document.querySelector("select[name='coupons']");
    const usedPointResult = document.querySelector(".usedPointResult");
    const usedPointResultspan = document.getElementById("usedPointResult");
    const usedCouponResult = document.querySelector(".usedCouponResult");
    const discountResult = document.querySelector(".DiscountResult");
    const DiscountResult = document.getElementById("finalDiscountResult");
    const finalOrderQuantity = document.getElementById("finalOrderQuantity");
    const finalOrderProductPrice = document.getElementById("finalOrderProductPrice");
    const finalOrderDiscount = document.getElementById("finalOrderDiscount");
    const finalOrderDeliveryFee = document.getElementById("finalOrderDeliveryFee");
    const finalOrderTotal = document.getElementById("finalOrderTotal");
    const finalOrderPoint = document.getElementById("finalOrderPoint");
    // Retrieve the elements
    const postcodeElement = document.getElementById("M_postcode");
    const addressElement = document.querySelector(".totalAddress");

    //수령자
    const receiver = document.getElementById('receiver').value;
    console.log(receiver);

    //핸드폰
    const hp = document.getElementById('hp').value;
    console.log(hp);



// Get the values from data attributes
    const postcode = postcodeElement.getAttribute("data-postcode");
    const addr = addressElement.getAttribute("data-addr");
    const addr2 = addressElement.getAttribute("data-addr2");
    // Log or use the values as needed
    console.log("Postcode:", postcode);
    console.log("Address Line 1:", addr);
    console.log("Address Line 2:", addr2);

// Calculate and display discount results based on used points and coupons
    const pointuseBtn = document.getElementById("pointuseBtn");

    pointuseBtn.addEventListener("click", function () {
        const usedPoint = parseInt(usedPointInput.value) || 0;
        if (usedPoint > currentPoint) {
            alert("사용가능한 포인트가 부족합니다.");
            usedPointInput.value = 0;
        } else {
            updateDiscountResult();
        }
    });

    // // Calculate and display discount results based on used points and coupons
    // usedPointInput.addEventListener("input", function () {
    //     const usedPoint = parseInt(usedPointInput.value) || 0;
    //     if (usedPoint > currentPoint) {
    //         alert("사용가능한 포인트가 부족합니다.");
    //         usedPointInput.value = 0;
    //     } else {
    //         updateDiscountResult();
    //     }
    // });

    couponSelect.addEventListener("change", updateDiscountResult);


    // Function to update discount and calculate final order details
    function updateDiscountResult() {
        const usedPoint = parseInt(usedPointInput.value) || 0;

        // Get coupon discount based on the selected coupon
        const selectedCouponValue = couponSelect.options[couponSelect.selectedIndex]?.value || "0";
        let couponDiscount = 0;
        if (selectedCouponValue === "1") {
            couponDiscount = Math.floor(totalProductPrice() * 0.03); // Example: 3% discount
        }

        // Calculate the initial total before applying points
        const initialTotalOrderAmount = totalProductPrice() - couponDiscount + totalShippingFee();

        // Apply the points but limit them to the total order amount
        const limitedUsedPoint = Math.min(usedPoint, initialTotalOrderAmount);
        usedPointInput.value = limitedUsedPoint;
        currentIn.textContent = currentPoint - limitedUsedPoint;

        // Update each discount and display the final results
        usedPointResult.textContent = limitedUsedPoint;
        usedPointResultspan.innerText = limitedUsedPoint;
        usedCouponResult.textContent = couponDiscount;

        // Total discount including product discounts, coupon, and used points
        totalDiscount = totalProductDiscount();   // 상품 할인금액
        totalDiscountPandC = limitedUsedPoint + couponDiscount;   // 쿠폰 및 포인트 사용금액
        discountResult.textContent = totalDiscountPandC;
        DiscountResult.textContent = totalDiscountPandC;

        // Update final order information
        finalOrderQuantity.textContent = totalQuantity();
        finalOrderProductPrice.textContent = totalProductPrice().toLocaleString();
        finalOrderDiscount.textContent = totalDiscount.toLocaleString();
        finalOrderDeliveryFee.textContent = totalShippingFee().toLocaleString();

        orderTotal = totalProductPrice() - totalDiscountPandC - totalDiscount + totalShippingFee();
        finalOrderTotal.textContent = orderTotal.toLocaleString();
        finalOrderPoint.textContent = Math.floor(orderTotal * 0.01).toLocaleString();
    }

    function totalProductDiscount() {
        return Array.from(document.querySelectorAll('.T_discount')).reduce((total, elem, index) => {
            const originalPrice = parseInt(document.querySelectorAll('.T_originalPrice')[index].dataset.original || 0);
            const quantity = document.querySelectorAll('.T_quantity')[index].value || 0;
            const discountPercentage = parseInt(elem.innerText || 0);

            return total + Math.floor((originalPrice * discountPercentage) / 100) * quantity;
        }, 0);
    }

    calculateTotals();

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

    function calculateTotals() {
        // Total quantity, product price, and shipping fee calculation
        const totalQuantity = Array.from(document.querySelectorAll('.T_quantity')).reduce((total, elem) => total + parseInt(elem.value || 0), 0);
        const totalProductPrice = Array.from(document.querySelectorAll('.T_originalPrice')).reduce((total, elem) => total + parseInt(elem.dataset.original || 0), 0);
        const totalShippingFee = Array.from(document.querySelectorAll('.T_shippingFee')).reduce((total, elem) => total + parseInt(elem.dataset.ship || 0), 0);

        const totalDiscountAmount = totalProductDiscount();

        // Update final result display
        finalOrderQuantity.textContent = totalQuantity;
        finalOrderProductPrice.textContent = totalProductPrice.toLocaleString();
        finalOrderDiscount.textContent = (totalDiscountAmount + totalDiscount).toLocaleString();
        finalOrderDeliveryFee.textContent = totalShippingFee.toLocaleString();
        finalOrderPoint.textContent = Math.floor((totalProductPrice - totalDiscountAmount) * 0.01).toLocaleString();
    }

    // Call updateDiscountResult on page load to initialize totals
    updateDiscountResult();

    // Get the select element
    const shippingInfoSelect = document.getElementById("shippingInfo");

    const InfoInput = document.getElementById("InfoInput");
    shippingInfoSelect.addEventListener('change', () => {
        console.log("선택됨",shippingInfoSelect.value)
        if (shippingInfoSelect.value === '6') {
            InfoInput.type = 'text';
        } else {
            InfoInput.type = 'hidden';
        }
    });
// Function to get the selected value
    function getSelectedShippingInfo() {
        const selectedValue = shippingInfoSelect.value;
        if(selectedValue === '6'){
            console.log(InfoInput.value);
            return InfoInput.value;
        }
        console.log("Selected shipping info:", selectedValue);
        return selectedValue;
    }

// Example: Call this function on a button click or when the selection changes
    shippingInfoSelect.addEventListener("change", getSelectedShippingInfo);

    const paymentOptions = document.getElementsByName('payment');
    let selectedPayment=null;
    paymentOptions.forEach(option => {
        option.addEventListener('change', () => {
            selectedPayment = document.querySelector('input[name="payment"]:checked').value;
            console.log("선택된 결제 방식:", selectedPayment);
        });
    })
    let couponValue = 0;
    let couponText= 0;
    couponSelect.addEventListener('change', () => {
        const selectedOption = couponSelect.options[couponSelect.selectedIndex];
        couponValue = selectedOption.value ||1;
        couponText = selectedOption.text || 1;

        console.log("쿠폰 값 (value):", couponValue);
        console.log("쿠폰 사용 금액 (text):", couponText);
    });


    function updateOrderItem() {
        orderItem = {
            productDataArray: productDataArray,            // List<BuyNowRequestDTO>로 매핑
            receiver: receiver,                            // 일치
            hp: hp,                                        // 일치
            postcode: postcode,                            // 일치
            addr1: addr,                                   // 일치
            addr2: addr2,                                  // 일치
            TotalDiscount: finalOrderDiscount.textContent,   //총 할인금액
            productDiscount: finalOrderDiscount.textContent,
            totalPointandCoupon:  totalDiscountPandC,
            usedPointResult: Number(usedPointResult.textContent),  // 일치
            usedCouponResult: Number(couponValue),         // usedCoupon -> usedCouponResult로 매핑
            usedCouponName:couponText,
            totalOrderQuantity: finalOrderQuantity.textContent,
            totalOriginalPrice:finalOrderProductPrice.textContent,
            totalShippingFee: finalOrderDeliveryFee.textContent,                           // 기본값 설정 필요
            totalFinalPrice: finalOrderTotal.textContent,                            // 기본값 설정 필요
            credit: selectedPayment,                       // 일치
            couponId: Number(couponValue),            // couponValue를 couponId로 매핑 (확인 필요)
            shippingInfo: getSelectedShippingInfo(),
            finalOrderPoint: finalOrderPoint.textContent
        };
    }

    updateOrderItem();
    console.log(orderItem);
    document.querySelector('.order-Btn').addEventListener('click', function (event) {
        event.preventDefault();  // 기본 제출 동작 방지

        if (productDataArray.length > 0) {
            const isConfirm = confirm("주문하시겠습니까?");
            if(isConfirm){
                updateOrderItem();
                console.log(orderItem)
                // 서버에 데이터를 전송
                fetch('/market/order/saveOrder', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(orderItem),
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.result > 0) {
                            alert('주문이 완료되었습니다!');
                            localStorage.removeItem('productDataArray');  // 성공 시 로컬 데이터 삭제
                            window.location.href = '/market/completed/'+data.result; // 완료 후 페이지 이동
                        } else {
                            alert('주문 처리 중 오류가 발생했습니다.');
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('서버와의 통신 중 오류가 발생했습니다.');
                    });
            }
        }else {
            alert('주문할 상품이 없습니다.');
        }
    });


    window.addEventListener("beforeunload", (event) => {
        if (!sessionStorage.getItem("page_reload")) {
            localStorage.removeItem("productDataArray");
        }
    });

    sessionStorage.setItem("page_reload", "true");

    window.addEventListener("load", () => {
        sessionStorage.removeItem("page_reload");
    });
});
