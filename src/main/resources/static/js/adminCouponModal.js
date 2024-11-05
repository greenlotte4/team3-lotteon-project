document.addEventListener("DOMContentLoaded", function () {
    var modal = document.getElementById("couponModal");
    var closeBtn = document.querySelector(".close"); // X 버튼
    var closeFooterBtn = document.querySelector(".modal-close-btn"); // 하단 '닫기' 버튼
    var orderLinks = document.querySelectorAll(".order-link");

    // 링크 클릭 시 모달 열기 및 데이터 로드
    orderLinks.forEach(function (link) {
        link.addEventListener("click", function (event) {
            event.preventDefault();

            var couponNumber = this.getAttribute("data-coupon-id");
            var issuer = this.getAttribute("data-issuer");
            var couponType = this.getAttribute("data-type");
            var couponName = this.getAttribute("data-name");
            var benefit = this.getAttribute("data-benefit");
            var period = this.getAttribute("data-period");
            var notes = this.getAttribute("data-notes");
            console.log(period)
            console.log()
            // 모달에 데이터 채우기
            document.getElementById("modalCouponNumber").innerText = couponNumber;
            document.getElementById("modalIssuer").innerText = issuer;
            document.getElementById("modalCouponType").innerText = couponType;
            document.getElementById("modalCouponName").innerText = couponName;
            document.getElementById("modalBenefit").innerText = benefit;
            document.getElementById("modalPeriod").innerText = period;
            document.getElementById("modalNotes").innerText = notes;



            // 모달 보이기
            modal.style.display = "block";
        });
    });

    // 모달 X 버튼 클릭 시 닫기
    closeBtn.onclick = function () {
        modal.style.display = "none";
    };

    // 모달 하단 '닫기' 버튼 클릭 시 닫기
    closeFooterBtn.onclick = function () {
        modal.style.display = "none";
    };

    // 모달 외부 클릭 시 닫기
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    };
});


document.addEventListener('DOMContentLoaded', () => {
    // 쿠폰 등록 모달 관련 요소
    const addCouponBtn = document.querySelector('.add-coupon-btn');
    const addCouponModal = document.getElementById('addCouponModal');
    const closeBtn = addCouponModal.querySelector('.close');
    const cancelBtn = document.getElementById('cancelBtn');
    const submitBtn = document.getElementById('submitBtn');
    const productSelect = document.getElementById("productSelect"); // 상품 선택 요소

    // 쿠폰 등록 모달 열기
    addCouponBtn.addEventListener('click', () => {
        addCouponModal.style.display = 'block';
        loadProducts(); // 상품 불러오기 호출

    });

    // 모달 닫기 함수
    function closeModal() {
        addCouponModal.style.display = 'none';
    }

    // 모달 닫기 (x 아이콘 클릭 또는 취소 버튼 클릭 시)
    closeBtn.addEventListener('click', closeModal);
    cancelBtn.addEventListener('click', closeModal);
    // 등록 버튼 클릭 시 폼 제출 처리
    submitBtn.addEventListener('click', (event) => {
        event.preventDefault();

        const benefit = document.querySelector('input[name="benefit"]:checked');
        const startDate = document.getElementById("startDate").value;
        const endDate = document.getElementById("endDate").value;

        const inpitcouponName = couponForm.couponName.value;
        const inpitcouponType = couponForm.couponType.value;
        const inpitbenefit = couponForm.benefit.value;
        const inpitstartDate = couponForm.startDate.value;
        const inpitendDate = couponForm.endDate.value;
        const inpitnotes = couponForm.notes.value;
        // 상품 아이디 들고오기
        const selectedProductId = productSelect.value; // 여기가 중요합니다.
        console.log(selectedProductId,"상품아이디")
        // 모든 필드가 입력되었는지 확인
        if (!inpitcouponName || !inpitcouponType || !inpitbenefit || !inpitstartDate || !inpitendDate || !inpitnotes) {
            alert("모든 필드를 입력해 주세요.");
            return; // 폼 제출 방지
        }
        // 쿠폰 타입이 '개별상품할인'일 때 상품 선택 유효성 검사
        if (inpitcouponType === "discount" && !selectedProductId) {
            alert("상품을 선택해 주세요.");
            return; // 폼 제출 방지
        }
        if (startDate > endDate) {
            alert("사용 기간이 잘못 설정 되었습니다.");
            return; // 폼 제출 방지
        }

        const sellerCompany = document.getElementById("addCouponModal").getAttribute("data-seller-company");
        console.log("Seller Company:", sellerCompany);

        const couponData = {
            couponId: 0,
            couponName: document.getElementById("couponName").value,
            couponType: document.getElementById("couponType").value,
            benefit: benefit ? benefit.value : '',
            startDate: startDate,
            endDate: endDate,
            notes: document.getElementById("notes").value,
            rdate: new Date().toISOString().split('T')[0], // 현재 날짜
            productId: selectedProductId, // 선택된 상품 ID 추가
            sellerDTO: {
                // 필요한 SellerDTO 필드 추가
                company   : sellerCompany
            }
        };
        console.log("Selected Product ID:", selectedProductId);

        console.log("쿠폰 데이터 전송:", JSON.stringify(couponData)); // JSON 데이터 확인

        fetch('/seller/coupon/register', {
            method: 'POST',
            headers:{
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(couponData)

        })
            .then(resp => {
                if (resp.ok){
                    alert("쿠폰이 등록되었습니다.")
                    closeModal();
                    document.getElementById("couponForm").reset();
                    window.location.href = '/seller/coupon/list'; // 리다이렉트

                }else {
                    return resp.json().then(errorData => {
                        alert(`등록에 실패했습니다: ${errorData.message || "서버 오류"}.`);
                    });
                }
            })
            .catch(error => console.error("Error------------" + error))
    });

    function loadProducts(){
        const addCouponModal = document.getElementById("addCouponModal");
        const sellerCompany = addCouponModal.getAttribute("data-seller-company");

        console.log("Seller Company:", sellerCompany);


        fetch(`/seller/coupon/products`)
            .then(resp => {
                if (!resp.ok) {
                    throw new Error("상품을 불러오는 데 실패했습니다."); // 400 오류가 발생했을 때 처리
                }
                return resp.json();
            })
            .then(products => {
                console.log("불러온 상품 데이터:", products); // 전체 제품 데이터 로그

                productSelect.innerHTML = '<option value="" disabled selected>쿠폰 적용상품</option>'; // 기본 옵션 추가

                if (products.length === 0) {
                    alert("등록된 상품이 없습니다."); // 상품이 없을 때 알림
                    return; // 더 이상 진행하지 않음
                }

                products.forEach(product => {
                    console.log("상품 ID:", product.productId, "상품명:", product.productName); // 로그 추가
                    const option = document.createElement('option');
                    option.value = product.productId; // 상품 ID로 설정
                    option.textContent = product.productName; // 상품명
                    productSelect.appendChild(option);
                });
            })
            .catch(err => {
                console.error(err);
                alert("오류가 발생했습니다: " + err.message);
        });
    }
    // 모달 외부 클릭 시 모달 닫기
    window.addEventListener('click', (event) => {
        if (event.target === addCouponModal) {
            closeModal();
        }
    });
});



// 모달 엘리먼트와 닫기 버튼
document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById('couponInfoModal');
    const closeModal = modal.querySelector('.close'); // X 아이콘
    const modalCloseBtn = modal.querySelector('.modal-close-btn'); // 닫기 버튼

    // 쿠폰 정보 업데이트 함수
    function updateModal(couponData) {
        document.getElementById('modalCouponNumber').textContent = couponData.couponNumber;
        document.getElementById('modalIssueNumber').textContent = couponData.issueNumber;
        document.getElementById('modalCouponType').textContent = couponData.couponType;
        document.getElementById('modalIssuer').textContent = couponData.issuer;
        document.getElementById('modalUsageStatus').textContent = couponData.usageStatus;
        document.getElementById('modalTarget').textContent = couponData.target;
        document.getElementById('modalCouponName').textContent = couponData.couponName;
        document.getElementById('modalBenefit').textContent = couponData.benefit;
        document.getElementById('modalPeriod').textContent = couponData.period;
        document.getElementById('modalNotes').textContent = couponData.notes;
    }

    // 발급번호 클릭 이벤트
    document.querySelectorAll('.order-link').forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();

            const couponData = {
                couponNumber: this.dataset.couponNumber,
                issueNumber: this.textContent, // 발급번호
                couponType: this.dataset.couponType,
                issuer: this.dataset.issuer,
                usageStatus: this.dataset.usageStatus,
                target: this.dataset.target,
                couponName: this.dataset.couponName,
                benefit: this.dataset.benefit,
                period: this.dataset.period,
                notes: this.dataset.notes,
            };

            updateModal(couponData);
            modal.style.display = 'block';
        });
    });

    // 모달 닫기 이벤트 (X 아이콘 클릭)
    closeModal.onclick = function () {
        modal.style.display = 'none';
    };

    // 모달 닫기 이벤트 (취소 버튼 클릭)
    modalCloseBtn.onclick = function () {
        modal.style.display = 'none';
    };

    // 모달 외부 클릭 시 닫기
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    };
});

// 쿠폰 종료 버튼 처리
document.querySelectorAll('.end-button').forEach(button => {
    button.addEventListener('click', function () {
        if (confirm("정말로 이 쿠폰을 종료하시겠습니까?")) {
            const couponId = this.closest('tr').querySelector('.order-link').getAttribute('data-coupon-id');
            const couponRow = this.closest('tr');
            const statusElement = couponRow.querySelector('.coupon-status');
            const currentStatus = statusElement.innerText.trim(); // 상태 가져오기
            console.log("쿠폰 ID:", couponId); // 확인용 로그

            fetch(`/seller/coupon/${couponId}/end`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' }
            })
                .then(resp => {
                    if (!resp.ok) throw new Error("Response not OK");
                    return resp.json();
                })
                .then(updatedCoupon => {
                    this.disabled = true; // 버튼 비활성화
                    statusElement.innerText = updatedCoupon.status; // 상태 업데이트
                    localStorage.setItem(`coupon-${couponId}`, JSON.stringify(updatedCoupon.status));
                    window.location.href = '/seller/coupon/list'; // 리다이렉트
                })
                .catch(error => console.error('Error:', error));
        }
    });
    document.querySelectorAll('.coupon-status').forEach(statusElement => {
        const couponRow = statusElement.closest('tr');
        const button = couponRow.querySelector('.end-button');
        const currentStatus = statusElement.innerText;

        if (currentStatus !== '발급 중') {
            button.style.backgroundColor = '#888'; // 회색 배경
            button.style.color = 'white'; // 흰색 글자
            button.disabled = true; // 상태에 따라 버튼 비활성화
        }
    });
});



