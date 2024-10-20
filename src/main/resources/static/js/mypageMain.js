document.addEventListener('DOMContentLoaded', function() {


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

    const stars = document.querySelectorAll('.star-rating .star');

    // 별점 클릭 이벤트
    stars.forEach(star => {
        star.addEventListener('click', function () {
            const ratingValue = parseInt(this.getAttribute('data-value'));

            // 선택된 별까지 모두 gold로 변경
            stars.forEach((s, index) => {
                if (index < ratingValue) {
                    s.classList.add('selected');
                } else {
                    s.classList.remove('selected');
                }
            });
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
            newFileInput.name = 'pReviewFiles';
            newFileInput.classList.add('file-input');
            fileContainer.appendChild(newFileInput);
        }
    });


});