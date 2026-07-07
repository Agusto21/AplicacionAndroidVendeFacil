import { randomUUID } from "crypto";
import * as repo from "../repositories/venta.repository.mjs";

export const create = async (usuario_id, data) => {
  const newVenta = { 
    usuario_id: usuario_id, 
    venta_id: randomUUID(),
    fecha: new Date().toISOString(), // Agregamos la fecha automáticamente
    ...data 
  };
  await repo.createVenta(newVenta);
  return newVenta;
};

export const getById = async (usuario_id, venta_id) => {
  const result = await repo.getVenta(usuario_id, venta_id);
  return result.Item;
};

export const list = async (usuario_id) => {
  const result = await repo.listVentas(usuario_id);
  return result.Items;
};

export const update = async (usuario_id, venta_id, body) => {
  let updateExpression = "set ";
  let names = {};
  let values = {};

  const keys = Object.keys(body);

  keys.forEach((key, index) => {
    updateExpression += `#${key} = :${key}`;
    if (index < keys.length - 1) updateExpression += ", ";
    names[`#${key}`] = key;
    values[`:${key}`] = body[key];
  });

  const result = await repo.updateVenta(usuario_id, venta_id, updateExpression, names, values);
  return result.Attributes;
};

export const remove = async (usuario_id, venta_id) => {
  await repo.deleteVenta(usuario_id, venta_id);
};