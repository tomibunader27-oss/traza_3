


import lombok.*;
import lombok.experimental.SuperBuilder;





@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class ImagenArticulo {
    private Long id;
    private String name;
    private String url;

}
