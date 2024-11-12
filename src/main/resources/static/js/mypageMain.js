document.addEventListener('DOMContentLoaded', function() {

    // // 재귀적으로 카테고리 계층 구조를 DOM에 추가하는 함수
    // function buildCategoryTree(container, categories) {
    //     categories.forEach(category => {
    //         const li = document.createElement('li');
    //         li.textContent = category.name;
    //
    //         // 하위 카테고리가 있을 경우, 하위 목록을 생성하고 추가
    //         if (category.children && category.children.length > 0) {
    //             const subUl = document.createElement('ul');
    //             buildCategoryTree(subUl, category.children);
    //             li.appendChild(subUl);
    //         }
    //
    //         container.appendChild(li);
    //     });
    // }
    //
    //
    //
    //
    // document.addEventListener('click', function (event) {
    //     console.log("여기 되는거 맞아????")
    //     if (event.target.tagName === 'LI' && event.target.querySelector('ul')) {
    //         event.target.classList.toggle('expanded');
    //     }
    // });


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
        document.getElementById("orderItemId").textContent = orderElement.getAttribute("data-order-item-id");

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


// 수취확인 모달 관련 코드
    const qreceiptbtn = document.querySelectorAll(".receipt-btn");
    const receiptModal = document.getElementById("receiptModal");
    const closeModalBtn4 = document.querySelector(".modal.receipt .close.receipt");
    const confirmReceiptBtn = document.getElementById("confirmReceiptBtn");
    const cancelModalBtn = document.getElementById("cancelModalBtn");

// 주문 아이템 ID를 저장할 변수
    let currentOrderItemId = null;

// 수취확인 버튼 클릭 시 모달 열기
    qreceiptbtn.forEach(order => {
        order.addEventListener("click", function(e) {
            e.preventDefault();
            currentOrderItemId = order.getAttribute("data-order-item-id");  // 해당 주문 아이템 ID 저장
            receiptModal.style.display = "block"; // 모달 열기
        });
    });

// 모달 닫기 (닫기 버튼 클릭)
    closeModalBtn4.addEventListener("click", function() {
        receiptModal.style.display = "none"; // 모달 닫기
    });

// 모달 닫기 (취소 버튼 클릭)
    cancelModalBtn.addEventListener("click", function() {
        receiptModal.style.display = "none"; // 모달 닫기
    });

// 모달 외부 클릭 시 닫기
    window.onclick = function(event) {
        if (event.target == receiptModal) {
            receiptModal.style.display = "none"; // 모달 닫기
        }
    };

// 수취 확인 버튼 클릭 시 상태 변경
    confirmReceiptBtn.addEventListener("click", function() {
        if (currentOrderItemId !== null) {
            // 서버에 상태 변경 요청
            fetch(`/mypage/confirmReceipt/${currentOrderItemId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ status: "CONFIRMATION" })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        // 성공적으로 상태가 변경되면 페이지 업데이트 (수취확인 상태 표시)
                        const orderItemStatus = document.querySelector(`[data-order-item-id="${currentOrderItemId}"]`)
                            .closest('tr')
                            .querySelector('[th\\:text="${orderItem.status.description}"]');
                        orderItemStatus.textContent = "CONFIRMATION"; // 상태 텍스트 업데이트

                        // 모달 닫기
                        receiptModal.style.display = "none";

                        // 페이지 새로고침
                        location.reload();
                    } else {
                        alert("수취 확인에 실패했습니다.");
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                });
        }
    });

    //pReview-btn
    const pReviewbtn = document.querySelectorAll(".pReview-btn");
    const pReviewModel = document.getElementById("pReviewModal");
    const closeModalBtn5 = document.querySelector(".modal.pReview .close.pReview");

    document.querySelectorAll('.pReview-btn').forEach(button => {
        button.onclick = function() {
            const productId = this.dataset.productNo; // 상품 ID 가져오기
            const productName = this.dataset.productName; // 상품 이름 가져오기
            const orderItemId = this.dataset.orderItemId;
            console.log("여기야" +productName );
            console.log("여기야" +orderItemId );
            console.log("여기야" +productId );


            // 모달에 상품 이름과 ID 설정
            document.getElementById("modalProductName").textContent = productName;
            document.getElementById("modalOrderItemId").value = orderItemId;
            document.getElementById("modalProductId").value = productId; // hidden 필드에 상품 ID 설정

            // 모달 표시
            document.getElementById("pReviewModal").style.display = "block";
        };
    });

    document.getElementById("submitReviewBtn").onclick = function() {
        console.log("Submitted Rating:", document.getElementById('rating').value);
        const productId = document.getElementById("modalProductId").value;
        const orderItemId = document.getElementById("modalOrderItemId").value;
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
        formData.append("orderItemId",orderItemId)

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



});


// 하위 메뉴 보이기 함수
function showSubmenu(element) {
    const submenu = element.querySelector('ul');
    console.log('submenu : '+submenu);
    if (submenu) {
        submenu.style.display = 'block';
        adjustHeightAndPosition();
    }
}

// 하위 메뉴 숨기기 함수
function hideSubmenu(element) {
    const submenu = element.querySelector('ul');
    if (submenu) submenu.style.display = 'none';
}

// 1depth의 총 높이에 맞춰 2depth, 3depth 높이 및 위치 설정 함수
function adjustHeightAndPosition() {
    const menu1depth = document.getElementById('menu-1depth');
    const menu2depths = document.querySelectorAll('.menu-2depth');
    const menu3depths = document.querySelectorAll('.menu-3depth');

    // 1depth의 전체 높이를 가져와서 하위 메뉴에 적용
    const height = menu1depth.offsetHeight;

    // 2depth와 3depth의 높이를 1depth의 높이에 맞춤
    menu2depths.forEach(menu2 => {
        menu2.style.height = `${height}px`;
    });

    menu3depths.forEach(menu3 => {
        const parentMenu2 = menu3.closest('.menu-2depth');
        const firstMenu2Item = parentMenu2.querySelector('li');

        // 2depth의 첫 번째 항목과 동일한 위치에 3depth 배치
        if (firstMenu2Item) {
            const offsetTop = firstMenu2Item.offsetTop;
            menu3.style.top = `${offsetTop}px`;
        }
        menu3.style.height = `${height}px`;
    });
}