package edu.escuelaing.arsw.ASE.app.springpacman.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "boardStates")
public class BoardState {

    @Id
    private String id; // Asegúrate de que el ID sea del tipo adecuado

    private String state; // Si 'state' debe ser un String, asegúrate de que así esté en tu código

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
