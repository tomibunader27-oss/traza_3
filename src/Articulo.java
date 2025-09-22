
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@SuperBuilder


public class Articulo  {
    protected Long id;
    protected String denominacion;
    protected Double precioVenta;



    @Builder.Default

    protected Set<ImagenArticulo> imagenes = new HashSet<>();


    protected UnidadMedida unidadMedida;


    private Categoria categoria;


}

