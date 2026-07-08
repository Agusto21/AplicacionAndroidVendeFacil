import * as service from "../services/producto.service.mjs";
import { productoSchema } from "../validators/producto.schema.mjs";
import { ok, created, badRequest, notFound, noContent } from "../utils/response.mjs";

export const createProducto = async (usuarioId, event) => {
  const body = JSON.parse(event.body);
  const parsed = productoSchema.safeParse(body);

  if (!parsed.success) return badRequest(parsed.error);

  const producto = await service.create(usuarioId, parsed.data);
  return created(producto);
};

export const getProducto = async (usuarioId, productoId) => {
  const producto = await service.getById(usuarioId, productoId);
  if (!producto) return notFound("Producto no encontrado");
  return ok(producto);
};

export const listProductos = async (usuarioId) => {
  const result = await service.list(usuarioId);
  return ok(result);
};

export const updateProducto = async (usuarioId, productoId, event) => {
  const body = JSON.parse(event.body);
  
  const productoActualizado = await service.update(usuarioId, productoId, body);
  return ok(productoActualizado);
};


export const deleteProducto = async (usuarioId, productoId) => {
  await service.remove(usuarioId, productoId);
  return noContent();
};
