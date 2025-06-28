const checkoutBtn = document.getElementById('checkoutBtn');

if (checkoutBtn) {
    checkoutBtn.addEventListener('click', function() {
        // Reutilize as mesmas variáveis CSRF que já funcionam no addToCart
        const csrfToken = document.querySelector('meta[name="_csrf"]').content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
        
        const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
        
        fetch('/cart/checkout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ paymentMethod: paymentMethod })
        })
        .then(response => {
            if (response.ok) {
                return response.json().then(data => {
                    window.location.href = '/orders/' + data.orderId;
                });
            }
            return response.json().then(err => { 
                throw new Error(err || 'Erro ao finalizar compra:' || err.message);
            });
        })
        .catch(error => {
            alert(error.message);
            console.error('Error:', error);
        });
    });
}

function addToCart(buttonElement) {
    const productId = buttonElement.getAttribute('data-product-id');
    const price = buttonElement.getAttribute('data-product-price');
    const quantity = document.getElementById('quantity-' + productId).value;
    
    const parsedQuantity = parseInt(quantity);
    const parsedPrice = parseFloat(price);
    
    console.log("Adding to cart - Product ID:", productId, "Quantity:", parsedQuantity, "Price:", parsedPrice);

    if (isNaN(parsedQuantity)) {
        return;
    }

    const cartItem = {
        productId: productId,
        quantity: parsedQuantity,
        unitPrice: parsedPrice
    };

    console.log("cartItem: ", cartItem);

    // Obter o token CSRF
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';

    if (!csrfToken) {
        console.error("CSRF token not found");
        showToast('Erro de segurança. Por favor, recarregue a página.', 'error');
        return;
    }

    fetch('/cart/add/' + cartItem.productId, {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify(cartItem)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { 
                throw new Error(err.message || 'Erro ao adicionar ao carrinho') 
            });
        }
        return response;
    })
    .then(data => {
        console.log("Success:", data);
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

