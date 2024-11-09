document.addEventListener('DOMContentLoaded', function () {
    fetchBestProducts();
    const eventSource = new EventSource('/sse/best-products');
    eventSource.onopen = function(event) {
        console.log("SSE 연결이 열렸습니다.");
    };



    eventSource.onerror = function (error) {
        if (eventSource.readyState === EventSource.CLOSED) {
            console.error("연결이 닫혔습니다.");
        }
            console.error('Error receiving SSE:', error);
            eventSource.close();


    };

    eventSource.onmessage = function (event) {
        const data = JSON.parse(event.data);
        updateBestProductList(data);  // Define this function to update the DOM
    };
});


function fetchBestProducts() {
    fetch('/api/best-products')
        .then(response => response.json())
        .then(data => updateBestProductList(data))
        .catch(error => console.error('Error fetching best products:', error));
}

function updateBestProductList(products) {
    const bestProductContainer = document.querySelector('.bestProduct');
    bestProductContainer.innerHTML = `
        <span><img src="/images/common/aside_best_product_tagR.png" alt="태그">베스트 상품</span>
    `;

    // Loop over products and append to the bestProductContainer
    products.forEach((product, index) => {
        const productHTML = `
            <div class="products ${index === 0 ? 'first' : ''}">
                <div class="productimg ${index === 0 ? 'first' : ''}">
                    <img src="${product.savedPath}/${product.file230}" alt="${product.productName}">
                    <p>${index + 1}</p>
                </div>
                <div class="productInfo">
                    <span class="product-name">${product.productName}</span>
                    <span class="original-price">${product.originalPrice.toLocaleString()}원</span>
                    <span class="discounted-price">
                        <p class="discount-rate">${product.discount}%</p> <p>↓</p>
                    </span>
                    <span class="final-price">${product.finalPrice.toLocaleString()}원</span>
                </div>
            </div>
        `;
        bestProductContainer.insertAdjacentHTML('beforeend', productHTML);
    });
}