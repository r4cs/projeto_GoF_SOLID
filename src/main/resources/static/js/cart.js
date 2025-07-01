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

function removeItem(productId) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
    
    fetch('/cart/remove/' + productId, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        }
    }).then(response => {
        if(response.ok) {
            window.location.reload();
        } else {
            return response.json().then(err => {
                throw new Error(err.error || 'Erro ao remover item');
            });
        }
    })
    .catch(error => {
        alert(error.message);
        console.error('Error:', error);
    });
}

function updateQuantity(productId, change) {
    const input = document.querySelector(`input[data-product-id="${productId}"]`);
    let newQuantity = parseInt(input.value) + change;
    
    newQuantity = Math.max(1, newQuantity);
    
    input.value = newQuantity;
    
    updateCartItem(productId, newQuantity);
}

function updateCartItem(productId, quantity) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
    
    const input = document.querySelector(`input[data-product-id="${productId}"]`);
    input.classList.add('updating');
    
    fetch('/cart/update/' + productId, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({ quantity: quantity })
    })
    .then(response => {
        if (!response.ok) {
            // Se a resposta não for OK, tentamos extrair o erro
            return response.text().then(text => {
                throw new Error(text || 'Falha ao atualizar quantidade');
            });
        }
        // Tentamos parsear como JSON apenas se houver conteúdo
        return response.text().then(text => {
            return text ? JSON.parse(text) : {};
        });
    })
    .then(data => {
        if (data && data.total) {
            updateCartTotals(data.total);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showError(error.message);
        input.value = input.defaultValue;
    })
    .finally(() => {
        input.classList.remove('updating');
    });
}

function updateCartTotals(newTotal) {
    // Atualiza os elementos que mostram o total
    document.querySelectorAll('.cart-total').forEach(el => {
        el.textContent = `R$ ${newTotal.toFixed(2)}`;
    });
}


function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'alert alert-danger position-fixed top-0 end-0 m-3';
    errorDiv.style.zIndex = '1000';
    errorDiv.textContent = message;
    document.body.appendChild(errorDiv);
    setTimeout(() => errorDiv.remove(), 5000);
}