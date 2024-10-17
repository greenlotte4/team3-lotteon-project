function showEmailInput() {
    // 입력 필드를 활성화
    document.getElementById('email-id').removeAttribute('readonly');
    document.getElementById('email-domain').removeAttribute('readonly');
    document.getElementById('domain-select').removeAttribute('disabled');

    // 수정 버튼 숨기기
    document.getElementById('edit-email-btn').style.display = 'none';
    // 저장 버튼 보이기
    document.getElementById('save-email-btn').style.display = 'inline';
}

function updateDomain() {
    var selectedDomain = document.getElementById("domain-select").value;
    var emailDomainInput = document.getElementById("email-domain");

    // 선택된 도메인이 "직접 입력"일 경우 도메인 입력 필드를 활성화
    if (selectedDomain === "직접 입력") {
        emailDomainInput.value = ""; // 입력 필드 비우기
        emailDomainInput.disabled = false; // 입력 필드 활성화
    } else {
        emailDomainInput.value = selectedDomain; // 입력 필드에 도메인 채우기
        emailDomainInput.disabled = false; // 입력 필드 비활성화
    }

    updateOutputEmail(); // 이메일 출력 업데이트
}
function saveEmail() {
    // 저장 로직을 추가하세요
    // 예: 이메일 저장 후 다시 readonly로 설정
    document.getElementById('email-id').setAttribute('readonly', true);
    document.getElementById('email-domain').setAttribute('readonly', true);
    document.getElementById('domain-select').setAttribute('disabled', true);

    // 수정 버튼 다시 보이기
    document.getElementById('edit-email-btn').style.display = 'inline';
    // 저장 버튼 숨기기
    document.getElementById('save-email-btn').style.display = 'none';

    // 이메일 출력 부분 업데이트
    const emailId = document.getElementById('email-id').value;
    const emailDomain = document.getElementById('email-domain').value;
    document.getElementById('output-email').textContent = emailId + '@' + emailDomain;
}

function moveToNext(current, nextFieldId) {
    if (current.value.length >= current.maxLength) {
        document.getElementById(nextFieldId).focus();
    }
}

function editPhone() {
    // 입력 필드를 활성화
    document.getElementById('phone-part1').removeAttribute('readonly');
    document.getElementById('phone-part2').removeAttribute('readonly');
    document.getElementById('phone-part3').removeAttribute('readonly');

    // 수정 버튼 숨기기
    document.getElementById('edit-phone-btn').style.display = 'none';
    // 저장 버튼 보이기
    document.getElementById('save-phone-btn').style.display = 'inline';
}

function savePhone() {
    // 저장 로직을 추가하세요
    // 예: 전화번호 저장 후 다시 readonly로 설정
    document.getElementById('phone-part1').setAttribute('readonly', true);
    document.getElementById('phone-part2').setAttribute('readonly', true);
    document.getElementById('phone-part3').setAttribute('readonly', true);

    // 수정 버튼 다시 보이기
    document.getElementById('edit-phone-btn').style.display = 'inline';
    // 저장 버튼 숨기기
    document.getElementById('save-phone-btn').style.display = 'none';
}