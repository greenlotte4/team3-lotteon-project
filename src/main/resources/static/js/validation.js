// 유효성 검사에 사용할 정규표현식
const reUid   = /^[a-z]+[a-z0-9]{4,19}$/g;
const rePass  = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/;
const reName  = /^[가-힣]{2,10}$/;
const reEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const reHp    = /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/;

let isUidOk   = false;
let isPassOk  = false;
let isNameOk  = false;
let isEmailOk = false;
let isHpOk    = false;

window.onload = function(){

    const registerForm = document.getElementsByTagName('form')[0];

    // 1.아이디 유효성 검사
    const btnCheckUid = document.getElementById('btnCheckUid');
    btnCheckUid.onclick = function(){
        const uid = registerForm.uid.value;
        if (!uid.match(reUid)) {
            alert('아이디가 유효하지 않습니다.');
            return;
        }
        fetch('/user/checkUser?type=uid&value='+uid)
            .then(resp => resp.json())
            .then(data => {
                if (data.result > 0) {
                    alert('이미 사용중인 아이디 입니다.');
                    isUidOk = false;
                } else {
                    alert('사용 가능한 아이디 입니다.');
                    isUidOk = true;
                }
            })
            .catch(err => {
                console.error(err);
            });
    };

    // 2. 비밀번호 유효성 검사
    const pass1 = registerForm.pass;
    const pass2 = registerForm.passConfirm;
    pass2.addEventListener('focusout', function(){
        if (!pass1.value.match(rePass)) {
            alert('비밀번호가 유효하지 않습니다.');
            return;
        }
        if (pass1.value === pass2.value) {
            alert('비밀번호가 일치합니다.');
            isPassOk = true;
        } else {
            alert('비밀번호가 일치하지 않습니다.');
            isPassOk = false;
        }
    });

    // 3. 이름 유효성 검사
    registerForm.name.addEventListener('focusout', function(){
        const name = registerForm.name.value;
        if (!name.match(reName)) {
            alert('이름이 유효하지 않습니다.');
            isNameOk = false;
        } else {
            isNameOk = true;
        }
    });

    // 4. 이메일 유효성 검사
    const btnSendEmail = document.getElementById('btnSendEmail');
    btnSendEmail.onclick = function() {
        const email = registerForm.email.value;
        if (!email.match(reEmail)) {
            alert('이메일이 유효하지 않습니다.');
            return;
        }
        fetch('/user/checkUser?type=email&value=' + email)
            .then(resp => resp.json())
            .then(data => {
                if (data.result > 0) {
                    alert('이미 사용중인 이메일 입니다.');
                    isEmailOk = false;
                } else {
                    alert('이메일 인증 코드를 확인하세요.');
                    isEmailOk = true;
                }
            })
            .catch(err => {
                console.error(err);
            });
    };

    // 5. 휴대폰 유효성 검사
    registerForm.hp.addEventListener('focusout', function(){
        const hp = registerForm.hp.value;
        if (!hp.match(reHp)) {
            alert('휴대폰 번호가 유효하지 않습니다.');
            return;
        }
        fetch('/user/checkUser?type=hp&value=' + hp)
            .then(resp => resp.json())
            .then(data => {
                if (data.result > 0) {
                    alert('이미 사용중인 휴대폰번호 입니다.');
                    isHpOk = false;
                } else {
                    isHpOk = true;
                }
            })
            .catch(err => {
                console.error(err);
            });
    });

    // 최종 폼 전송 유효성 검사
    registerForm.onsubmit = function(){
        if (!isUidOk || !isPassOk || !isNameOk || !isEmailOk || !isHpOk) {
            alert('입력하신 정보에 문제가 있습니다.');
            return false;  // 폼 전송 중단
        }
        return true;
    };
};
