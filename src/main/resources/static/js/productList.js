    // Select/Deselect all checkboxes
    document.getElementById("selectAll").addEventListener("click", function() {
    const checkboxes = document.querySelectorAll(".product-checkbox");
    checkboxes.forEach(checkbox => checkbox.checked = this.checked);
});

    // Delete Selected button event listener
    document.getElementById("deleteSelectedButton").addEventListener("click", function() {
        const selectedProducts = Array.from(document.querySelectorAll('.product-checkbox:checked'))
            .map(checkbox => {
                console.log(checkbox.dataset.productId); // Log each data-product-id to verify
                return Number(checkbox.dataset.productId); // Convert ID to number
            });

        console.log("selectedProducts", selectedProducts);

        if (selectedProducts.length === 0) {
            alert("선택된 상품이 없습니다.");
            return;
        }

        if (confirm("선택된 상품을 삭제하시겠습니까?")) {
            fetch('/seller/product/deleteSelected', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(selectedProducts) // Send selected product IDs as JSON array
            })
                .then(response => {
                    if (response.headers.get("content-type")?.includes("application/json")) {
                        return response.json();
                    } else {
                        throw new Error("Non-JSON response received");
                    }
                })
                .then(data => {
                    console.log("Response received:", data);
                    if (data.success) {
                        alert("선택된 상품이 삭제되었습니다.");
                        location.reload();
                    } else {
                        alert("삭제 중 오류가 발생했습니다.");
                    }
                })
                .catch(error => {
                    console.error("Error deleting products:", error);
                    alert("삭제 중 오류가 발생했습니다.");
                });
        }
    });
