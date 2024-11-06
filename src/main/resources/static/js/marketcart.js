document.addEventListener('DOMContentLoaded', function () {
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
                alert('수정되었습니다');
                toggleEditMode(row, false); // 수정 모드 해제


            })
            .catch(error => {
                console.log('error', error)
                alert('수량 오류')
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


        const cartItemIds = [];

        selectedItems.forEach(function (checkbox){
            const cartItemRow = checkbox.closest('tr');
            const cartItemId = cartItemRow.getAttribute('data-cart-item-id'); // 카트 아이템 ID 가져오기
            cartItemIds.push(cartItemId)
        })

        if (confirm(`정말로 삭제 하시겠습니까?`)) {
            const deletionPromises = cartItemIds.map(cartItemId => {
                return fetch(`/api/delete/${cartItemId}`, { // cartItemId를 URL에 추가
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



let initialQuantity = parseInt(document.querySelector('.orderQnt span').innerText.trim()) || 0;
let initialPrice = parseInt(document.querySelector('.orderOriginPrice .price').innerText.replace(/,/g, '')) || 0;
let initialDiscount = parseInt(document.querySelector('.orderSalePrice .price').innerText.replace(/,/g, '')) || 0;
let initialDelivery = parseInt(document.querySelector('.delivery-fee .price').innerText.replace(/,/g, '')) || 0;
let initialPoints = parseInt(document.querySelector('.orderPoint .price').innerText.replace(/,/g, '')) || 0;





document.addEventListener('DOMContentLoaded', function () {
    const checkBoxAll = document.getElementById('checkBoxAll');
    const itemCheckBoxes = document.querySelectorAll('input[name="select"]');

    // 전체 선택 체크박스 이벤트
    checkBoxAll.addEventListener('change', function () {
        itemCheckBoxes.forEach(function (checkbox) {
            checkbox.checked = checkBoxAll.checked;
        });
        if (!checkBoxAll.checked) {
            updateOrderSummary();
        }
    });

    // 개별 체크박스 변경 이벤트
    itemCheckBoxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            if (!checkbox.checked) {
                checkBoxAll.checked = false;
            } else if (Array.from(itemCheckBoxes).every(cb => cb.checked)) {
                checkBoxAll.checked = true;
            }
            updateOrderSummary();
        });
    });

// 주문 요약 업데이트 함수
    function updateOrderSummary() {
        let totalQuantity = 0;
        let totalPrice = 0;
        let totalDiscount = 0;
        let totalDelivery = 0;
        let totalPoints = 0;

        // 체크된 항목을 순회
        const checkedCheck = document.querySelectorAll('input[name="select"]:checked');
        if (checkedCheck.length > 0) {
            checkedCheck.forEach(checkbox => {
                const row = checkbox.closest('tr');

                // 수량, 가격, 할인, 배송비, 포인트 가져오기
                totalQuantity += parseInt(row.querySelector('input[name="quantity"]').value);
                totalPrice += parseInt(row.querySelector('td:nth-child(4)').innerText.replace(/,/g, ''));
                totalDiscount += parseInt(row.querySelector('td:nth-child(5)').innerText.replace(/,/g, ''));
                totalDelivery += parseInt(row.querySelector('td:nth-child(7)').innerText.replace(/,/g, ''));
                totalPoints += parseInt(row.querySelector('td:nth-child(6)').innerText.replace(/,/g, ''));
            });

            // 전체 주문 금액 계산
            const totalOrderPrice = totalPrice - totalDiscount + totalDelivery;

            // 화면에 값 업데이트
            document.querySelector('.orderQnt span').innerText = totalQuantity;
            document.querySelector('.orderOriginPrice .price').innerText = totalPrice.toLocaleString();
            document.querySelector('.orderSalePrice .price').innerText = totalDiscount.toLocaleString();
            document.querySelector('.delivery-fee .price').innerText = totalDelivery.toLocaleString();
            document.querySelector('.orderTotalPrice .price').innerText = totalOrderPrice.toLocaleString();
            document.querySelector('.orderPoint .price').innerText = totalPoints.toLocaleString();
        } else {
            // 체크박스가 하나도 체크되지 않은 경우 기본값으로 되돌리기
            document.querySelector('.orderQnt span').innerText = initialQuantity;
            document.querySelector('.orderOriginPrice .price').innerText = initialPrice.toLocaleString();
            document.querySelector('.orderSalePrice .price').innerText = initialDiscount.toLocaleString();
            document.querySelector('.delivery-fee .price').innerText = initialDelivery.toLocaleString();
            document.querySelector('.orderTotalPrice .price').innerText = (initialPrice - initialDiscount + initialDelivery).toLocaleString();
            document.querySelector('.orderPoint .price').innerText = initialPoints.toLocaleString();
        }
    }

    // 주문 버튼 이벤트
    document.querySelector('.order-Btn').addEventListener('click', function (event) {
        event.preventDefault(); // 기본 동작 방지
        console.log('주문 버튼 클릭');

        const userId = document.getElementById('uid').value;
        const selectedOptions = [];

        const checkedItems = document.querySelectorAll('input[name="select"]:checked');
        if (checkedItems.length === 0) {
            alert("주문할 상품을 선택해 주세요");
            return;
        }

        const isConfirmed = confirm("구매하시겠습니까?");
        if (isConfirmed) {

            const cartId = document.getElementById('cartId').value;
            checkedItems.forEach(checkbox => {
                const row = checkbox.closest('tr');
                const cartItemId = row.getAttribute('data-cart-item-id'); // 상품 ID
                const quantityInput = row.querySelector('input[name="quantity"]'); // 수량 input 요소
                const quantity = parseInt(quantityInput.value, 10); // 수량 값을 숫자로 변환

                // Add to selected options in the structure of CartItemDTO
                selectedOptions.push({ cartItemId, quantity });
            });

            // Create CartOrderRequestDTO structure
            const orderRequestData = {
                cartId: cartId, // Assuming all items belong to the same cart
                cartItemDTO: selectedOptions.map(option => ({
                    cartItemId: option.cartItemId,
                    quantity: option.quantity
                }))
            };

            console.log("Order Request Data:", orderRequestData); // 확인용 로그

            // Send API request
            fetch(`/api/cart/cartOrder/${userId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(orderRequestData)
            })
                .then(resp => {
                    console.log("Response status:", resp.status);
                    if (!resp.ok) {
                        throw new Error("Network response was not ok");
                    }
                    return resp.json();
                })
                .then(data => {
                    console.log("Response data:", data);
                    if (data.success) {
                        alert("구매가 완료되었습니다.");
                    } else {
                        alert("오류가 발생했습니다.");
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    alert("오류가 발생했습니다.");
                });
        }
    });
});


    // 추가적인 주문 처리 로직...

