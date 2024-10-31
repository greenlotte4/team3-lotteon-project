document.addEventListener('DOMContentLoaded', function() {
    const links = document.querySelectorAll('#aside .aside_list ul li a');
    const titleElement = document.querySelector('#service .title h2');  // <h2> 요소
    const descriptionElement = document.querySelector('#service .title p');  // <p> 요소

    // 첫 번째 링크에 기본적으로 active 클래스 추가 및 초기 제목, 설명 설정
    if (links.length > 0) {
        links[0].classList.add('active');
        titleElement.textContent = links[0].textContent;  // 초기 제목 설정
        descriptionElement.textContent = `${links[0].textContent} 관련 문의내용 입니다.`;  // 초기 설명 설정
    }

    // 각 링크 클릭 시 active 클래스 처리 및 제목, 설명 업데이트
    links.forEach(link => {
        link.addEventListener('click', function() {
            // 모든 링크에서 active 클래스 제거
            links.forEach(item => item.classList.remove('active'));
            // 클릭한 링크에 active 클래스 추가
            this.classList.add('active');

            // 클릭한 카테고리명으로 제목 및 설명 업데이트
            const selectedCategory = this.textContent;
            titleElement.textContent = selectedCategory;
            descriptionElement.textContent = `${selectedCategory} 관련 문의내용 입니다.`;
        });
    });
});
