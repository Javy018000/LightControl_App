package com.example.lightcontrol_app.Modelo_RecycleView;

public class OrdenServicioVistaPrevia {
        private int id;
        private String description;

        public OrdenServicioVistaPrevia(int id, String description) {
            this.id = id;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }
}
