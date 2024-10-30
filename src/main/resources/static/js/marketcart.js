document.addEventListener('DOMContentLoaded', function () {

   /* function isLoginUser(){
        const uid = document.getElementById("uid").value; // 값 가져오기
        return uid && uid.trim() !== ""; // uid 가 존재하고 비어 있지 않은지 확인
    }if(!isLoginUser()){
        alert('로그인이 필요합니다. 로그인 페이지로 날려버립니다.')
        window.location.href = '/user/login';
        return; // 더이상 실행 하지 않음
    }*/
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

    const aside = document.querySelector('aside');
    const headerHeight = 188; // 헤더 높이
    const footer = document.querySelector('footer');
    const footerHeight = 520; // 푸터 높이
    const asideHeight = aside.offsetHeight; // aside 높이
    console.log('asdieHeight:'+asideHeight)
    function handleAsideScroll() {
        const scrollPosition = window.scrollY; // 현재 스크롤 위치
        console.log(scrollPosition)
        const footerTop = footer.getBoundingClientRect().top + window.scrollY; // Footer의 상단 위치
        console.log('footerTop:'+footerTop);
        // 스크롤 위치가 헤더 아래에 있고, 푸터에 도달하기 전이면 aside에 scroll 클래스를 추가
        if (scrollPosition <= headerHeight || (scrollPosition + asideHeight + 50) < footerTop ) {
            aside.classList.remove('scroll');
            aside.style.position = 'fixed';
            aside.style.top = ''; // 헤더 아래에 고정
            aside.style.left = ''; // 푸터 닿지 않음
        } else if ((scrollPosition + asideHeight + 50) >= footerTop) {
            // 푸터에 도달하면 aside를 푸터 상단에서 멈추게 하기
            aside.classList.add('scroll');
            aside.style.position = 'absolute';
            aside.style.top = `${footerTop - asideHeight - 50}px`;  // 푸터 상단에 고정
            // console.log('asdide top : '+`${footerTop - asideHeight - 50}px`)
        } else {
            aside.classList.add('scroll');
            aside.style.position = ''; // 초기 위치로 돌아감
            aside.style.top = `${headerHeight}`; // 초기 위치로 돌아감

        }
    }

    // 스크롤할 때마다 실행
    window.addEventListener('scroll', handleAsideScroll);
    // 화면 크기가 변경될 때도 실행
    window.addEventListener('resize', handleAsideScroll);

});

document.addEventListener("DOMContentLoaded", function () {
    const checkBoxAll = document.getElementById('checkBoxAll');
    const itemCheckBoxes = document.querySelectorAll('input[name="select"]'); // 'checkBox' 대신 여러 체크박스를 선택

    checkBoxAll.addEventListener('change', function () {
        itemCheckBoxes.forEach(function (checkbox) {
            checkbox.checked = checkBoxAll.checked; // 전체 체크박스의 상태에 따라 모든 체크박스를 체크 또는 해제
        });
    });

    itemCheckBoxes.forEach(function (checkbox) {
        checkbox.addEventListener('change', function () {
            if (!checkbox.checked) {
                checkBoxAll.checked = false; // 하나의 체크박스가 해제되면 전체 체크박스도 해제
            } else if (Array.from(itemCheckBoxes).every(cb => cb.checked)) {
                checkBoxAll.checked = true; // 모든 체크박스가 체크되면 전체 체크박스도 체크
            }
        });
    });

    const selectAllButton = document.querySelector('.selected-all');
    selectAllButton.addEventListener('click', function (e){
        e.preventDefault();
        checkBoxAll.checked = !checkBoxAll.checked; //현재 상태 반전
        itemCheckBoxes.forEach(function (checkbox){
            checkbox.checked = checkBoxAll.checked;
        });
    });
});

// function updateCartSummary(cartItems) {
//     const totalQuantity = cartItems.reduce((total, item) => total + item.quantity, 0);
//     const totalPrice = cartItems.reduce((total, item) => total + item.price * item.quantity, 0 );
//     const totalDiscount = cartItems.reduce((total, item) => total + item.discount * item.quantity, 0);
//     const deliveryFee = 0;
//     const totalOrderPrice = totalPrice - totalDiscount + deliveryFee;
//     const totalPoints = Math.floor(totalPrice * 0.01);
//
//     // ui 업데이트
//     document.querySelector('.orderQnt span').innerText = totalQuantity;
//     document.querySelector('.orderOriginPrice  .price').innerText = totalPrice;
//     document.querySelector('.orderSalePrice .price').innerText = totalDiscount;
//     document.querySelector('.delivery-fee .price').innerText = deliveryFee;
//     document.querySelector('.orderTotalPrice .price').innerText = totalOrderPrice;
//     document.querySelector('.orderPoint .price').innerText = totalPoints;
// }
const cartItems = [];


// 수정 && 삭줴하기 붜틘


// 버튼 요소 선택
const modifyButtons = document.querySelectorAll('.countModifyBtn');
const cancelButtons = document.querySelectorAll('.cancel-btn');
const applyButtons = document.querySelectorAll('.apply-btn');

// 수정 모드 토글 함수
function toggleEditMode(row, isEditMode) {
    const inputField = row.querySelector('input[name="quantity"]');
    const numberControls = row.querySelector('.number-controls');
    const editButtons = row.querySelector('.edit-buttons');
    const modifyButton = row.querySelector('.countModifyBtn');

    // 수정 모드로 전환할 때 요소 표시 여부 조정
    inputField.style.display = isEditMode ? 'none' : 'block';
    numberControls.style.display = isEditMode ? 'flex' : 'none';
    editButtons.style.display = isEditMode ? 'flex' : 'none';
    modifyButton.classList.toggle('hidden', isEditMode);

    // 수정 모드라면 기존 값 복사
    if (isEditMode) {
        row.querySelector('.quantity-input').value = inputField.value;
    }
}

// 수정하기 버튼 이벤트 핸들러
modifyButtons.forEach(button => {
    button.addEventListener('click', function (e) {
        e.preventDefault();
        const row = this.closest('tr');
        toggleEditMode(row, true);
    });
});

// 취소 버튼 이벤트 핸들러
cancelButtons.forEach(button => {
    button.addEventListener('click', function () {
        const row = this.closest('tr');
        toggleEditMode(row, false);
    });
});

// 변경하기 버튼 이벤트 핸들러
applyButtons.forEach(button => {
    button.addEventListener('click', function () {
        const row = this.closest('tr');
        const inputField = row.querySelector('input[name="quantity"]');
        const newQuantity = row.querySelector('.quantity-input').value.trim(); // 공백 제거

        const cartItemId = row.dataset.cartItemId;
        if (!cartItemId) {
            console.log("아이디가 읍댜")
            return;
        }

        // 수량 검증
        if (isNaN(newQuantity) || newQuantity < 1) {
            alert('수량은 1 이상이여야 합니다.')
            return;
        }

        const requestBody = {
            cartItemId: parseInt(cartItemId, 10), // 숫자로 변환
            quantity: parseInt(newQuantity, 10)  // 숫자로 변환
        };
        console.log("전송할 requestBody:", requestBody);

        fetch(`/api/cart/${cartItemId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody),

        })

            .then(resp => {
                if (!resp.ok) {
                    alert('서버 요청중 요류발생')
                }
                return resp;
            })
            .then(data => {
                inputField.value = newQuantity;
                toggleEditMode(row, false); // 수정 모드 해제
                window.location.reload();


            })
            .catch(error => {
                console.log('error', error)
                alert('수량 업뎃중 오류닭')
            })
    })

});

// + - 버튼 클릭 시 숫자 조정 함수
function adjustQuantity(button, delta) {
    const inputField = delta < 0 ? button.nextElementSibling : button.previousElementSibling;
    let currentValue = parseInt(inputField.value) || 1;
    inputField.value = Math.max(1, currentValue + delta); // 최소값 1로 설정
}

// - 버튼 이벤트 핸들러
document.querySelectorAll('.qnt-decrease').forEach(button => {
    button.addEventListener('click', function () {
        adjustQuantity(this, -1);
    });
});

// + 버튼 이벤트 핸들러
document.querySelectorAll('.qnt-increase').forEach(button => {
    button.addEventListener('click', function () {
        adjustQuantity(this, 1);
    });
});


// 선택삭제 버튼 이벤트
document.querySelector('.selected-delete').addEventListener('click', function() {
    // 선택된 항목 삭제 로직
    const selectedItems = document.querySelectorAll('input[name="select"]:checked');

    if (selectedItems.length === 0) {
        console.log('삭제할 항목이 없다.')
        return
    }


        console.log('삭제할 아이템 ID:', cartItemId);
        const cartItemIds = [];
        selectedItems.forEach(function (checkbox){
            const cartItemRow = checkbox.closest('tr');
            const cartItemId = cartItemRow.getAttribute('data-cart-item-id'); // 카트 아이템 ID 가져오기
            cartItemIds.push(cartItemId)
        })

        if (confirm(`정말로 ${cartItemIds.length}개의 항목을 삭제 할기가?`)) {
            const deletionPromises = cartItemIds.map(cartItemId => {
                return fetch(`/api/delete`, {
                    method: `DELETE`
                })
                    .then(resp => {
                        if (resp.ok) {
                            console.log(`아이템 ${cartItemId} 삭제 성공`);
                            // 삭제 후 해당 행 제거
                            const cartItemRow = document.querySelector(`tr[data-cart-item-id="${cartItemId}"]`);
                            if (cartItemRow) {
                                cartItemRow.remove();
                            }
                        } else {
                            console.log(`아이템 ${cartItemId} 삭제 실패`);
                        }
                    })
                    .catch(error => {
                        console.log('오류 발새ㅑㅇ', error)
                    });

            });
                Promise.all(deletionPromises).then(() => {
                    console.log('모든 삭제 요청 완료');
                    window.location.reload(); // 페이지 새로고침
                });
        } else {
            console.log('삭제 취소');
        }
});

// 선택주문 버튼 이벤트
document.querySelector('.selected-order').addEventListener('click', function() {
    // 선택된 항목 주문 로직
    console.log('선택주문 버튼 클릭');
    // 여기에 선택된 항목을 주문하는 코드 작성
});

// 전체선택 버튼 이벤트
document.querySelector('.selected-all').addEventListener('click', function() {
    // 모든 항목 선택 로직
    console.log('전체선택 버튼 클릭');
    // 여기에 모든 항목을 선택하는 코드 작성
});
