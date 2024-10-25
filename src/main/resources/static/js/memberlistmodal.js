document.addEventListener("DOMContentLoaded", function() {
    var memberListModal = document.getElementById("memberlistModal");
    var openMemberListBtns = document.getElementsByClassName("openModalBtn2");
    var closeMemberListSpan = memberListModal.getElementsByClassName("close")[0];

    // 버튼 클릭 시 모달 열기
    for (let btn of openMemberListBtns) {
        btn.onclick = function(event) {
            event.preventDefault();

            // 데이터 속성에서 직접 값을 가져옴
            const memberData = {
                uid: btn.dataset.uid,
                name: btn.dataset.name,
                gender: btn.dataset.gender,
                grade: btn.dataset.grade,
                email: btn.dataset.email,
                hp: btn.dataset.hp,
                postcode: btn.dataset.postcode,
                addr: btn.dataset.addr,
                addr2: btn.dataset.addr2
            };

            // 각 필드에 값 설정
            document.getElementById("uid").value = memberData.uid || '';
            document.getElementById("name").value = memberData.name || '';
            document.getElementById("status").value = memberData.grade || '';
            document.getElementById("EMAIL").value = memberData.email || '';
            document.getElementById("HP").value = memberData.hp || '';
            document.getElementById("Postcode").value = memberData.postcode || '';
            document.getElementById("addr").value = memberData.addr || '';
            document.getElementById("addr2").value = memberData.addr2 || '';

            // 성별 라디오 버튼 설정
            const genderRadios = document.querySelectorAll('input[name="gender"]');
            genderRadios.forEach(radio => {
                radio.checked = radio.value === memberData.gender;
            });

            memberListModal.style.display = "block";
        }
    }

    // 닫기 버튼 이벤트
    if (closeMemberListSpan) {
        closeMemberListSpan.onclick = function() {
            memberListModal.style.display = "none";
        }
    }

    // 모달 외부 클릭 시 닫기
    window.onclick = function(event) {
        if (event.target == memberListModal) {
            memberListModal.style.display = "none";
        }
    }
});