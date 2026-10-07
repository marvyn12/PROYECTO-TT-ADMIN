document.addEventListener('DOMContentLoaded', () => {
    const formLogin = document.getElementById('formLogin');

    if (formLogin) {
        formLogin.addEventListener('submit', function (e) {
            e.preventDefault();

            const correo = document.getElementById('correo').value;
            const contrasena = document.getElementById('contrasena').value;

            // Validación de dominio IPN
            if (!correo.endsWith('@alumno.ipn.mx') && !correo.endsWith('@ipn.mx')) {
                alert('El acceso está restringido a correos institucionales del IPN.');
                return;
            }

            fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ correo: correo, contrasena: contrasena })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        window.location.href = '/';
                    } else {
                        alert(data.mensaje);
                    }
                })
                .catch(error => console.error('Error:', error));
        });
    }
});