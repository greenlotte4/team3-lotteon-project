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
    const footerHeight = 440;
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
    try {
        const productData = JSON.parse(localStorage.getItem("productData"));
        if (!productData) {
            document.getElementById("buynow").style.display = "none";
        } else {
            populateProductDetails(productData);
        }
    } catch (error) {
        document.getElementById("buynow").style.display = "none";
    }

    // Populate product details in the document
    function populateProductDetails(data) {
        document.getElementById("productId").value = data.productId;
        document.getElementById("productName").innerText = data.productName;
        document.getElementById("originalPrice").innerText = data.originalPrice * data.quantity;
        document.getElementById("originalPrice").dataset.original = data.originalPrice;
        document.getElementById("finalPrice").innerText = data.finalPrice;
        document.getElementById("optionName").innerText = data.optionName;
        document.getElementById("shippingFee").innerText = "4000";
        document.getElementById("shippingFee").dataset.ship = "4000";
        document.getElementById("optionId").value = data.optionId;
        document.getElementById("quantity").value = data.quantity;
        document.getElementById("productImage").src = `/uploads/productimg/${data.file190}`;
        document.getElementById("productImage").alt = data.productName;
        document.getElementById("discount").innerText = data.discount;
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

    let productPrice = parseInt(document.getElementById("originalPrice").innerText);
    let deliveryFee = parseInt(document.getElementById("shippingFee").innerText) || 0;
    let productQuantity = parseInt(document.getElementById("quantity").value) || 1;

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
            couponDiscount = Math.floor(productPrice * 0.03); // Example: 3% discount
        }

        // Calculate initial order total before points
        const initialTotalOrderAmount = (productPrice * productQuantity) - couponDiscount + deliveryFee;

        // If used points exceed the total order amount, limit them
        const limitedUsedPoint = Math.min(usedPoint, initialTotalOrderAmount);

        // Display the limited used points if they were capped
        usedPointInput.value = limitedUsedPoint;
        currentIn.textContent = currentPoint - limitedUsedPoint; // Update displayed remaining points

        // Update discount and final order information
        usedPointResult.textContent = limitedUsedPoint;
        usedCouponResult.textContent = couponDiscount;

        const totalDiscount = limitedUsedPoint + couponDiscount;
        discountResult.textContent = totalDiscount;

        // Final order calculations
        finalOrderQuantity.textContent = productQuantity;
        finalOrderProductPrice.textContent = productPrice * productQuantity;
        finalOrderDiscount.textContent = totalDiscount;
        finalOrderDeliveryFee.textContent = deliveryFee;

        // Calculate final total after all discounts
        const orderTotal = initialTotalOrderAmount - limitedUsedPoint;
        finalOrderTotal.textContent = orderTotal;

        // Calculate and display estimated points (example: 1% of the final price)
        finalOrderPoint.textContent = Math.floor(orderTotal * 0.01);
    }

    // Calculate total values for the final order
    calculateTotals();

    function calculateTotals() {
        const totalQuantity = Array.from(document.querySelectorAll('.T_quantity'))
            .reduce((total, elem) => total + parseInt(elem.value || 0), 0);

        const totalOriginalPrice = Array.from(document.querySelectorAll('.T_originalPrice'))
            .reduce((total, elem) => total + parseInt(elem.dataset.original || 0), 0);

        const totalShipping = Array.from(document.querySelectorAll('.T_shippingFee'))
            .reduce((total, elem) => total + parseInt(elem.dataset.ship || 0), 0);

        const totalDiscountAmount = Array.from(document.querySelectorAll('.T_discount'))
            .reduce((total, elem, index) => {
                const originalPrice = parseInt(document.querySelectorAll('.T_originalPrice')[index].dataset.original || 0);
                const discountPercentage = parseInt(elem.innerText || 0);
                return total + Math.floor((originalPrice * discountPercentage) / 100);
            }, 0);

        finalOrderQuantity.textContent = totalQuantity;
        finalOrderProductPrice.textContent = totalOriginalPrice;
        finalOrderDiscount.textContent = totalDiscountAmount + parseInt(discountResult.textContent) || 0;
        finalOrderDeliveryFee.textContent = totalShipping;
        finalOrderPoint.textContent = totalQuantity * 10; // Example: points per quantity
    }


});


// Set a flag in sessionStorage on page load
window.addEventListener("load", () => {
    sessionStorage.setItem("page_reload", "true");
});

// Detect unload event
window.addEventListener("beforeunload", (event) => {
    // Check if "page_reload" flag is set; if not, it's a true navigation away from the page
    if (sessionStorage.getItem("page_reload") === "true") {
        // Remove the flag to indicate a refresh
        sessionStorage.removeItem("page_reload");
    } else {
        // On true navigation, remove productData
        localStorage.removeItem("productData");
    }
});
