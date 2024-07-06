package com.example.lightcontrol_app.Modelo_RecycleView;

public class OrdenServicio{
        private String id;
        private String description;

        public OrdenServicio(String id, String description) {
            this.id = id;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }
}
