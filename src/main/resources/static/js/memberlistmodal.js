



// 모달 열고 닫는 기능 구현
document.addEventListener("DOMContentLoaded", function() {
    var memberListModal = document.getElementById("memberlistModal");
    var openMemberListBtns = document.getElementsByClassName("openModalBtn2");
    var closeMemberListSpan = memberListModal.getElementsByClassName("close")[0];

    // 버튼을 클릭하면 회원 목록 모달을 엽니다.
    for (let btn of openMemberListBtns) {
        btn.onclick = function(event) {
            event.preventDefault(); // 링크 기본 동작 방지
            memberListModal.style.display = "block"; // 모달 열기
        }
    }

    // 닫기 버튼을 클릭하면 회원 목록 모달을 닫습니다.
    if (closeMemberListSpan) {
        closeMemberListSpan.onclick = function() {
            memberListModal.style.display = "none"; // 모달 닫기
        }
    }

    // 모달 외부를 클릭하면 회원 목록 모달을 닫습니다.
    window.onclick = function(event) {
        if (event.target == memberListModal) {
            memberListModal.style.display = "none"; // 모달 닫기
        }
    }

    // 전화번호 잘림 적용
    const phoneElement = document.getElementById('phone');
    const hiddenPhone = hidePhoneNumber(phoneElement.textContent);
    phoneElement.textContent = hiddenPhone;
});



