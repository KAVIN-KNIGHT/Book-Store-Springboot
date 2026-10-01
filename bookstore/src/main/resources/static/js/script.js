// Enhanced Header scroll effect with smooth transitions
document.addEventListener('DOMContentLoaded', function() {
    const header = document.querySelector('.site-header');
    const scrollThreshold = 50;
    let lastScroll = 0;

    function handleScroll() {
        const currentScroll = window.scrollY;
        
        if (currentScroll > scrollThreshold) {
            header.classList.add('scrolled');
        } else {
            header.classList.remove('scrolled');
        }
        
        lastScroll = currentScroll;
    }

    // Throttle scroll events for better performance
    let ticking = false;
    window.addEventListener('scroll', function() {
        if (!ticking) {
            window.requestAnimationFrame(function() {
                handleScroll();
                ticking = false;
            });
            ticking = true;
        }
    });
});

// Mobile menu toggle
document.addEventListener('DOMContentLoaded', function() {
    const menuToggle = document.querySelector('.mobile-menu-toggle');
    const navMenu = document.querySelector('.nav-menu');

    if (menuToggle && navMenu) {
        menuToggle.addEventListener('click', function() {
            navMenu.classList.toggle('active');
            menuToggle.setAttribute('aria-expanded', 
                navMenu.classList.contains('active'));
        });
    }
});

// Form validation
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');

    forms.forEach(form => {
        const requiredInputs = form.querySelectorAll('[required]');
        const submitButton = form.querySelector('button[type="submit"]');

        if (submitButton) {
            form.addEventListener('input', function() {
                let isValid = true;
                requiredInputs.forEach(input => {
                    if (!input.value.trim()) {
                        isValid = false;
                    }
                });
                submitButton.disabled = !isValid;
            });
        }
    });
});

// Quantity input handlers
document.addEventListener('DOMContentLoaded', function() {
    const quantityInputs = document.querySelectorAll('.quantity-input');

    quantityInputs.forEach(input => {
        const decrementBtn = input.parentElement.querySelector('.quantity-decrement');
        const incrementBtn = input.parentElement.querySelector('.quantity-increment');

        if (decrementBtn && incrementBtn) {
            decrementBtn.addEventListener('click', () => {
                const currentValue = parseInt(input.value) || 1;
                if (currentValue > 1) {
                    input.value = currentValue - 1;
                    input.dispatchEvent(new Event('change'));
                }
            });

            incrementBtn.addEventListener('click', () => {
                const currentValue = parseInt(input.value) || 1;
                input.value = currentValue + 1;
                input.dispatchEvent(new Event('change'));
            });
        }
    });
});

// Alert dismissal
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');

    alerts.forEach(alert => {
        if (!alert.classList.contains('alert-persistent')) {
            setTimeout(() => {
                alert.style.opacity = '0';
                setTimeout(() => {
                    alert.remove();
                }, 300);
            }, 5000);
        }
    });
});

// Book card hover effect
document.addEventListener('DOMContentLoaded', function() {
    const bookCards = document.querySelectorAll('.book-card');

    bookCards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-8px)';
        });

        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });
});

// Intersection Observer for fade-in animations
document.addEventListener('DOMContentLoaded', function() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);

    // Observe elements with fade-in class
    document.querySelectorAll('.feature-card, .testimonial-card, .category-card').forEach(el => {
        el.style.opacity = '0';
        el.style.transform = 'translateY(20px)';
        el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(el);
    });
});

// Smooth scroll for anchor links
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const href = this.getAttribute('href');
            if (href !== '#' && document.querySelector(href)) {
                e.preventDefault();
                document.querySelector(href).scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
});

// Add loading states to buttons on form submit
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const submitBtn = this.querySelector('button[type="submit"]');
            if (submitBtn && !submitBtn.classList.contains('no-loading')) {
                submitBtn.classList.add('btn-loading');
                submitBtn.disabled = true;
            }
        });
    });
});

// Image lazy loading fallback
document.addEventListener('DOMContentLoaded', function() {
    if ('loading' in HTMLImageElement.prototype) {
        const images = document.querySelectorAll('img[loading="lazy"]');
        images.forEach(img => {
            img.src = img.dataset.src || img.src;
        });
    } else {
        // Fallback for browsers that don't support lazy loading
        const script = document.createElement('script');
        script.src = 'https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js';
        document.body.appendChild(script);
    }
});