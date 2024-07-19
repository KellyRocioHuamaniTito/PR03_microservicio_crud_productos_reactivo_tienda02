package service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProdcutosServiceImpl implements ProductosService {

	private static List<Producto> productos = new ArrayList<>(
			List.of(new Producto(110, "Azucar", "Alimentación", 1.10, 20),
					new Producto(111, "Huevo", "Alimentación", 8.20, 15),
					new Producto(112, "Jabón", "Limpieza", 0.89, 30),
					new Producto(113, "Mesa", "Hogar", 125, 4),
					new Producto(114, "Refrigeradora", "Hogar", 950, 10),
					new Producto(115, "Huevos", "Alimentación", 2.20, 30),
					new Producto(116, "Esponja", "Limpieza", 1.40, 6),
					new Producto(117, "Detergente", "Limpieza", 8.7, 12)));

	@Override
	public Flux<Producto> catalogo() {
		return Flux.fromIterable(productos)// devuelve un Flux<Producto>
				.delayElements(Duration.ofSeconds(2));
	}

	@Override
	public Flux<Producto> productoCategoria(String categoria) {
		return catalogo()// Flux<Producto>
				.filter(p -> p.getCategoria().equals(categoria));
	}

	@Override
	public Mono<Producto> productoCodigo(int cod) {
		return catalogo()// Flux<Producto>
				.filter(p -> p.getCodProducto() == cod)// Flux<Producto>
				.next();// Mono<Producto>
		// .swichIfEmpty(Mono.just(new Producto()));// en caso de no encontarr ningun
		// objeto
	}

	@Override
	public Mono<Void> altaProducto(Producto producto) {
		return productoCodigo(producto.getCodProducto())//Mono<Producto>
		.switchIfEmpty(Mono.just(producto).map(p->{
			productos.add(producto);
			return p;
		}))//Mono<Producto>
		.then();//Mono<Void>


	}

	@Override
	public Mono<Producto> eliminarProducto(int cod) {
		return productoCodigo(cod)//Mono<Producto>
		.map(p->{//si es empty el map no se ejecuta
			productos.removeIf(pr->pr.getCodProducto()==cod);
			return p;
		});//Mono<Producto>
		//.switchEmpty(null); // En caso se devuelva un emty.
	}

	@Override
	public Mono<Producto> actualizarPrecio(int cod, double precio) {
		return productoCodigo(cod)//Mono<Producto>
		.map(p->{
			p.setPrecioUnitario(precio);
			return p;
		});
	}

}
