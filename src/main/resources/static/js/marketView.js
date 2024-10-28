
document.addEventListener("DOMContentLoaded", function () {
    // 모든 장바구니 버튼에 클릭 이벤트 추가
    const addToCartBtn = document.querySelectorAll('.add-to-cart');
    console.log('클릭됨')

    addToCartBtn.forEach(btn => {
        btn.addEventListener('click', function (){

        // 상품 정보 수집
            const productId = this.dataset.productId; // 버튼에 data-product 속성 추가
            const quantity = document.querySelector(`#quantity`).value; // 수정된 부분
            console.log("Product ID:", productId);
            if(quantity <= 0){
                alert('수량을 1 이상으로 설정해 주세요.');
                return
            }

        // 서버에 장바구니 추가 요금 보내기
        fetch('/api/cart',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({productId, quantity}), // JSON 형식으로 전송
        })
            .then(resp => {
                if (resp.status === 401) {
                    // 사용자 인증이 안 된 경우 로그인 페이지로
                    alert('로그인 없이 이곳은 접근 금지! 빨리 로그인해 주세요');
                    window.location.href = '/user/login';
                    return; // 추가적인 처리 중단
                }
                if(!resp.ok){
                    throw new Error('궁시렁궁시렁 리스폰이 안됨');

                }
                return resp.json();
            })
            .then(data => {
                // 성공적으로 장바구니에 추가되었을때
                alert('장바구니에 추가 되었습니다!!!!!!!!!!!!!!');

            })
            .catch(error => {
                console.error('궁시렁궁시렁 추가하는데 실패함ㅋ');
                alert('장바구니에 추가하는 데 실패하심');

            });
        });
    });
});