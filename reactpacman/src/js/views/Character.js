// src/js/views/Character.js

import React from 'react';
import PropTypes from 'prop-types';
import './Character.css'; // Asegúrate de crear el archivo CSS si necesitas estilos específicos

const Character = ({ type, position }) => {
  return (
    <div className={`character ${type}`} style={{ top: position.top, left: position.left }}>
      {/* Renderiza el tipo de personaje o cualquier otro contenido necesario */}
      {type === 'pacman' ? '😃' : '👻'}
    </div>
  );
};

Character.propTypes = {
  type: PropTypes.string.isRequired,
  position: PropTypes.shape({
    top: PropTypes.number.isRequired,
    left: PropTypes.number.isRequired
  }).isRequired
};

export default Character;
