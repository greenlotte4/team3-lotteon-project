// 모달 열고 닫는 기능 구현
document.addEventListener("DOMContentLoaded", function () {
    // var memberListModal = document.getElementById("memberlistModal");
    // var openMemberListBtns = document.getElementsByClassName("openModalBtn2");
    // var closeMemberListSpan = memberListModal.getElementsByClassName("close")[0];

    const modal = document.getElementById("memberlistModal"); // `modal` 변수로 변경
    const openModalBtns = document.getElementsByClassName("openModalBtn2");
    const closeModalBtn = modal.getElementsByClassName("close")[0];

    const uidInputDisplay = modal.querySelector("#uidDisplay");
    const uidInput = modal.querySelector("#uid");
    const nameInput = modal.querySelector("#name");
    const genderInputs = modal.querySelectorAll("input[name='gender']"); // 성별은 여러 개의 input이므로 NodeList로 가져옵니다.
    const gradeInput = modal.querySelector("#grade"); // 등급은 readonly이므로 #status로 가져옵니다.
    const emailInput = modal.querySelector("#EMAIL");
    const hpInput = modal.querySelector("#HP");
    const zipcodeInput = modal.querySelector("#zipcode");
    const address1Input = modal.querySelector("#address1");
    const address2Input = modal.querySelector("#address2");
    const regdateInput = modal.querySelector("#Joindate");
    const lastdateInput = modal.querySelector("#Lastlogindate");

    // openModalBtns.forEach(button => {
    Array.from(openModalBtns).forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();
            const userId = this.getAttribute("data-id");

            // 모달 열기 전에 입력 필드 초기화
            uidInputDisplay.value = '';
            nameInput.value = '';
            emailInput.value = '';
            hpInput.value = '';
            zipcodeInput.value = '';
            address1Input.value = '';
            address2Input.value = '';
            regdateInput.textContent = '';
            lastdateInput.textContent = '';

            // AJAX 호출로 사용자 정보 가져오기
            fetch(`/admin/user/${userId}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Network response was not ok");
                    }
                    return response.json();
                })
                .then(data => {
                    // 모달에 사용자 정보 채우기
                    console.log(data); // 데이터가 잘 받아졌는지 확인
                    uidInputDisplay.value = data.uid || ''; // 기본값으로 빈 문자열 설정
                    uidInput.value = data.uid|| ''; // 기본값으로 빈 문자열 설정
                    console.log(uidInput.value);
                    nameInput.value = data.name || ''; // 기본값으로 빈 문자열 설정
                    console.log(nameInput.value);
                    emailInput.value = data.email || ''; // 기본값으로 빈 문자열 설정
                    hpInput.value = data.hp || ''; // 기본값으로 빈 문자열 설정
                    zipcodeInput.value = data.postcode || ''; // 기본값으로 빈 문자열 설정
                    address1Input.value = data.addr || ''; // 기본값으로 빈 문자열 설정
                    address2Input.value = data.addr2 || ''; // 기본값으로 빈 문자열 설정
                    regdateInput.value = data.regDate || '';
                    lastdateInput.value = data.lastDate || '';

                    // 성별 체크박스 처리
                    genderInputs.forEach(genderInput => {
                        genderInput.checked = (genderInput.value === data.gender);

                    });

                    modal.style.display = "block"; // 모달 표시
                })
                .catch(error => console.error("Error fetching user data:", error));
        });
    });

    // 닫기 버튼 클릭 시 모달 닫기
    closeModalBtn.onclick = function () {
        modal.style.display = "none";
    }

    // 모달 외부 클릭 시 모달 닫기
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    // 전화번호 잘림 적용 함수
    function hidePhoneNumber(phoneNumber) {
        return phoneNumber.slice(0, 3) + "****" + phoneNumber.slice(-4);
    }

    // 전화번호 잘림 적용
    const phoneElement = document.getElementById('phone');
    if (phoneElement) {
        const hiddenPhone = hidePhoneNumber(phoneElement.textContent);
        phoneElement.textContent = hiddenPhone;
    }


    function updateUser(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        const updatedUserData = {
            uid: uidInput.value, // 수정할 사용자의 고유 ID
            name: nameInput.value,
            gender: Array.from(genderInputs).find(input => input.checked)?.value,
            email: emailInput.value,
            hp: hpInput.value,
            postcode: zipcodeInput.value,
            addr: address1Input.value,
            addr2: address2Input.value,
            regDate: regdateInput.value,
            lastDate: lastdateInput.value,
        };

        if (!uidInput.value) {
            console.error("UID는 비어 있을 수 없습니다.");
            return; // uid가 없을 경우 더 이상 진행하지 않음
        }

        // UID와 데이터 출력
        console.log("UID:", uidInput.value);
        console.log("Updated User Data:", updatedUserData);

        // POST 요청을 통해 수정된 회원 정보 전송
        fetch(`/admin/user/member/update?uid=${uidInput.value}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedUserData),  // 수정된 회원 정보 JSON 형식으로 전송
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then(data => {
                alert(data.message); // 서버에서 반환된 성공 메시지 표시
                modal.style.display = 'none'; // 모달을 숨김
                window.location.href = '/admin/user/list'; // 리스트 페이지로 이동
            })
            .catch(error => console.error("Error updating user data:", error));
    }

// "수정하기" 버튼에 클릭 이벤트 추가
    const updateButton = modal.querySelector("button[type='submit']"); // 수정 버튼
    updateButton.addEventListener("click", updateUser);
});







