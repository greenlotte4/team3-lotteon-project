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

            // data-id에서 uid 가져오기
            const uid = btn.getAttribute('data-id'); // 'data-id'에서 uid 가져오기

            // UID로 멤버 정보 가져오기
            fetch(`/admin/user/list/${uid}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('네트워크 응답이 좋지 않습니다.');
                    }
                    return response.json();
                })
                .then(memberData => {
                    // 각 필드에 값 설정
                    document.getElementById("uid").value = memberData.user.uid || ''; // uid
                    document.getElementById("name").value = memberData.name || '';      // 이름
                    document.getElementById("status").value = memberData.grade || '';   // 등급
                    document.getElementById("EMAIL").value = memberData.email || '';    // 이메일
                    document.getElementById("HP").value = memberData.hp || '';          // 휴대폰
                    document.getElementById("Postcode").value = memberData.postcode || ''; // 우편번호
                    document.getElementById("addr").value = memberData.addr || '';      // 주소
                    document.getElementById("addr2").value = memberData.addr2 || '';    // 상세주소

                    // 성별 라디오 버튼 설정
                    const genderRadios = document.querySelectorAll('input[name="gender"]');
                    genderRadios.forEach(radio => {
                        radio.checked = radio.value === memberData.gender;
                    });

                    // 모달 창 표시
                    memberListModal.style.display = "block";
                })
                .catch(error => {
                    console.error('Error fetching member data:', error);
                });
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



