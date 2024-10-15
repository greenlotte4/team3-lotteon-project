document.querySelectorAll('.admin_menu > li').forEach(menu => {
    const submenu = menu.querySelector('.admin_sub_menu');
    
    menu.addEventListener('click', function(e) {
        e.stopPropagation();

        
        if(submenu) {
            submenu.classList.toggle('open');

        }
    });

    // 서브 메뉴 항목 클릭 이벤트
    if(submenu){
        submenu.querySelectorAll('li').forEach(subLi => {
            subLi.addEventListener('click', function(e){
                e.stopPropagation();       
            })
        })
    }


})
console.log("script.js 파일이 정상적으로 로드되었습니다.");
