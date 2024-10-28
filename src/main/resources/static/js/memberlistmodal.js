// 모달 열고 닫는 기능 구현
document.addEventListener("DOMContentLoaded", function () {
    var memberListModal = document.getElementById("memberlistModal");
    var openMemberListBtns = document.getElementsByClassName("openModalBtn2");
    var closeMemberListSpan = memberListModal.getElementsByClassName("close")[0];

    const uidInput = modal.querySelector("#uid");
    const passwordInput = modal.querySelector("#password");
    const genderInputs = modal.querySelectorAll("input[name='gender']"); // 성별은 여러 개의 input이므로 NodeList로 가져옵니다.
    const gradeInput = modal.querySelector("#status"); // 등급은 readonly이므로 #status로 가져옵니다.
    const emailInput = modal.querySelector("#EMAIL");
    const hpInput = modal.querySelector("#HP");
    const zipcodeInput = modal.querySelector("#zipcode");
    const address1Input = modal.querySelector("#address1");
    const address2Input = modal.querySelector("#address2");

    openModalBtns.forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault(); // 기본 링크 동작 방지
            const userId = this.getAttribute("data-id"); // data-id에서 사용자 ID 가져오기

            // AJAX 호출로 사용자 정보 가져오기
            fetch(`/api/users/${userId}`) // 실제 사용자 정보를 가져오는 API 경로로 변경
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Network response was not ok");
                    }
                    return response.json();
                })
                .then(data => {
                    // 모달에 사용자 정보 채우기
                    uidInput.value = data.uid; // 사용자 ID
                    passwordInput.value = data.name; // 이름
                    emailInput.value = data.email; // 이메일
                    hpInput.value = data.hp; // 휴대폰
                    zipcodeInput.value = data.zipcode; // 우편번호
                    address1Input.value = data.address1; // 주소1
                    address2Input.value = data.address2; // 주소2

                    // 성별 체크박스 처리
                    genderInputs.forEach(genderInput => {
                        genderInput.checked = (genderInput.value === data.gender);
                    });

                    modal.style.display = "block"; // 모달 표시
                })
                .catch(error => console.error("Error fetching user data:", error));
        });
    });

    // 닫기 버튼을 클릭하면 회원 목록 모달을 닫습니다.
    if (closeMemberListSpan) {
        closeMemberListSpan.onclick = function () {
            memberListModal.style.display = "none"; // 모달 닫기
        }
    }

    // 모달 외부를 클릭하면 회원 목록 모달을 닫습니다.
    window.onclick = function (event) {
        if (event.target == memberListModal) {
            memberListModal.style.display = "none"; // 모달 닫기
        }
    }

    // 전화번호 잘림 적용
    const phoneElement = document.getElementById('phone');
    const hiddenPhone = hidePhoneNumber(phoneElement.textContent);
    phoneElement.textContent = hiddenPhone;
});



