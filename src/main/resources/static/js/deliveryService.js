// 배송지 리스트를 동적으로 생성

var deliveryDTOList = /*[[${deliveryDTOList}]]*/[];
renderAddressList(deliveryDTOList);


function renderAddressList(deliveryDTOList) {
    const addressListDiv = document.querySelector('.address-list');
    addressListDiv.innerHTML = '';  // 기존 목록 초기화

    // 각 배송지 정보에 대해 동적으로 주소 항목 생성
    deliveryDTOList.forEach(function (delivery) {
        const addressItemDiv = document.createElement('div');
        addressItemDiv.classList.add('address-item');

        // 주소 정보
        const addressInfoDiv = document.createElement('div');
        addressInfoDiv.classList.add('address-info');
        addressItemDiv.appendChild(addressInfoDiv);

        // 라디오 버튼
        const radioInput = document.createElement('input');
        radioInput.type = 'radio';
        radioInput.name = 'address';
        radioInput.id = `address${delivery.id}`; // 배송지 ID로 고유값 설정
        addressInfoDiv.appendChild(radioInput);

        // 배송지 이름
        const label = document.createElement('label');
        label.setAttribute('for', radioInput.id);
        label.textContent = delivery.name;
        addressInfoDiv.appendChild(label);

        // 주소
        const addressText = document.createElement('p');
        addressText.innerHTML = `[${delivery.postcode}] ${delivery.addr}`;
        addressInfoDiv.appendChild(addressText);

        // 전화번호
        const phoneText = document.createElement('p');
        phoneText.textContent = delivery.hp;
        addressInfoDiv.appendChild(phoneText);

        // 수정, 삭제 버튼
        const addressBtnDiv = document.createElement('div');
        addressBtnDiv.classList.add('address-btn');
        addressItemDiv.appendChild(addressBtnDiv);

        const editButton = document.createElement('button');
        editButton.classList.add('edit-btn');
        editButton.textContent = '수정';
        addressBtnDiv.appendChild(editButton);

        const deleteButton = document.createElement('button');
        deleteButton.classList.add('delete-btn');
        deleteButton.textContent = '삭제';
        addressBtnDiv.appendChild(deleteButton);

        // 주소 항목을 리스트에 추가
        addressListDiv.appendChild(addressItemDiv);
    });
}

// // 페이지가 로드될 때 서버에서 전달된 배송지 목록을 렌더링
// document.addEventListener('DOMContentLoaded', function () {
//     const deliveryDTOList = /* 서버에서 전달된 배송지 데이터 */ [];
//     renderAddressList(deliveryDTOList);
// });
//
// document.addEventListener("DOMContentLoaded", function () {
//     const memberId = 3; // 실제 Member ID 사용
//     const addressSelectModal = document.getElementById("addressSelectModal");
//     const addressListDiv = document.querySelector(".address-list");
//
//     // 배송지 목록 불러오기
//     function loadDeliveries() {
//         fetch(`/api/member/${memberId}/deliveries`)
//             .then(response => response.json())
//             .then(deliveries => {
//                 addressListDiv.innerHTML = '';
//                 deliveries.forEach((delivery, index) => {
//                     const addressItem = document.createElement("div");
//                     addressItem.classList.add("address-item");
//
//                     addressItem.innerHTML = `
//                         <div class="address-info">
//                             <input type="radio" id="address${index}" name="address">
//                             <label for="address${index}">${delivery.name}</label>
//                             <p>[${delivery.postcode}] ${delivery.addr} ${delivery.addr2}</p>
//                             <p>${delivery.hp}</p>
//                         </div>
//                         <div class="address-btn">
//                             <button class="edit-btn" onclick="editDelivery(${delivery.id})">수정</button>
//                             <button class="delete-btn" onclick="deleteDelivery(${delivery.id})">삭제</button>
//                         </div>
//                     `;
//                     addressListDiv.appendChild(addressItem);
//                 });
//             })
//             .catch(error => console.error("배송지 목록 불러오기 실패:", error));
//     }

    // 새 배송지 추가
    document.getElementById("addressForm").addEventListener("submit", function (event) {
        event.preventDefault();

        const deliveryData = {
            name: document.getElementById("name").value,
            hp: document.getElementById("phoneNumber").value,
            postcode: document.getElementById("postalCode").value,
            addr: document.getElementById("addressLine1").value,
            addr2: document.getElementById("addressLine2").value,
            // entranceCode: document.getElementById("entranceCode").value,
            deliveryMessage: document.getElementById("deliveryMessage").value,
        };

        console.log("Member ID:", memberId);
        console.log("보내는 JSON 데이터:", JSON.stringify(deliveryData));


        fetch(`/api/member/${memberId}/delivery`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(deliveryData)
        })
            .then(response => response.json())
            .then(data => {
                console.log("배송지 추가 성공:", data);
                alert('배송지가 등록되었습니다.');
                loadDeliveries();

            })
            .catch(error => console.error("배송지 추가 실패:", error));
    });

//     loadDeliveries();
// });

