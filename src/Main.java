import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public class Main {
    public static void main(String[] args) {

        // Inicializar repositorios
        InMemoryRepository<Empresa> empresaRepository = new InMemoryRepository<>();
        System.out.println(" -----------PROBAMOS EL SISTEMA ----------");
        Pais argentina = Pais.builder().nombre("Argentina").
                build();

        // Crear provincias y localidades
        Provincia buenosAires = Provincia.builder()
                .id(1L)
                .nombre("Buenos Aires")
                .pais(argentina)
                .build();

        Localidad caba = Localidad.builder()
                .id(1L)
                .nombre("CABA")
                .provincia(buenosAires)
                .build();

        Domicilio domicilio1 = Domicilio.builder()
                .id(1L)
                .calle("Calle 1")
                .numero(100)
                .cp(1000)
                .piso(1)
                .nroDpto(1)
                .localidad(caba)
                .build();

        Localidad laPlata = Localidad.builder()
                .id(2L)
                .nombre("La Plata")
                .provincia(buenosAires)
                .build();

        Domicilio domicilio2 = Domicilio.builder()
                .id(2L)
                .calle("Calle 2")
                .numero(200)
                .cp(2000)
                .piso(2)
                .nroDpto(2)
                .localidad(laPlata)
                .build();


        Provincia cordoba = Provincia.builder()
                .id(2L)
                .nombre("Córdoba")
                .pais(argentina)
                .build();

        Localidad cordobaCapital = Localidad.builder()
                .id(3L)
                .nombre("Córdoba Capital")
                .provincia(cordoba)
                .build();

        Domicilio domicilio3 = Domicilio.builder()
                .id(3L)
                .calle("Calle 3")
                .numero(300)
                .cp(3000)
                .piso(3)
                .nroDpto(3)
                .localidad(cordobaCapital)
                .build();


        Localidad villaCarlosPaz = Localidad.builder()
                .id(4L)
                .nombre("Villa Carlos Paz")
                .provincia(cordoba)
                .build();


        Domicilio domicilio4 = Domicilio.builder()
                .id(4L)
                .calle("Calle 4")
                .numero(400)
                .cp(4000)
                .piso(4)
                .nroDpto(4)
                .localidad(villaCarlosPaz)
                .build();

        // Crear sucursales Para buenos Aires
        Sucursal sucursal1 = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal 1")
                .horarioApertura(LocalTime.of(9, 0))
                .horarioCierre(LocalTime.of(18, 0))
                .esCasaMatriz(true)
                .domicilio(domicilio1)
                .build();

        Sucursal sucursal2 = Sucursal.builder()
                .id(2L)
                .nombre("Sucursal 2")
                .horarioApertura(LocalTime.of(9, 0))
                .horarioCierre(LocalTime.of(18, 0))
                .esCasaMatriz(false)
                .domicilio(domicilio2)
                .build();

        // Crear Sucursales Para Cordoba

        Sucursal sucursal3 = Sucursal.builder()
                .id(3L)
                .nombre("Sucursal 3")
                .horarioApertura(LocalTime.of(9, 0))
                .horarioCierre(LocalTime.of(18, 0))
                .esCasaMatriz(true)
                .domicilio(domicilio3)
                .build();

        Sucursal sucursal4 = Sucursal.builder()
                .id(4L)
                .nombre("Sucursal 4")
                .horarioApertura(LocalTime.of(9, 0))
                .horarioCierre(LocalTime.of(18, 0))
                .esCasaMatriz(false)
                .domicilio(domicilio4)
                .build();

        // Crear empresas y asociar sucursales
        Empresa empresa1 = Empresa.builder()
                .nombre("Empresa 1")
                .razonSocial("Razon Social 1")
                .cuil(12345678901L)
                .sucursales(new HashSet<>(Set.of(sucursal1, sucursal2)))
                .build();

        Empresa empresa2 = Empresa.builder()
                .nombre("Empresa 2")
                .razonSocial("Razon Social 2")
                .cuil(22225678901L)
                .sucursales(new HashSet<>(Set.of(sucursal3, sucursal4)))
                .build();

        // Asignar empresa a sucursales
        sucursal1.setEmpresa(empresa1);
        sucursal2.setEmpresa(empresa1);
        sucursal3.setEmpresa(empresa2);
        sucursal4.setEmpresa(empresa2);

        // Guardar empresas en el repositorio
        empresaRepository.save(empresa1);
        empresaRepository.save(empresa2);

        // Mostrar todas las empresas
        System.out.println("Todas las empresas:");
        List<Empresa> todasLasEmpresas = empresaRepository.findAll();
        todasLasEmpresas.forEach(System.out::println);

        // Buscar empresa por ID
        Optional<Empresa> empresaEncontrada = empresaRepository.findById(1L);
        empresaEncontrada.ifPresent(e -> System.out.println("Empresa encontrada por ID 1: " + e));

        // Buscar empresa por nombre
        List<Empresa> empresasPorNombre = empresaRepository.genericFindByField("nombre", "Empresa 1");
        System.out.println("Empresas con nombre 'Empresa 1':");
        empresasPorNombre.forEach(System.out::println);

        // Actualizar empresa por ID
        Empresa empresaActualizada = Empresa.builder()
                .id(1L)
                .nombre("Empresa 1 Actualizada")
                .razonSocial("Razon Social 1 Actualizada")
                .cuil(12345678901L)
                .sucursales(empresa1.getSucursales())
                .build();

        empresaRepository.genericUpdate(1L, empresaActualizada);
        Optional<Empresa> empresaVerificada = empresaRepository.findById(1L);
        empresaVerificada.ifPresent(e -> System.out.println("Empresa después de la actualización: " + e));

        // Eliminar empresa por ID
        empresaRepository.genericDelete(1L);
        Optional<Empresa> empresaEliminada = empresaRepository.findById(1L);
        if (empresaEliminada.isEmpty()) {
            System.out.println("La empresa con ID 1 ha sido eliminada.");
        }

        // Mostrar todas las empresas restantes
        System.out.println("Todas las empresas después de la eliminación:");
        List<Empresa> empresasRestantes = empresaRepository.findAll();
        empresasRestantes.forEach(System.out::println);
        System.out.println("--------------Mostrar las sucursales de una empresa determinada");
        // Mostrar las sucursales de una empresa deerminada
        Optional<Empresa> empresa = empresaRepository.findById(2L);
        if (empresa.isPresent()) {
            System.out.println("Sucursales de la empresa con ID " + ":");
            Set<Sucursal> sucursales = empresa.get().getSucursales();
            sucursales.forEach(System.out::println);
        } else {
            System.out.println("Empresa con ID " + " no encontrada.");
        }

        // Inicializar repositorios
        InMemoryRepository<Categoria> categoriaRepository = new InMemoryRepository<>();
        InMemoryRepository<ArticuloInsumo> articuloInsumoRepository = new InMemoryRepository<>();
        InMemoryRepository<ArticuloManufacturado> articuloManufacturadoRepository = new InMemoryRepository<>();
        InMemoryRepository<UnidadMedida> unidadMedidaRepository = new InMemoryRepository<>();

        // Crear categorías
        Categoria pizzas = Categoria.builder().denominacion("Pizzas").esInsumo(false).build();
        Categoria sandwiches = Categoria.builder().denominacion("Sandwiches").esInsumo(false).build();
        Categoria bebidas = Categoria.builder().denominacion("Bebidas").esInsumo(false).build();
        Categoria insumos = Categoria.builder().denominacion("Insumos").esInsumo(true).build();

        categoriaRepository.save(pizzas);
        categoriaRepository.save(sandwiches);
        categoriaRepository.save(bebidas);
        categoriaRepository.save(insumos);

        // Crear unidades de medida
        UnidadMedida kg = UnidadMedida.builder().denominacion("Kg").build();
        UnidadMedida litro = UnidadMedida.builder().denominacion("Litro").build();
        UnidadMedida gramos = UnidadMedida.builder().denominacion("Gramos").build();

        unidadMedidaRepository.save(kg);
        unidadMedidaRepository.save(litro);
        unidadMedidaRepository.save(gramos);

        // Crear artículos insumos
        ArticuloInsumo sal = ArticuloInsumo.builder()
                .denominacion("Sal")
                .precioCompra(1.0)
                .stockActual(100)
                .stockMinimo(10)
                .stockMaximo(200)
                .esParaElaborar(true)
                .unidadMedida(gramos)
                .categoria(insumos)
                .build();

        ArticuloInsumo harina = ArticuloInsumo.builder()
                .denominacion("Harina")
                .precioCompra(0.5)
                .stockActual(50)
                .stockMinimo(5)
                .stockMaximo(100)
                .esParaElaborar(true)
                .unidadMedida(kg)
                .categoria(insumos)
                .build();

        ArticuloInsumo aceite = ArticuloInsumo.builder()
                .denominacion("Aceite")
                .precioCompra(3.0)
                .stockActual(30)
                .stockMinimo(3)
                .stockMaximo(60)
                .esParaElaborar(true)
                .unidadMedida(litro)
                .categoria(insumos)
                .build();

        ArticuloInsumo carne = ArticuloInsumo.builder()
                .denominacion("Carne")
                .precioCompra(5.0)
                .stockActual(20)
                .stockMinimo(2)
                .stockMaximo(40)
                .esParaElaborar(true)
                .unidadMedida(kg)
                .categoria(insumos)
                .build();

        articuloInsumoRepository.save(sal);
        articuloInsumoRepository.save(harina);
        articuloInsumoRepository.save(aceite);
        articuloInsumoRepository.save(carne);

        // Crear imágenes para los artículos
        ImagenArticulo img1 = ImagenArticulo.builder().
                name("HawaianaPizza1").url("http://example.com/pizza1").build();
        ImagenArticulo img2 = ImagenArticulo.builder().name("HawaianaPizza2").url("http://example.com/pizza2").build();
        ImagenArticulo img3 = ImagenArticulo.builder().name("HawaianaPizza3").url("http://example.com/pizza3").build();
        ImagenArticulo img4 = ImagenArticulo.builder().name("LomoCompletoLomo1").url("http://example.com/lomo1").build();
        ImagenArticulo img5 = ImagenArticulo.builder().name("LomoCompletoLomo2").url("http://example.com/lomo2").build();
        ImagenArticulo img6 = ImagenArticulo.builder().name("LomoCompletoLomo3").url("http://example.com/lomo3").build();

        // Crear detalles de artículos manufacturados
        ArticuloManufacturadoDetalle detalle1PizzaHawaina = ArticuloManufacturadoDetalle.builder()
                .cantidad(1)
                .articuloInsumo(sal)
                .build();

        ArticuloManufacturadoDetalle detalle2PizzaHawaina = ArticuloManufacturadoDetalle.builder()
                .cantidad(2)
                .articuloInsumo(harina)
                .build();

        ArticuloManufacturadoDetalle detalle3PizzaHawaina = ArticuloManufacturadoDetalle.builder()
                .cantidad(1)
                .articuloInsumo(aceite)
                .build();

        ArticuloManufacturadoDetalle detalle1LomoCompleto = ArticuloManufacturadoDetalle.builder()
                .cantidad(1)
                .articuloInsumo(sal)
                .build();

        ArticuloManufacturadoDetalle detalle2LomoCompleto = ArticuloManufacturadoDetalle.builder()
                .cantidad(1)
                .articuloInsumo(aceite)
                .build();

        ArticuloManufacturadoDetalle detalle3LomoCompleto = ArticuloManufacturadoDetalle.builder()
                .cantidad(2)
                .articuloInsumo(carne)
                .build();

        // Crear artículos manufacturados
        ArticuloManufacturado pizzaHawaina = ArticuloManufacturado.builder()
                .denominacion("Pizza Hawaina")
                .precioVenta(12.0)
                .descripcion("Pizza con piña y jamón")
                .tiempoEstimadoMinutos(20)
                .preparacion("Hornear por 20 minutos")
                .categoria(pizzas)
                .unidadMedida(kg)
                .imagenes(new HashSet<>(Set.of(img1, img2, img3)))
                .articuloManufacturadoDetalles(new HashSet<>(Set.of(detalle1PizzaHawaina, detalle2PizzaHawaina, detalle3PizzaHawaina)))
                .build();

        ArticuloManufacturado lomoCompleto = ArticuloManufacturado.builder()
                .denominacion("Lomo Completo")
                .precioVenta(15.0)
                .descripcion("Lomo completo con todos los ingredientes")
                .tiempoEstimadoMinutos(25)
                .preparacion("Cocinar a la parrilla por 25 minutos")
                .categoria(sandwiches)
                .unidadMedida(kg)
                .imagenes(new HashSet<>(Set.of(img4, img5, img6)))
                .articuloManufacturadoDetalles(new HashSet<>(Set.of(detalle1LomoCompleto, detalle2LomoCompleto, detalle3LomoCompleto)))
                .build();

        articuloManufacturadoRepository.save(pizzaHawaina);
        articuloManufacturadoRepository.save(lomoCompleto);

        // Mostrar todas las categorías
        System.out.println("Todas las categorías:");
        List<Categoria> todasLasCategorias = categoriaRepository.findAll();
        todasLasCategorias.forEach(System.out::println);

        // Mostrar todos los artículos insumos
        System.out.println("Todos los artículos insumos:");
        List<ArticuloInsumo> todosLosArticulosInsumos = articuloInsumoRepository.findAll();
        todosLosArticulosInsumos.forEach(System.out::println);

        // Mostrar todos los artículos manufacturados
        System.out.println("Todos los artículos manufacturados:");
        List<ArticuloManufacturado> todosLosArticulosManufacturados = articuloManufacturadoRepository.findAll();
        todosLosArticulosManufacturados.forEach(System.out::println);

        // Buscar un artículo manufacturado por ID
        Optional<ArticuloManufacturado> articuloEncontrado = articuloManufacturadoRepository.findById(1L);
        articuloEncontrado.ifPresent(a -> System.out.println("Artículo manufacturado encontrado por ID 1: " + a));

        // Actualizar un artículo manufacturado por ID
        ArticuloManufacturado pizzaHawainaActualizada = ArticuloManufacturado.builder()

                .id(1L)
                .denominacion("Pizza Hawaina Actualizada")
                .precioVenta(14.0)
                .descripcion("Pizza con piña, jamón y queso extra")
                .tiempoEstimadoMinutos(22)
                .preparacion("Hornear por 22 minutos")
                .categoria(pizzas)
                .unidadMedida(kg)
                .imagenes(new HashSet<>(Set.of(img1, img2, img3)))
                .articuloManufacturadoDetalles(new HashSet<>(Set.of(detalle1PizzaHawaina, detalle2PizzaHawaina, detalle3PizzaHawaina)))
                .build();

        articuloManufacturadoRepository.genericUpdate(1L, pizzaHawainaActualizada);
        Optional<ArticuloManufacturado> articuloVerificado = articuloManufacturadoRepository.findById(1L);
        articuloVerificado.ifPresent(a -> System.out.println("Artículo manufacturado después de la actualización: " + a));

        // Eliminar un artículo manufacturado por ID
        articuloManufacturadoRepository.genericDelete(1L);
        Optional<ArticuloManufacturado> articuloEliminado = articuloManufacturadoRepository.findById(1L);
        if (articuloEliminado.isEmpty()) {
            System.out.println("El artículo manufacturado con ID 1 ha sido eliminado.");
        }

        // Mostrar todos los artículos manufacturados restantes
        System.out.println("Todos los artículos manufacturados restantes:");
        todosLosArticulosManufacturados = articuloManufacturadoRepository.findAll();
        todosLosArticulosManufacturados.forEach(System.out::println);

        // ====== EJEMPLO DE USO (Opción 2): crear registros y consultar ======
        StockServiceB stockService = new StockServiceB();

// Registramos stock por sucursal (ej.: para el artículo 'aceite')
        stockService.registrar(new Stock(aceite, sucursal1, 30, 0));  // sucursal1 tiene 30
        stockService.registrar(new Stock(aceite, sucursal2, 20, 0));  // sucursal2 tiene 20

// Recalculamos y propagamos el total empresa (se escribe en cada fila como stockEmpresa)
        stockService.recalcularYPropagarStockEmpresa(aceite, empresa1);

// Consultas:
        System.out.println("Consultas de stock:");
        int sCentro = stockService.stockSucursal(aceite, sucursal1);
        int totalEmp = stockService.stockEmpresaPersistido(aceite, empresa1);

        System.out.println("Stock 'aceite' en sucursal1: " + sCentro);
        System.out.println("Stock 'aceite' total empresa1: " + totalEmp);



    }
}




