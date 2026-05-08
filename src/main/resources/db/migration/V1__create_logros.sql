CREATE TABLE logros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    juego_id BIGINT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(500)
);

CREATE TABLE logros_usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    juego_id BIGINT NOT NULL,
    logro_id BIGINT NOT NULL,
    desbloqueado_en DATETIME NOT NULL,
    CONSTRAINT uk_logros_usuario_logro UNIQUE (usuario_id, logro_id),
    CONSTRAINT fk_logros_usuario_logro FOREIGN KEY (logro_id) REFERENCES logros(id)
);
