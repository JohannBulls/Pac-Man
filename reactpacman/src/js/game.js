export const initializeGame = (containerId, rows, cols, matrix) => {
    console.log("llega a game.js"+matrix);
    const tablero = document.getElementById(containerId);
    // Elimina cualquier elemento de tablero existente
    while (tablero.firstChild) {
        tablero.removeChild(tablero.firstChild);
    }

    // Establecer el tamaño del tablero en función del número de filas y columnas
    tablero.style.gridTemplateColumns = `repeat(${cols}, 1fr)`;
    tablero.style.gridTemplateRows = `repeat(${rows}, 1fr)`;

    // Crear el tablero dinámicamente basado en la matriz
    for (let i = 0; i < rows; i++) {
        for (let j = 0; j < cols; j++) {
            const celda = document.createElement('div');
            celda.className = 'celda';
            celda.id = `celda-${i}-${j}`;
            // Aplicar el color basado en la matriz
            celda.style.backgroundColor = matrix[i][j] === 1 ? 'blue' : 'white';
            tablero.appendChild(celda);
        }
    }

    let posicionX = 22;
    let posicionY = 14;

    // Posicionar el cuadrado inicialmente
    const actualizarCuadrado = () => {
        document.querySelectorAll('.cuadrado').forEach(celda => celda.classList.remove('cuadrado'));
        const celdaActual = document.getElementById(`celda-${posicionY}-${posicionX}`);
        celdaActual.classList.add('cuadrado');
    }

    // Mover el cuadrado con las flechas del teclado
    document.addEventListener('keydown', (e) => {
        let nuevaX = posicionX;
        let nuevaY = posicionY;

        switch (e.key) {
            case 'ArrowUp':
                nuevaY--;
                break;
            case 'ArrowDown':
                nuevaY++;
                break;
            case 'ArrowLeft':
                nuevaX--;
                break;
            case 'ArrowRight':
                nuevaX++;
                break;
        }

        // Verificar si la nueva posición es válida
        if (nuevaX >= 0 && nuevaX < cols && nuevaY >= 0 && nuevaY < rows) {
            const nuevaCelda = document.getElementById(`celda-${nuevaY}-${nuevaX}`);
            if (nuevaCelda.style.backgroundColor !== 'blue') {
                posicionX = nuevaX;
                posicionY = nuevaY;
                actualizarCuadrado();
            }
        }
    });

    // Inicializar la posición del cuadrado
    actualizarCuadrado();
}
