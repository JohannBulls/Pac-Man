// src/main/java/edu/escuelaing/arsw/ASE/app/springpacman/repository/BoardStateRepository.java
package edu.escuelaing.arsw.ASE.app.springpacman.repository;

import edu.escuelaing.arsw.ASE.app.springpacman.model.BoardState;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardStateRepository extends MongoRepository<BoardState, String> {
}
