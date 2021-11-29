data class Tienda(val nombre: String, val clientes: List<Clientes>) {
    fun obtenerConjuntoDeClientes(): Set<Clientes> {
        return clientes.toSet()
    }

    fun obtenerCiudadesDeClientes(): Set<Ciudad> = clientes.map { it.ciudad }.toSet()

    fun obtenerClientesPor(ciudad: Ciudad): List<Clientes> = clientes.filter { it.ciudad == ciudad }
    fun checkTodosClientesSonDe(ciudad: Ciudad): Boolean = clientes.all { it.ciudad == ciudad }
    fun hayClientesDe(ciudad: Ciudad): Boolean = clientes.any { it.ciudad == ciudad }
    fun cuentaClientesDe(ciudad: Ciudad): Int = clientes.count { it.ciudad == ciudad }
    fun encuentraClienteDe(ciudad: Ciudad): Clientes? = clientes.find { it.ciudad == ciudad }
    fun obtenerClientesOrdenadosPorPedidos(): List<Clientes> = clientes.sortedByDescending { it.pedidos.size }
    fun obtenerClientesConPedidosSinEngregar(): Set<Clientes> =
        clientes.partition { cliente -> cliente.pedidos.all { it.estaEntregado > !it.estaEntregado } }.second.toSet()

    fun obtenerProductosPedidos(): Set<Producto> = clientes.flatMap { it.pedidos }.flatMap { it.productos }.toSet()
    fun obtenerNumeroVecesProductoPedido(producto: Producto): Int =
        clientes.flatMap { cliente -> cliente.pedidos.flatMap { it.productos } }.count { it == producto }

    fun agrupaClientesPorCiudad(): Map<Ciudad, List<Clientes>> = clientes.groupBy { it.ciudad }
}


data class Clientes(val nombre: String, val ciudad: Ciudad, val pedidos: List<Pedido>) {
    override fun toString() = "$nombre from ${ciudad.nombre}"
    fun obtenerProductosPedidos(): List<Producto> = pedidos.flatMap { it.productos }.toList()
    fun encuentraProductoMasCaro(): Producto? =
        pedidos.filter { it.estaEntregado }.flatMap { it.productos }.maxByOrNull { it.precio }

    fun dineroGastado(): Double = pedidos.flatMap { it.productos }.sumOf { it.precio }
}

data class Pedido(val productos: List<Producto>, val estaEntregado: Boolean)

data class Producto(val nombre: String, val precio: Double) {
    override fun toString() = "'$nombre' for $precio€"
}

data class Ciudad(val nombre: String) {
    override fun toString() = nombre
}


fun main() {
    val madrid = Ciudad("Madrid")
    val barcelona = Ciudad("Barcelona")
    val tomate = Producto("Tomate", 2.0)
    val pechuga = Producto("Pechuga de Pollo", 4.0)
    val lechuga = Producto("Lechuga", 3.5)
    val listaProducto1 = listOf(tomate)
    val listaProducto2 = listOf(pechuga)
    val listaProducto3 = listOf(lechuga)
    val pedido1 = Pedido(listaProducto1, true)
    val pedido2 = Pedido(listaProducto2, false)
    val pedido3 = Pedido(listaProducto3, true)
    val pedidosCliente1 = listOf(pedido2)
    val pedidosCliente2 = listOf(pedido1,pedido2)
    val pedidosCliente3 = listOf(pedido3)
    val cliente1 = Clientes("Luna", madrid, pedidosCliente1)
    val cliente2 = Clientes("Pedro", barcelona, pedidosCliente2)
    val cliente3 = Clientes("Yolanda", madrid, pedidosCliente3)
    val listaClientes = listOf(cliente1, cliente2, cliente3)
    val carrefour = Tienda("Carrefour", listaClientes)

    println("El conjunto de clientes es: ${carrefour.obtenerConjuntoDeClientes()}")
    println("Las ciudades de los clientes son: ${carrefour.obtenerCiudadesDeClientes()}")
    println("Según la ciudad ${madrid.toString()} los clientes son: ${carrefour.obtenerClientesPor(madrid)}")
    println("En la ciudad ${barcelona.toString()} hay un total de ${carrefour.cuentaClientesDe(barcelona)} clientes.")
    println("En la ciudad ${barcelona.toString()} vive ${carrefour.encuentraClienteDe(barcelona)}.")
    println("Lista de clientes por el número de pedidos: ${carrefour.obtenerClientesOrdenadosPorPedidos()}")
    println("Lista de clientes con pedidos sin entregar: ${carrefour.obtenerClientesConPedidosSinEngregar()}")
    println("Los productos pedidos por ${cliente2.toString()} son: ${cliente2.obtenerProductosPedidos()}")
    println("Los productos que fueron pedidos por al menos un cliente son: ${carrefour.obtenerProductosPedidos()}")
    println("El producto más caro de ${cliente2.toString()} es: ${cliente2.encuentraProductoMasCaro()}")
    println("El producto ${pechuga.toString()} se ha pedido ${carrefour.obtenerNumeroVecesProductoPedido(pechuga)} veces.")
    println(carrefour.agrupaClientesPorCiudad())
    println("El dinero gastado por ${cliente2.toString()} es ${cliente2.dineroGastado()}€.")
}
