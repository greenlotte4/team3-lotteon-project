document.addEventListener('DOMContentLoaded', function () {
    sessionStorage.setItem("page_reload", "true");

    // Format prices with thousands separator for all elements with class 'price'
    const priceElements = document.querySelectorAll('.price');
    priceElements.forEach(priceElement => {
        let priceValue = priceElement.textContent.trim().replace(/[^0-9]/g, '');
        priceValue = parseInt(priceValue, 10);
        if (!isNaN(priceValue)) {
            priceElement.textContent = priceValue.toLocaleString();
        }
    });



    // Elements for final order calculations

    let orderItem= {};
    let totalDiscount = 0;
    let totalDiscountPandC=0;
    let orderTotal=0;
    let couponDiscount = 0;
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
    const memberGrade = document.getElementById("memberGrade").value;
    const memberName=document.getElementById('memberName').value;
    const memberHp = document.getElementById('memberHp').value;
    // Retrieve the elements
    const postcodeElement = document.getElementById("M_postcode");
    const addressElement = document.querySelector(".totalAddress");

    //수령자
    const receiver = document.getElementById('receiver').value;
    console.log(receiver);

    //핸드폰
    const hp = document.getElementById('hp').value;
    console.log(hp);


    const gradePercentages = {
        "VVIP": 5,
        "VIP": 4,
        "GOLD": 3,
        "SILVER": 2,
        "FAMILY": 1
    };

    const pointPercentage = gradePercentages[memberGrade] || 0; // Default to 0 if grade not found
    console.log(`Grade: ${memberGrade}, Point Percentage: ${pointPercentage}%`);


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

        if (data.options && data.options.length > 0) {
            const totalAdditionalPrice = data.options[0].additionalPrice;
            const calcPrice= data.originalPrice+ totalAdditionalPrice;
            return `
        <tr class="order-row">
            <td>
                <div><img src="/uploads/${data.file190}" alt="${data.productName}"></div>
                <div>
                    <span>${data.productName}</span>
                    <p class="product_option"> [ 옵션 : ${data.options[0].combinationString} ]</p>
                </div>
            </td>
            <td>
                <div class="qnt">
                    <input type="number" class="T_quantity" value="${data.quantity}" readonly>
                </div>
            </td>
            <td><span class="T_originalPrice price" data-original="${calcPrice}" data-additional="${calcPrice}">${calcPrice.toLocaleString()}</span></td>
            <td><span class="T_discount">${data.discount}</span>%</td>
            <td><span class="T_point">${data.calcPrice * pointPercentage}</span></td>
            <td><span class="T_shippingFee" data-ship="${shippingFee}">${shippingFee.toLocaleString()}</span></td>
            <td><span class="T_finalPrice price">${(data.finalPrice * data.quantity).toLocaleString()}</span></td>
            <td><input type="hidden" class="shippingTerms" value="${data.shippingTerms}"></td>
        </tr>
    `;
        }else{

            return `
        <tr class="order-row">
            <td>
                <div><img src="/uploads/productImg/${data.file190}" alt="${data.productName}"></div>
                <div>
                    <span>${data.productName}</span>

                </div>
            </td>
            <td>
                <div class="qnt">
                    <input type="number" class="T_quantity" value="${data.quantity}" readonly>
                </div>
            </td>
            <td><span class="T_originalPrice price" data-original="${data.originalPrice}">${(data.originalPrice).toLocaleString()}</span></td>
            <td><span class="T_discount">${data.discount}</span>%</td>
            <td><span class="T_point">${data.finalPrice * pointPercentage}</span></td>
            <td><span class="T_shippingFee" data-ship="${shippingFee}">${shippingFee.toLocaleString()}</span></td>
            <td><span class="T_finalPrice price">${(data.finalPrice * data.quantity).toLocaleString()}</span></td>
            <td><input type="hidden" class="shippingTerms" value="${data.shippingTerms}"></td>
        </tr>
    `;
        }

    }


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
        console.log("쿠폰!!:",selectedCouponValue);


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
        let pointsEarned=0;
        if(couponDiscount === 0){
            pointsEarned =  Math.floor((( orderTotal -totalShippingFee())* pointPercentage) / 100);

        }
        console.log(pointsEarned);
        finalOrderPoint.textContent = pointsEarned.toLocaleString(); // Display with thousands separator
        finalOrderTotal.textContent = orderTotal.toLocaleString();
        // finalOrderPoint.textContent = Math.floor(orderTotal * 0.01).toLocaleString();
    }


    //기존꺼
    // function totalProductDiscount() {
    //     return Array.from(document.querySelectorAll('.T_discount')).reduce((total, elem, index) => {
    //         const originalPrice = parseInt(document.querySelectorAll('.T_originalPrice')[index].dataset.original || 0);
    //         const quantity = document.querySelectorAll('.T_quantity')[index].value || 0;
    //         const discountPercentage = parseInt(elem.innerText || 0);
    //
    //         return total + Math.floor((originalPrice * discountPercentage) / 100) * quantity;
    //     }, 0);
    // }

    function totalProductDiscount() {
        return Array.from(document.querySelectorAll('.T_discount')).reduce((total, elem, index) => {
            const originalPrice = parseInt(document.querySelectorAll('.T_originalPrice')[index].dataset.original || 0);
            const quantity = parseInt(document.querySelectorAll('.T_quantity')[index].value || 1);
            const discountPercentage = parseInt(elem.innerText || 0);

            // Calculate discount based on the original price, quantity, and discount percentage
            return total + Math.floor((originalPrice * discountPercentage) / 100) * quantity;
        }, 0);
    }

    calculateTotals();

    // Calculate the total values based on all selected options
    function totalQuantity() {
        return Array.from(document.querySelectorAll('.T_quantity'))
            .reduce((total, elem) => total + parseInt(elem.value || 0), 0);
    }

    //기존꺼
    // function totalProductPrice() {
    //     return Array.from(document.querySelectorAll('.T_originalPrice'))
    //         .reduce((total, elem) => {
    //             const originalPrice = parseInt(elem.dataset.original || 0);
    //             const additionalPrice = parseInt(elem.dataset.additional || 0); // Assuming additionalPrice is stored here
    //             const quantity = parseInt(elem.closest('.order-row').querySelector('.T_quantity').value || 1); // Get the quantity
    //             return total + (additionalPrice) * quantity;
    //         }, 0);
    // }
    // Function to calculate total product price
    function totalProductPrice() {
        return Array.from(document.querySelectorAll('.T_originalPrice'))
            .reduce((total, elem) => {
                const originalPrice = parseInt(elem.dataset.original || 0);
                const additionalPrice = elem.dataset.additional ? parseInt(elem.dataset.additional) : 0; // Check if additional price exists
                const quantity = parseInt(elem.closest('.order-row').querySelector('.T_quantity').value || 1); // Get the quantity

                // Sum total based on original price and any additional price if options exist
                return total + (originalPrice + additionalPrice) * quantity;
            }, 0);
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
        console.log(totalProductPrice);

        finalOrderDiscount.textContent = (totalDiscountAmount + totalDiscount).toLocaleString();
        console.log((totalDiscountAmount + totalDiscount));

        finalOrderDeliveryFee.textContent = totalShippingFee.toLocaleString();


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
            usedCouponResult: Number(couponDiscount),         // usedCoupon -> usedCouponResult로 매핑
            usedCouponName:couponText,
            totalOrderQuantity: finalOrderQuantity.textContent,
            totalOriginalPrice:finalOrderProductPrice.textContent,
            totalShippingFee: finalOrderDeliveryFee.textContent,                           // 기본값 설정 필요
            totalFinalPrice: finalOrderTotal.textContent,                            // 기본값 설정 필요
            credit: selectedPayment,                       // 일치
            couponId: Number(couponValue),            // couponValue를 couponId로 매핑 (확인 필요)
            shippingInfo: getSelectedShippingInfo(),
            finalOrderPoint: finalOrderPoint.textContent,
            gradePercentage: pointPercentage,
            memberName:memberName,
            memberHp:memberHp
        };
    }
    console.log("Product Data Array Before Order Submission:", productDataArray);


    const orderBtn = document.querySelector('.order-Btn');
    const receiverInput = document.getElementById('receiver');
    const hpInput = document.getElementById('hp');
// Function to validate all required fields
    function validateOrderForm() {
        const address = addressElement.getAttribute("data-addr") || '';
        const shippingInfo = getSelectedShippingInfo();
        const paymentSelected = Array.from(paymentOptions).some(option => option.checked);
        const receiver = receiverInput.value.trim();
        const hp = hpInput.value.trim();
        const postcode = postcodeElement.getAttribute("data-postcode") || '';

        // Check if any required fields are empty
        if (!address || !shippingInfo || !paymentSelected || !receiver || !hp || !postcode) {
            return false;
        }

        return true;
    }



    updateOrderItem();
    console.log(orderItem);
    document.querySelector('.order-Btn').addEventListener('click', function (event) {
        event.preventDefault();  // 기본 제출 동작 방지

        if (productDataArray.length > 0 && validateOrderForm()) {
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
                            alert("모든 필수 항목을 채워주세요: 수령자 정보, 주소, 배송 요청사항, 결제수단");

                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('서버와의 통신 중 오류가 발생했습니다.');
                    });
            }
        }else if(!validateOrderForm()){
            alert('주문정보를 확인해주세요');
        } else {
            alert('주문할 상품이 없습니다.');
        }
    });



});


