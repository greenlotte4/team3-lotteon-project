document.addEventListener('DOMContentLoaded', function () {
    // Script for toggling the category menu
    // const viewAllButton = document.getElementById('viewAllButton');
    // const categoryMenu = document.querySelector('.categoryMenu');
    // const header = document.querySelector('.category.on');

    // viewAllButton.addEventListener('click', function () {
    //     // Toggle the visibility of the menu and the button bar transformation
    //     header.classList.toggle('menuVisible');
    // });


    const viewAllButton = document.getElementById('viewAllButton');
    const categoryMenu = document.querySelector('.categoryMenu');
    const header = document.querySelector('.category.on');

    viewAllButton.addEventListener('click', function () {
        // Toggle the visibility of the menu with slide down/up animation
        header.classList.toggle('menuVisible');

        // If the menu is visible, expand the max-height to show content
        if (header.classList.contains('menuVisible')) {
            categoryMenu.style.maxHeight = categoryMenu.scrollHeight + 'px';
        } else {
            categoryMenu.style.maxHeight = '0'; // Collapse the menu
        }
    });

    const viewdecrease = document.getElementById('decrease');
    const viewincrease = document.getElementById('increase');
    if(viewdecrease != null){
    viewdecrease.addEventListener('click', function () {
            let quantity = document.getElementById('quantity');
            if (quantity.value > 1) {
                quantity.value = parseInt(quantity.value) - 1;
            }
        });

        viewincrease.addEventListener('click', function () {
            let quantity = document.getElementById('quantity');
            quantity.value = parseInt(quantity.value) + 1;
        });
    }

    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            document.querySelectorAll('h3 span').forEach(el => el.classList.remove('active'));
            this.parentElement.classList.add('active');
        });
    });

    const reviews = document.querySelectorAll('.p-review');
    const qnas = document.querySelectorAll('.p-qna');
    const moreqnaBtn = document.getElementById('moreQnaBtn');
    const moreReviewsBtn = document.getElementById('moreReviewsBtn');
    const pagination = document.getElementById('reviews-pagination');
    const pagination2 = document.getElementById('qna-pagination');
    let isExpanded = false;  // 더보기 상태를 저장하는 변수

    // 리뷰 더보기 버튼 클릭 시
    if(moreReviewsBtn !=null){
        moreReviewsBtn.addEventListener('click', function (e) {
            e.preventDefault();
            if (!isExpanded) {
                // 더보기를 눌렀을 때: 3개 이상의 리뷰 표시 + 페이지네이션 활성화
                isExpanded = true;
                reviews.forEach((review, index) => {
                    if (index >= 3) {
                        review.classList.add('show');
                        pagination.classList.add('show');
                    }
                });
                moreReviewsBtn.textContent = '더보기 닫기';  // 더보기 버튼 텍스트 변경
            } else {
                // 더보기 닫기를 눌렀을 때: 첫 3개의 리뷰만 표시 + 페이지네이션 숨김
                isExpanded = false;
                reviews.forEach((review, index) => {
                    review.classList.remove('show');
                });
                pagination.classList.remove('show');
                moreReviewsBtn.textContent = '더보기';  // 더보기 버튼 텍스트 복구
            }
        });
    }
    

    if(moreqnaBtn !=null){
        // qna 더보기 버튼 클릭 시
        moreqnaBtn.addEventListener('click', function (e) {
            e.preventDefault();
            if (!isExpanded) {
                // 더보기를 눌렀을 때: 3개 이상의 qna 표시 + 페이지네이션 활성화
                isExpanded = true;
                qnas.forEach((qna, index) => {
                    qna.classList.add('show');
                    pagination2.classList.add('show');
                });
                moreqnaBtn.textContent = '더보기 닫기';  // 더보기 버튼 텍스트 변경
            } else {
                // 더보기 닫기를 눌렀을 때: qna 숨김 + 페이지네이션 숨김
                isExpanded = false;
                qnas.forEach((qna, index) => {
                    qna.classList.remove('show');
                });
                pagination2.classList.remove('show');
                moreqnaBtn.textContent = '더보기';  // 더보기 버튼 텍스트 복구
            }
        });
    }

    

    // Image carousel functionality
    const reviewImages = document.getElementById('reviewImages');
    const leftArrow = document.getElementById('leftArrow');
    const rightArrow = document.getElementById('rightArrow');
   

    if(reviewImages !=null){

        let currentPosition = 0;
        const imageWidth = 150; // Width of each image (including margins)
        const visibleImages = 3; // Number of images to show at once
        const maxPosition = reviewImages.children.length - visibleImages; // Maximum scroll position

        function updateImagePosition() {
            reviewImages.style.transform = `translateX(${-currentPosition * imageWidth}px)`;
        }
    
        // Move left
        leftArrow.addEventListener('click', function () {
            if (currentPosition > 0) {
                currentPosition--;
                updateImagePosition();
            }
        });
    
        // Move right
        rightArrow.addEventListener('click', function () {
            if (currentPosition < maxPosition) {
                currentPosition++;
                updateImagePosition();
            }
        });

    }
   

    // Submenu functionality for lnb-items
    const lnbItems = document.querySelectorAll('.lnb-item > a');

    lnbItems.forEach(function (lnbItem) {
        lnbItem.addEventListener('click', function (e) {
            e.preventDefault(); // Prevent default anchor behavior

            // Remove 'open' and 'active' from all lnb-items
            lnbItems.forEach(item => {
                const parent = item.parentElement;
                const subMenu = parent.querySelector('.lnb-sub-menu');

                // Close the sub-menu of any other lnb-items
                if (parent !== this.parentElement) {
                    parent.classList.remove('open');
                    if (subMenu) {
                        subMenu.classList.add('hidden');
                    }
                }

                // Remove active class from other links
                item.classList.remove('active');

                // Remove active from all li inside sub-menus
                if (subMenu) {
                    subMenu.querySelectorAll('li').forEach(li => {
                        li.classList.remove('active');
                    });
                }
            });

            // Toggle the current item
            const parentItem = this.parentElement;
            const subMenu = parentItem.querySelector('.lnb-sub-menu');
            parentItem.classList.toggle('open');
            if (subMenu) {
                subMenu.classList.toggle('hidden');

                // Add 'active' class to the clicked sub-menu's li items (if desired)
                subMenu.querySelectorAll('li').forEach(li => {
                    li.addEventListener('click', function () {
                        // Remove 'active' from all other li in the sub-menu
                        subMenu.querySelectorAll('li').forEach(el => el.classList.remove('active'));
                        // Add 'active' to the clicked li
                        this.classList.add('active');
                    });
                });
            }
            // Add active class to the clicked link
            this.classList.add('active');
        });
    });



    const modifyButtons = document.querySelectorAll('.countModifyBtn');
    const cancelButtons = document.querySelectorAll('.cancel-btn');
    const applyButtons = document.querySelectorAll('.apply-btn');
    const currentValue=null;
    if(modifyButtons != null){
        // 수정하기 버튼 클릭 시
        modifyButtons.forEach((button, index) => {
            button.addEventListener('click', function (e) {
                e.preventDefault();

                // 해당 행의 수정하기 관련 요소들 표시
                const row = this.closest('tr');
                const inputField = row.querySelector('input[name="quantity"]');
                const numberControls = row.querySelector('.number-controls');
                const editButtons = row.querySelector('.edit-buttons');
                console.log(button);

                // 수정 모드로 전환
                inputField.style.display = 'none';
                numberControls.style.display = 'flex';
                editButtons.style.display = 'flex';
                button.classList.add('hidden');

                // 기존 값 반영
                currentValue = inputField.value;
                row.querySelector('.quantity-input').value = currentValue;
            });
        });

        // 취소 버튼 클릭 시
        cancelButtons.forEach((button) => {
            button.addEventListener('click', function () {
                const row = this.closest('tr');
                const inputField = row.querySelector('input[name="quantity"]');
                const numberControls = row.querySelector('.number-controls');
                const editButtons = row.querySelector('.edit-buttons');

                // 수정 모드에서 빠져나오기
                inputField.style.display = 'block';
                numberControls.style.display = 'none';
                editButtons.style.display = 'none';
                 // hidden 클래스 제거
                const modifyButton = row.querySelector('.countModifyBtn');
                modifyButton.classList.remove('hidden');

            });
        });

        // 변경하기 버튼 클릭 시
        applyButtons.forEach((button) => {
            button.addEventListener('click', function () {
                const row = this.closest('tr');
                const inputField = row.querySelector('input[name="quantity"]');
                const numberControls = row.querySelector('.number-controls');
                const editButtons = row.querySelector('.edit-buttons');

                // 새로운 값 적용
                const newValue = row.querySelector('.quantity-input').value;
                inputField.value = newValue;


                 // Fetch를 이용해 서버로 POST 요청
                // fetch('/updateQuantity', {
                //     method: 'POST',
                //     headers: {
                //         'Content-Type': 'application/json'
                //     },
                //     body: JSON.stringify({
                //         productId: productId,
                //         quantity: updatedQuantity
                //     })
                // })
                // .then(response => response.json())
                // .then(data => {
                //     if (data.success) {
                //         // 성공 시 처리할 로직
                //         console.log('수량이 성공적으로 업데이트되었습니다.');
                //     } else {
                //         // 실패 시 처리할 로직
                //         console.error('수량 업데이트에 실패했습니다.');
                //     }
                // })
                // .catch(error => {
                //     console.error('에러 발생:', error);
                // });

                // 수정 모드에서 빠져나오기
                inputField.style.display = 'block';
                numberControls.style.display = 'none';
                editButtons.style.display = 'none';
                 // hidden 클래스 제거
                const modifyButton = row.querySelector('.countModifyBtn');
                modifyButton.classList.remove('hidden');

            });
        });

        // + - 버튼 클릭 시 숫자 조정
        document.querySelectorAll('.qnt-decrease').forEach((button) => {
            button.addEventListener('click', function () {
                const inputField = this.nextElementSibling;
                let currentValue = parseInt(inputField.value);
                if (currentValue > 1) {
                    inputField.value = currentValue - 1;
                }
            });
        });

        document.querySelectorAll('.qnt-increase').forEach((button) => {
            button.addEventListener('click', function () {
                const inputField = this.previousElementSibling;
                let currentValue = parseInt(inputField.value);
                inputField.value = currentValue + 1;
            });
        });
    }
    


});
