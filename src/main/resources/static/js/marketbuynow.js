
// 전역 변수로 선택된 옵션 값을 초기화
let selectedOptionValue = "";
let selectedOptionText = "";
document.getElementById("option").addEventListener("change", function() {
    selectedOptionValue = this.value; // 선택된 옵션의 값 (ID)
    selectedOptionText = this.options[this.selectedIndex].text; // 선택된 옵션의 텍스트

    console.log("선택된 옵션 ID:", selectedOptionValue);
    console.log("선택된 옵션 설명:", selectedOptionText);
});
console.log("2323선택된 옵션 ID:", selectedOptionValue);
console.log("23233선택된 옵션 설명:", selectedOptionText);
const productId = document.getElementById("productId").value;
const point = document.getElementById("point").value;
const productName = document.getElementById("productName").value;
const originalPrice = document.getElementById("originalPrice").innerText;
const finalPrice = document.getElementById("finalPrice").innerText;
const file190 = document.getElementById("file190").value;
const discount = document.getElementById("discount").value;


console.log("Product ID:", productId);
console.log("Original Price:", originalPrice);
console.log("Final Price:", finalPrice);

document.getElementById("buy-now-btn").addEventListener("click", function(e) {
    alert("여기1!!");
    if(!selectedOptionValue) {
        alert("옵션을 선택해주세요.")
        return;
    }


    const quantity = document.getElementById("quantity").value;
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
