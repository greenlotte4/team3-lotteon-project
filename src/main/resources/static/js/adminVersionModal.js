
// 모달 열고 닫는 기능 구현
var modal = document.getElementById("orderModal");
var btn = document.getElementById("openModalBtn");
var span = document.getElementsByClassName("close")[0];
// 버튼을 클릭하면 모달을 엽니다.
btn.onclick = function() {
    modal.style.display = "block";
}
// 닫기 버튼을 클릭하면 모달을 닫습니다.
span.onclick = function() {
    modal.style.display = "none";
}
// 모달 외부를 클릭하면 모달을 닫습니다.
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

// 모달과 버튼을 선택
var modal2 = document.querySelector(".modal.orderModal"); // .modal.orderModal 클래스를 가진 첫 번째 모달 선택
var btn2 = document.querySelectorAll(".openModalBtn2"); // 모든 모달 열기 버튼 선택
var span2 = document.querySelector(".close2"); // 닫기 버튼
var closeButton2 = document.querySelector(".closeButton"); // '닫기' 버튼 선택

// 각 모달 열기 버튼에 대해 클릭 이벤트 추가
btn2.forEach(function(button) {
    button.onclick = function() {
        modal2.style.display = "block"; // 모달 열기
    };
});

// 닫기 버튼 (×)을 클릭하면 모달을 닫기
span2.onclick = function() {
    modal2.style.display = "none"; // 모달 닫기
};

// '닫기' 버튼을 클릭하면 모달을 닫기
closeButton2.onclick = function() {
    modal2.style.display = "none"; // 모달 닫기
};

// 모달 외부를 클릭하면 모달을 닫기
window.onclick = function(event) {
    if (event.target == modal2) {
        modal2.style.display = "none"; // 모달 닫기
    }
};