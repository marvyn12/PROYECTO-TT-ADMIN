document.addEventListener('DOMContentLoaded', () => {
    const formRegistro = document.getElementById('formRegistro');

    if (formRegistro) {
        formRegistro.addEventListener('submit', function (e) {
            e.preventDefault();

            // Obtenemos los valores principales
            const tipoUsuario = document.getElementById('tipoUsuario').value;
            const nombre = document.getElementById('nombre').value.trim();
            const correo = document.getElementById('correo').value.trim();
            const contrasena = document.getElementById('contrasena').value;

            // Estos campos pueden venir vacíos dependiendo del rol seleccionado
            const boleta = document.getElementById('boleta').value.trim();
            const carrera = document.getElementById('carrera').value;
            const numeroEmpleado = document.getElementById('numeroEmpleado').value.trim();

            if (!tipoUsuario) {
                Swal.fire({
                    icon: 'warning',
                    title: 'Falta información',
                    text: 'Por favor, selecciona qué tipo de usuario eres.',
                    confirmButtonColor: '#6A1C32'
                });
                return;
            }

            // Validaciones según el Tipo de Usuario
            if (tipoUsuario === 'ALUMNO') {
                if (!/^\d{10}$/.test(boleta)) {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Boleta inválida',
                        text: 'La boleta debe tener exactamente 10 dígitos numéricos.',
                        confirmButtonColor: '#6A1C32'
                    });
                    return;
                }
                if (!correo.endsWith('@alumno.ipn.mx')) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Correo inválido',
                        text: 'El registro para alumnos es exclusivo con terminación @alumno.ipn.mx',
                        confirmButtonColor: '#6A1C32'
                    });
                    return;
                }
            } else if (tipoUsuario === 'DOCENTE') {
                // El número de empleado es opcional, pero si lo ponen, deben ser solo números
                if (numeroEmpleado !== '' && !/^\d+$/.test(numeroEmpleado)) {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Número de empleado inválido',
                        text: 'El número de empleado solo debe contener dígitos numéricos.',
                        confirmButtonColor: '#6A1C32'
                    });
                    return;
                }
                if (!correo.endsWith('@ipn.mx')) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Correo inválido',
                        text: 'El registro para docentes es exclusivo con terminación @ipn.mx',
                        confirmButtonColor: '#6A1C32'
                    });
                    return;
                }
            } else if (tipoUsuario === 'EXTERNO') {
                // Validación estándar para cualquier formato de correo válido
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(correo)) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Correo inválido',
                        text: 'Por favor ingresa un correo electrónico válido.',
                        confirmButtonColor: '#6A1C32'
                    });
                    return;
                }
            }

            // Validación de Contraseña Segura (Aplica para todos)
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

            // Petición AJAX (mandamos todos los campos, el backend guardará los correspondientes al rol)
            fetch('/api/auth/registro', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    rol: tipoUsuario,
                    nombre: nombre,
                    boleta: boleta,
                    carrera: carrera,
                    numeroEmpleado: numeroEmpleado,
                    correo: correo,
                    contrasena: contrasena
                })
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