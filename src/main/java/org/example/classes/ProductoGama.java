package org.example.classes;

public class ProductoGama {
        private int idGama;
        private String nombreGama;
        private String codigoProducto;
        private String nombreProducto;

        public ProductoGama(String codigoProducto, String nombreProducto, int idGama, String gama) {
            this.codigoProducto = codigoProducto;
            this.nombreProducto = nombreProducto;
            this.idGama = idGama;
            this.nombreGama = nombreGama;
        }

        // Método toString() para imprimir la información del producto
        @Override
        public String toString() {
            return "Código: " + codigoProducto + ", Nombre: " + nombreProducto +
                    ", Gama (ID: " + idGama + "): " + nombreGama;
        }
    }

