import lombok.*;
import lombok.experimental.SuperBuilder;

    @AllArgsConstructor
    @NoArgsConstructor(force = true)
    @Setter
    @Getter
    @ToString
    @SuperBuilder

public class Stock {
    private final Articulo articulo;
    private final Sucursal sucursal;
    private int stockSucursal;
    private int stockEmpresa; // total a nivel empresa para ese artículo

    // ... constructor/getters/setters iguales a antes

    public void setStockEmpresa(int total) {
        this.stockEmpresa = Math.max(0, total);
    }
    public int getStockEmpresa() { return stockEmpresa; }
}
