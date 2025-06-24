document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.input-group button').forEach(button => {
        button.addEventListener('click', function() {
            const input = this.parentElement.querySelector('input');
            let value = parseInt(input.value);
            
            if (this.textContent === '+' && value < 99) {
                input.value = value + 1;
            } else if (this.textContent === '-' && value > 1) {
                input.value = value - 1;
            }
            
            // Aqui: adicionar uma chamada AJAX para atualizar a quantidade no servidor
        });
    });
    
    // Validação de quantidade
    document.querySelectorAll('.input-group input').forEach(input => {
        input.addEventListener('change', function() {
            let value = parseInt(this.value);
            if (isNaN(value) || value < 1) {
                this.value = 1;
            } else if (value > 99) {
                this.value = 99;
            }
        });
    });
});

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

