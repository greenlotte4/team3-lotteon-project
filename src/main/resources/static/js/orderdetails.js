function formatDate(date) {
    let year = date.getFullYear();
    let month = (date.getMonth() + 1).toString().padStart(2, '0');
    let day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}

// 오늘 날짜를 max 속성으로 설정
function setMaxDate() {
    const today = new Date();
    document.getElementById("startDate").setAttribute("max", formatDate(today));
    document.getElementById("endDate").setAttribute("max", formatDate(today));
}

// 선택한 기간만큼 시작일을 계산하는 함수
function setPeriod(days) {
    const today = new Date(); // 오늘 날짜
    const startDate = new Date(); // 시작일을 오늘 날짜로 설정
    startDate.setDate(today.getDate() - days); // 선택된 기간만큼 이전 날짜 설정

    // 시작일과 종료일을 input 필드에 설정
    document.getElementById("startDate").value = formatDate(startDate);
    document.getElementById("endDate").value = formatDate(today);
}

// 폼을 제출할 때 처리하는 함수
function submitForm() {
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;

    // 날짜 검증 (시작일이 종료일보다 이후일 수 없도록 설정)
    if (new Date(startDate) > new Date(endDate)) {
        alert("시작일이 종료일보다 클 수 없습니다.");
        return;
    }

    // 폼 데이터 처리 (여기서는 간단히 콘솔에 출력)
    console.log("선택된 기간:", startDate, "~", endDate);

}

// 페이지 로드 시 max 날짜 설정
window.onload = setMaxDate;

const modal = document.getElementById("orderModal");
const closeModalBtn = document.querySelector(".modal .close");
const orderNumbers = document.querySelectorAll(".order-number");

function openOrderModal(orderElement) {
    // 데이터 속성 값들을 모달에 채우기
    document.getElementById("orderDate").textContent = orderElement.getAttribute("data-order-date");
    document.getElementById("orderIdText").textContent = `주문번호: ${orderElement.getAttribute("data-order-id")}`;
    document.getElementById("companyName").textContent = orderElement.getAttribute("data-company");
    document.getElementById("productName").textContent = orderElement.getAttribute("data-product-name");
    document.getElementById("quantityText").textContent = `수량: ${orderElement.getAttribute("data-quantity")}`;
    document.getElementById("productPrice").textContent = orderElement.getAttribute("data-price");
    document.getElementById("salePrice").textContent = orderElement.getAttribute("data-price"); // 판매가
    document.getElementById("discountPrice").textContent = orderElement.getAttribute("data-discount"); // 할인액
    document.getElementById("totalPrice").textContent = orderElement.getAttribute("data-total-price"); // 결제금액
    document.getElementById("orderStatus").textContent = orderElement.getAttribute("data-status");
    document.getElementById("customerName").textContent = orderElement.getAttribute("data-customer-name");
    document.getElementById("customerPhone").textContent = orderElement.getAttribute("data-customer-phone");
    document.getElementById("customerAddress").textContent = orderElement.getAttribute("data-customer-address");
    document.getElementById("deliveryRequests").textContent = orderElement.getAttribute("data-delivery-requests");

    // 이미지 경로 업데이트
    const imagePath = orderElement.getAttribute("data-product-image");
    document.getElementById("productImage").src = `/uploads/productImg/${imagePath}`; // 이미지 경로 반영

    modal.style.display = "block"; // Show the modal
}

// 모달 닫기 버튼 클릭시
closeModalBtn.addEventListener("click", function() {
    modal.style.display = "none"; // 모달 숨기기
});

// 모달 외부를 클릭하면 모달 닫기
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none"; // 모달 숨기기
    }
};

// 각 주문 항목에 클릭 이벤트 추가
orderNumbers.forEach(order => {
    order.addEventListener("click", function(e) {
        e.preventDefault();
        openOrderModal(order); // openOrderModal 함수 호출
    });
});

//recipit 모달창
const qreceiptbtn = document.querySelectorAll(".receipt-btn");
const receiptModel = document.getElementById("receiptModal");
const closeModalBtn4 = document.querySelector(".modal.receipt .close.receipt");
console.log(closeModalBtn4);
qreceiptbtn.forEach(order => {
    order.addEventListener("click", function(e) {
        e.preventDefault();
        receiptModel.style.display = "block"; // Show the modal

    });
});

// Close modal when clicking the close button
closeModalBtn4.addEventListener("click", function() {
    receiptModel.style.display = "none"; // Hide the modal
});

// Close modal when clicking outside the modal content
window.onclick = function(event) {
    if (event.target == receiptModel) {
        receiptModel.style.display = "none"; // Hide the modal
    }
};

// 판매자 정보 모달창
const sellerModal = document.getElementById("sellerModal");
const sellerNumbers = document.querySelectorAll(".seller-number");
const closeModalBtn2 = document.querySelector(".modal.seller .close");

// 각 판매자 정보를 클릭할 때 모달에 데이터를 반영하고 열기
sellerNumbers.forEach(order => {
    order.addEventListener("click", function(e) {
        e.preventDefault();

        // 클릭한 요소의 data- 속성에서 각 데이터 가져오기
        const company = order.getAttribute("data-company");
        const ceo = order.getAttribute("data-ceo");
        const phone = order.getAttribute("data-phone");
        const fax = order.getAttribute("data-fax");
        const email = order.getAttribute("data-email");
        const bno = order.getAttribute("data-bno");
        const address = order.getAttribute("data-address");

        // 모달 내용에 해당 데이터 반영
        document.querySelector("#sellerModal td[data-field='company']").textContent = company;
        document.querySelector("#sellerModal td[data-field='ceo']").textContent = ceo;
        document.querySelector("#sellerModal td[data-field='phone']").textContent = phone;
        document.querySelector("#sellerModal td[data-field='fax']").textContent = fax;
        document.querySelector("#sellerModal td[data-field='email']").textContent = email;
        document.querySelector("#sellerModal td[data-field='bno']").textContent = bno;
        document.querySelector("#sellerModal td[data-field='address']").textContent = address;

        // 모달 보이기
        sellerModal.style.display = "block";
    });
});

// 닫기 버튼 클릭 시 모달 닫기
closeModalBtn2.addEventListener("click", function() {
    sellerModal.style.display = "none";
});

// 모달 외부 클릭 시 모달 닫기
window.onclick = function(event) {
    if (event.target == sellerModal) {
        sellerModal.style.display = "none";
    }
};

//pReview-btn
const pReviewbtn = document.querySelectorAll(".pReview-btn");
const pReviewModel = document.getElementById("pReviewModal");
const closeModalBtn5 = document.querySelector(".modal.pReview .close.pReview");

document.querySelectorAll('.pReview-btn').forEach(button => {
    button.onclick = function() {
        const productId = this.dataset.productNo; // 상품 ID 가져오기
        const productName = this.dataset.productName; // 상품 이름 가져오기

        // 모달에 상품 이름과 ID 설정
        document.getElementById("modalProductName").textContent = productName;
        document.getElementById("modalProductId").value = productId; // hidden 필드에 상품 ID 설정

        // 모달 표시
        document.getElementById("pReviewModal").style.display = "block";
    };
});

document.getElementById("submitReviewBtn").onclick = function() {
    console.log("Submitted Rating:", document.getElementById('rating').value);
    const productId = document.getElementById("modalProductId").value;
    const rating = document.getElementById('rating').value;
    if (!rating) {
        alert("만족도를 선택해주세요.");
        return;
    }
    const content = document.getElementById("contents").value;

    // FormData 객체 생성
    const formData = new FormData();
    formData.append("productId", productId);
    formData.append("rating", rating);
    formData.append("content", content);

    // 모든 파일 입력 요소에서 파일을 가져오기
    const fileInputs = document.querySelectorAll('.file-input');
    fileInputs.forEach(input => {
        if (input.files.length > 0) {
            for (let i = 0; i < input.files.length; i++) {
                formData.append("pReviewFiles", input.files[i]);
            }
        }
    });

    fetch("/mypage/myInfo/review", {
        method: "POST",
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("상품평이 등록되었습니다!");
                pReviewModal.style.display = "none";
                document.getElementById("contents").value = "";
                document.getElementById("rating").value = "";
                document.querySelectorAll('.star').forEach(s => {
                    s.classList.remove('selected');
                });
            } else {
                alert("상품평 등록에 실패했습니다.");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("상품평 등록 중 오류가 발생했습니다.");
        });
};
console.log(closeModalBtn4);
pReviewbtn.forEach(order => {
    order.addEventListener("click", function(e) {
        e.preventDefault();
        pReviewModel.style.display = "block"; // Show the modal

    });
});

// Close modal when clicking the close button
closeModalBtn5.addEventListener("click", function() {
    pReviewModel.style.display = "none"; // Hide the modal
});

// Close modal when clicking outside the modal content
window.onclick = function(event) {
    if (event.target == pReviewModel) {
        pReviewModel.style.display = "none"; // Hide the modal
    }
};

const stars = document.querySelectorAll('.star');

// 별점을 클릭했을 때의 이벤트 설정
stars.forEach(star => {
    star.addEventListener('click', function() {
        const ratingValue = this.getAttribute('data-value');

        // 선택한 별점에 맞춰 배경색 및 선택 상태를 업데이트합니다.
        stars.forEach(s => {
            if (s.getAttribute('data-value') <= ratingValue) {
                s.classList.add('selected'); // 선택된 별점
            } else {
                s.classList.remove('selected'); // 선택되지 않은 별점
            }
        });

        // rating hidden input에 값을 설정합니다.
        document.getElementById('rating').value = ratingValue; // 선택된 별점 값을 저장
    });
});

// 파일 추가 시 새로운 input[type="file"] 동적 추가
const fileContainer = document.getElementById('fileContainer');

fileContainer.addEventListener('change', function (event) {
    const target = event.target;
    if (target.classList.contains('file-input') && target.files.length > 0) {
        // 새로운 파일 input 추가
        const newFileInput = document.createElement('input');
        newFileInput.type = 'file';
        newFileInput.name = 'pReviewFiles'; // 동일한 이름 사용
        newFileInput.classList.add('file-input');
        newFileInput.multiple = true; // 여러 파일 선택 가능하도록 설정
        fileContainer.appendChild(newFileInput);
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


//판매자정보- 문의하기 모달창
const qnabtn = document.querySelectorAll(".qna-btn");
const qnaModel = document.getElementById("qnaModal");
const closeModalBtn3 = document.querySelector(".modal.qna .close");

qnabtn.forEach(order => {
    order.addEventListener("click", function(e) {
        e.preventDefault();
        qnaModel.style.display = "block"; // Show the modal
        sellerModal.style.display="none"
    });
});

// Close modal when clicking the close button
closeModalBtn3.addEventListener("click", function() {
    qnaModel.style.display = "none"; // Hide the modal
});

// Close modal when clicking outside the modal content
window.onclick = function(event) {
    if (event.target == qnaModel) {
        qnaModel.style.display = "none"; // Hide the modal
    }
};
