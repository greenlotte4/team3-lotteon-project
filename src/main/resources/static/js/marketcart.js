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


// 수정 && 삭줴하기 붜틘



