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


