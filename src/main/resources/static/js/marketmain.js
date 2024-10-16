

const slides = document.querySelectorAll('.slide');
const prevBtn = document.querySelector('.prev');
const nextBtn = document.querySelector('.next');
const dots = document.querySelectorAll('.dot');
let currentSlide = 0;

function showSlide(index) {
    if (index >= slides.length) {
        currentSlide = 0;
    } else if (index < 0) {
        currentSlide = slides.length - 1;
    } else {
        currentSlide = index;
    }

    document.querySelector('.slider-wrapper').style.transform = `translateX(-${currentSlide * 100}%)`;

    // Update active dot
    dots.forEach(dot => dot.classList.remove('active'));
    dots[currentSlide].classList.add('active');
}


// Event listeners for buttons
prevBtn.addEventListener('click', () => showSlide(currentSlide - 1));
nextBtn.addEventListener('click', () => showSlide(currentSlide + 1));

// Event listeners for pagination dots
dots.forEach((dot, index) => {
    dot.addEventListener('click', () => showSlide(index));
});

// Initialize first slide
showSlide(currentSlide);


const hitProducts = document.querySelectorAll('.hit-product-item');
const prevHitBtn = document.querySelector('.prev-hit');
const nextHitBtn = document.querySelector('.next-hit');
const hitWrapper = document.querySelector('.hit-products-wrapper');

let currentHitIndex = 0;
const maxHitsVisible = 4; // 한번에 보이는 상품 수
const maxHits = hitProducts.length - maxHitsVisible; // 전체 상품 수에서 보이는 상품 수를 뺀 값

function showHitSlide(index) {
    if (index > maxHits) {
        currentHitIndex = 0;
    } else if (index < 0) {
        currentHitIndex = maxHits;
    } else {
        currentHitIndex = index;
    }

    hitWrapper.style.transform = `translateX(-${currentHitIndex * (100 / maxHitsVisible)}%)`;
}

// Event listeners for buttons
prevHitBtn.addEventListener('click', () => showHitSlide(currentHitIndex - 1));
nextHitBtn.addEventListener('click', () => showHitSlide(currentHitIndex + 1));

