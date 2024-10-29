
// 전역 변수로 선택된 옵션 값을 초기화
let selectedOptionValue = "";
let selectedOptionText = "";
const productId = document.getElementById("productId").value;
const point = document.getElementById("point").value;
const productName = document.getElementById("productName").value;
const originalPrice = document.getElementById("originalPrice").innerText;
const finalPrice = document.getElementById("finalPrice").innerText;
const file190 = document.getElementById("file190").value;
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




    document.getElementById("buy-now-btn").addEventListener("click", function(e) {
        alert("여기1!!");
        if(!selectedOptionValue) {
            alert("옵션을 선택해주세요.")
            return;
        }



        console.log("Quantity:", quantity);
        const isConfirmed = confirm("구매하시겠습니까?");
        if (isConfirmed) {
            // JSON 객체 생성
            const productData = {
                productId: productId,         // 실제 값 추가
                productName: productName,     // 실제 값 추가
                originalPrice: originalPrice, // 실제 값 추가
                finalPrice: finalPrice,       // 실제 값 추가
                quantity: quantity,            // 실제 값 추가
                file190: file190,
                optionId :selectedOptionValue,
                optionName : selectedOptionText,
                point : point,
                discount : discount
            };

            // `localStorage`에 데이터 저장
            localStorage.setItem("productData", JSON.stringify(productData));

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
                        // 구매 성공 시 주문 페이지로 리다이렉트
                        window.location.href = "/market/order";
                    } else if(data.result === "login_required") {
                        const isconfirm= confirm("로그인이 필요합니다. 로그인 하시겠습니까?");
                        if(isconfirm) {
                            window.location.href = "/user/login";
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
