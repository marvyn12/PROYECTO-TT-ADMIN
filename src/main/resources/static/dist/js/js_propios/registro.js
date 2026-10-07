document.addEventListener('DOMContentLoaded', () => {
    const formRegistro = document.getElementById('formRegistro');

    if (formRegistro) {
        formRegistro.addEventListener('submit', function (e) {
            e.preventDefault();

            const nombre = document.getElementById('nombre').value.trim();
            const boleta = document.getElementById('boleta').value.trim();
            const carrera = document.getElementById('carrera').value;
            const correo = document.getElementById('correo').value.trim();
            const contrasena = document.getElementById('contrasena').value;

            // Validación de Boleta (Exactamente 10 dígitos)
            if (!/^\d{10}$/.test(boleta)) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Boleta inválida',
                    text: 'La boleta debe tener exactamente 10 dígitos numéricos.',
                    confirmButtonColor: '#6A1C32'
                });
                return;
            }

            // Validación estricta del dominio del correo
            if (!correo.endsWith('@alumno.ipn.mx')) {
                Swal.fire({
                    icon: 'error',
                    title: 'Correo inválido',
                    text: 'El registro es exclusivo para correos con terminación @alumno.ipn.mx',
                    confirmButtonColor: '#6A1C32'
                });
                return;
            }

            // Validación de Contraseña Segura
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
            if (!passwordRegex.test(contrasena)) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Contraseña débil',
                    html: 'Tu contraseña debe contener al menos:<br><br>' +
                        '<ul style="text-align: left; display: inline-block;">' +
                        '<li>8 caracteres</li>' +
                        '<li>Una letra mayúscula</li>' +
                        '<li>Una letra minúscula</li>' +
                        '<li>Un número</li>' +
                        '<li>Un carácter especial (@, $, !, %, *, ?, &)</li></ul>',
                    confirmButtonColor: '#6A1C32'
                });
                return;
            }

            // Petición AJAX agregando la carrera
            fetch('/api/auth/registro', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nombre, boleta, carrera, correo, contrasena })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        Swal.fire({
                            icon: 'success',
                            title: '¡Registro exitoso!',
                            text: data.mensaje,
                            showConfirmButton: false,
                            timer: 2000
                        }).then(() => {
                            window.location.href = '/login';
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error en el registro',
                            text: data.mensaje,
                            confirmButtonColor: '#6A1C32'
                        });
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Error de servidor',
                        text: 'No se pudo conectar con el sistema. Intenta más tarde.',
                        confirmButtonColor: '#6A1C32'
                    });
                });
        });
    }
});