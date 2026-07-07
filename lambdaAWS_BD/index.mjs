import * as productoController from "./controllers/producto.controller.mjs";
import * as usuarioController from "./controllers/usuario.controller.mjs";
import * as ventaController from "./controllers/venta.controller.mjs";

export const handler = async (event) => {
  try {
    const method = event.requestContext.http.method;
    const path = event.rawPath;
    
    // Obtenemos el usuario_id de los headers (tu token o identificador)
    const usuarioId = event.headers?.['x-usuario-id']; 

    if (path === "/login" && method === "POST") {
      return usuarioController.login(event);
    }

    // --- RUTAS PARA USUARIOS ---
    if (path.startsWith("/usuarios")) {
      const parts = path.split('/');
      const id = parts.length > 2 ? parts[2] : null;

      if (method === "POST") return usuarioController.createUsuario(event);
      if (method === "GET" && id) return usuarioController.getUsuario(id);
      if (method === "PUT" && id) return usuarioController.updateUsuario(event);
      if (method === "DELETE" && id) return usuarioController.deleteUsuario(event);
    }

    // Validación global de multi-tenancy para el resto de rutas
    if (!usuarioId) {
      return { statusCode: 401, body: JSON.stringify({ message: "Falta x-usuario-id en headers" }) };
    }

    // --- RUTAS PARA PRODUCTOS ---
    if (path.startsWith("/productos")) {
      const parts = path.split('/');
      const id = parts.length > 2 ? parts[2] : null;

      if (method === "POST") return productoController.createProducto(usuarioId, event);
      if (method === "GET" && !id) return productoController.listProductos(usuarioId);
      if (method === "GET" && id) return productoController.getProducto(usuarioId, id);
      if (method === "PUT" && id) return productoController.updateProducto(usuarioId, id, event);
      if (method === "DELETE" && id) return productoController.deleteProducto(usuarioId, id);
    }

    // --- RUTAS PARA VENTAS ---
    if (path.startsWith("/ventas")) {
      const parts = path.split('/');
      const id = parts.length > 2 ? parts[2] : null;

      if (method === "POST") return ventaController.createVenta(usuarioId, event);
      if (method === "GET" && !id) return ventaController.listVentas(usuarioId);
      if (method === "GET" && id) return ventaController.getVenta(usuarioId, id);
      if (method === "PUT" && id) return ventaController.updateVenta(usuarioId, id, event);
      if (method === "DELETE" && id) return ventaController.deleteVenta(usuarioId, id);
    }

    return { statusCode: 404, body: JSON.stringify({ message: "Ruta no encontrada" }) };

  } catch (error) {
    console.error("Error Inesperado:", error);
    return { statusCode: 500, body: JSON.stringify({ message: "Error interno del servidor" }) };
  }
};