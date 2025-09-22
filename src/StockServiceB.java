import java.util.*;
import java.util.stream.Collectors;

public class StockServiceB {
    private final Set<Stock> registros = new HashSet<>();

    public void registrar(Stock st) { registros.add(st); }

    /** Consulta: stock de un artículo en una sucursal */
    public int stockSucursal(Articulo a, Sucursal s) {
        return registros.stream()
                .filter(st -> st.getArticulo().equals(a) && st.getSucursal().equals(s))
                .mapToInt(Stock::getStockSucursal)
                .sum(); // normalmente 1 registro por (a,s)
    }

    /** Consulta: stockEmpresa “persistido” (leído del propio objeto Stock) */
    public int stockEmpresaPersistido(Articulo a, Empresa e) {
        // Traigo todos los Stock del artículo en las sucursales de esa empresa
        List<Stock> lista = registros.stream()
                .filter(st -> st.getArticulo().equals(a) && st.getSucursal().getEmpresa().equals(e))
                .collect(Collectors.toList());

        if (lista.isEmpty()) return 0;

        // Verifico consistencia: todos deberían tener el mismo stockEmpresa
        int esperado = lista.get(0).getStockEmpresa();
        int finalEsperado = esperado;
        boolean consistente = lista.stream().allMatch(st -> st.getStockEmpresa() == finalEsperado);
        if (!consistente) {
            // Si hay desfasajes, lo recalculo y propago
            esperado = recalcularYPropagarStockEmpresa(a, e);
        }
        return esperado;
    }

    /** Operación segura: setear stockSucursal y recalcular + propagar stockEmpresa */
    public void setStockSucursal(Articulo a, Sucursal s, int cantidad) {
        Stock st = registros.stream()
                .filter(x -> x.getArticulo().equals(a) && x.getSucursal().equals(s))
                .findFirst()
                .orElseGet(() -> {
                    Stock nuevo = new Stock();
                    registros.add(nuevo);
                    return nuevo;
                });
        st.setStockSucursal(Math.max(0, cantidad));
        recalcularYPropagarStockEmpresa(a, s.getEmpresa());
    }

    /** Movimiento entre sucursales (opcional y útil) */
    public void moverEntreSucursales(Articulo a, Sucursal origen, Sucursal destino, int cantidad) {
        if (cantidad <= 0) return;
        setStockSucursal(a, origen, Math.max(0, stockSucursal(a, origen) - cantidad));
        setStockSucursal(a, destino, stockSucursal(a, destino) + cantidad);
        // setStockSucursal ya recalcula y propaga
    }

    /** Recalcula el total empresa para el artículo y lo escribe en TODAS las filas (a, sucursal de esa empresa) */
    public int recalcularYPropagarStockEmpresa(Articulo a, Empresa e) {
        int total = registros.stream()
                .filter(st -> st.getArticulo().equals(a) && st.getSucursal().getEmpresa().equals(e))
                .mapToInt(Stock::getStockSucursal)
                .sum();

        registros.stream()
                .filter(st -> st.getArticulo().equals(a) && st.getSucursal().getEmpresa().equals(e))
                .forEach(st -> st.setStockEmpresa(total));

        return total;
    }

    /** Útil para informes: listado sucursal → cantidad */
    public Map<Sucursal, Integer> listarPorArticuloEnEmpresa(Articulo a, Empresa e) {
        return registros.stream()
                .filter(st -> st.getArticulo().equals(a) && st.getSucursal().getEmpresa().equals(e))
                .collect(Collectors.toMap(Stock::getSucursal, Stock::getStockSucursal));
    }
}
