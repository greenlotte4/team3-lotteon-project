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

    orderNumbers.forEach(order => {
        order.addEventListener("click", function(e) {
            e.preventDefault();
            modal.style.display = "block"; // Show the modal
        });
    });

    // Close modal when clicking the close button
    closeModalBtn.addEventListener("click", function() {
        modal.style.display = "none"; // Hide the modal
    });

    // Close modal when clicking outside the modal content
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none"; // Hide the modal
        }
    };

    //판매자정보 모달창
    const sellerModal = document.getElementById("sellerModal");
    const sellerNumbers = document.querySelectorAll(".seller-number");
    const closeModalBtn2 = document.querySelector(".modal.seller .close");

    sellerNumbers.forEach(order => {
        order.addEventListener("click", function(e) {
            e.preventDefault();
            sellerModal.style.display = "block"; // Show the modal
        });
    });

    // Close modal when clicking the close button
    closeModalBtn2.addEventListener("click", function() {
        sellerModal.style.display = "none"; // Hide the modal
    });

    // Close modal when clicking outside the modal content
    window.onclick = function(event) {
        if (event.target == sellerModal) {
            sellerModal.style.display = "none"; // Hide the modal
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