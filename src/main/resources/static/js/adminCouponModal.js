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

    // 쿠폰 등록 모달 열기
    addCouponBtn.addEventListener('click', () => {
        addCouponModal.style.display = 'block';
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
            sellerDTO: {
                // 필요한 SellerDTO 필드 추가
                company   : sellerCompany
            }
        };

        console.log("쿠폰데이터:", couponData)

        fetch('/admin/coupon/register', {
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
                }else {
                    return resp.json().then(errorData => {
                        alert(`등록에 실패했습니다: ${errorData.message || "서버 오류"}.`);
                    });
                }
            })
            .catch(error => console.error("Error------------" + error))
    });

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


