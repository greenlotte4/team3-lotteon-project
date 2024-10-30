
// 전역 변수로 선택된 옵션 값을 초기화
let selectedOptionValue = "";
let selectedOptionText = "";

const productId = document.getElementById("productId").value;
const point = document.getElementById("point").value;
const productName = document.getElementById("productName").value;
const originalPrice = document.getElementById("originalPrice").innerText;
const finalPrice = document.getElementById("finalPrice").innerText;
const file190 = document.getElementById("file190").value;

const shippingFee = parseInt(document.getElementById("shippingFee").getAttribute("data-shippingfee")) || 0;
let quantity = parseInt(document.getElementById("quantity").value); // Default quantity

const byCart = document.getElementById('buyCart');
console.log(byCart);
console.log("shippingFee ",shippingFee)

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

const discount = document.getElementById("discount").value;
const quantity = document.getElementById("quantity").value;
const shippingFee =document.getElementById("shippingFee").value;


document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("option").addEventListener("change", function() {
        selectedOptionValue = this.value; // 선택된 옵션의 값 (ID)
        selectedOptionText = this.options[this.selectedIndex].text; // 선택된 옵션의 텍스트

        console.log("선택된 옵션 ID:", selectedOptionValue);
        console.log("선택된 옵션 설명:", selectedOptionText);
    });
    console.log("2323선택된 옵션 ID:", selectedOptionValue);
    console.log("23233선택된 옵션 설명:", selectedOptionText);


console.log("Product ID:", productId);
console.log("Original Price:", originalPrice);
console.log("Final Price:", finalPrice);

    document.getElementById("buy-now-btn").addEventListener("click", function(e) {
        alert("여기1!!");
        if(!selectedOptionValue) {
            alert("옵션을 선택해주세요.")
            return;
        }



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
        const orderInfo={
             shippingFee:shippingFee,
        }



            // JSON 형식으로 데이터 전송
            fetch("/market/buyNow", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(productData)
                })
                .then(response => response.json())
                .then(data => {
                    console.log(data); // 서버 응답을 확인

                if (data.result === "success") {
                    const uid = document.getElementById("uid").value;
                    console.log(uid);
                    // 구매 성공 시 주문 페이지로 리다이렉트
                    window.location.href = `/market/order/${uid}`;
                } else if(data.result === "login_required") {
                    const isconfirm= confirm("로그인이 필요합니다. 로그인 하시겠습니까?");
                    if(isconfirm) {
                        const currentUrl = encodeURIComponent(window.location.href);
                        console.log(currentUrl);
                        window.location.href = `/user/login?redirect=${currentUrl}`;
                    }else{
                        location.reload();
                    }
                }
                else if (data.result === "auth") {
                    // 권한 없는 사용자(관리자 또는 판매자)일 경우 경고 메시지 표시
                    alert("구매 권한이 없는 계정입니다. 관리자 또는 판매자는 구매할 수 없습니다.");
                } else if (data.result === "fail") {
                    // 계정이 없거나 기타 오류가 발생한 경우
                    alert("구매 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("구매 처리 중 오류가 발생했습니다.");
            });
    }
});




    // 모든 장바구니 버튼에 클릭 이벤트 추가
    const addToCartBtn = document.querySelectorAll('.add-to-cart');
    console.log('클릭됨');

    addToCartBtn.forEach(btn => {
        btn.addEventListener('click', function () {
            const user = localStorage.getItem('user');
            if (!user) {
                alert('로그인 후 사용 가능합니다. 로그인 페이지로 이동합니다.');
                window.location.href = '/user/login'; // 로그인 페이지로 이동
                return;
            }

            // 상품 정보 수집
            const quantity = document.querySelector(`#quantity`).value; // 수정된 부분
            if (quantity <= 0) {
                alert('수량을 1 이상으로 설정해 주세요.');
                return;
            } else if (!selectedOptionValue) {
                alert("옵션을 선택해 주세요");
                return;
            }
            console.log("Quantity:", quantity);

            const isConfirmed = confirm("장바구니에 추가 하시겠습니까.");
            if (isConfirmed) {
                // json 객체 생성
                const productCart = {
                    productId: parseInt(productId, 10), // 숫자로 변환
                    productName: productName,
                    originalPrice: parseInt(originalPrice, 10),
                    finalPrice: parseInt(finalPrice, 10),
                    quantity: parseInt(quantity, 10),
                    file190: file190,
                    shippingFee: shippingFee || 0,
                    optionId: parseInt(selectedOptionValue, 10),
                    optionName: selectedOptionText,
                    point: parseInt(point, 10),
                    discount: parseInt(discount, 10)
                };

                console.log('전송할 데이터:', JSON.stringify(productCart));
                localStorage.setItem("productCart", JSON.stringify(productCart));
                fetch('/api/cart', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(productCart), // JSON 형식으로 전송
                })
                    .then(resp => {
                        // 서버 응답을 JSON으로 변환
                        if (!resp.ok) {
                            throw new Error(`서버 응답 오류: ${resp.status} ${resp.statusText}`);
                        }
                        return resp.json(); // JSON으로 변환된 응답을 반환
                    })
                    .then(data => {
                        if (data.status === 200) {
                            alert('장바구니에 추가 되었습니다!');
                        } else if (data.status === 401) {
                            alert('로그인 없이 이곳은 접근 금지! 빨리 로그인해 주세요');
                            window.location.href = '/user/login';
                        } else if (data.status === 'fail') {
                            alert('장바구니에 추가하는 데 실패하셨습니다.');
                        } else {
                            throw new Error('알 수 없는 오류가 발생했습니다.');
                        }
                    })
                    .catch(error => {
                        console.error('장바구니에 추가하는 데 실패함:', error);
                        alert('장바구니에 추가하는 데 실패하셨습니다.');
                    });
            }
        });
    });
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
