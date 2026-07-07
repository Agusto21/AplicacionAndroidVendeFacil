import * as service from "../services/venta.service.mjs";
import { ventaSchema } from "../validators/venta.schema.mjs";
import { ok, created, badRequest, notFound, noContent } from "../utils/response.mjs";

export const createVenta = async (usuarioId, event) => {
  const body = JSON.parse(event.body);
  const parsed = ventaSchema.safeParse(body);

  if (!parsed.success) return badRequest(parsed.error);

  const venta = await service.create(usuarioId, parsed.data);
  return created(venta);
};

export const getVenta = async (usuarioId, ventaId) => {
  const venta = await service.getById(usuarioId, ventaId);
  if (!venta) return notFound("Venta no encontrada");
  return ok(venta);
};

export const listVentas = async (usuarioId) => {
  const result = await service.list(usuarioId);
  return ok(result);
};

export const updateVenta = async (usuarioId, ventaId, event) => {
  const body = JSON.parse(event.body);
  const ventaActualizada = await service.update(usuarioId, ventaId, body);
  return ok(ventaActualizada);
};

export const deleteVenta = async (usuarioId, ventaId) => {
  await service.remove(usuarioId, ventaId);
  return noContent();
};