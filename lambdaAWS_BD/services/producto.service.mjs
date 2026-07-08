import { randomUUID } from "crypto";
import * as repo from "../repositories/producto.repository.mjs";

export const create = async (usuario_id, data) => {
  const newProducto = { 
    usuario_id: usuario_id, 
    producto_id: randomUUID(), 
    ...data 
  };
  await repo.createProducto(newProducto);
  return newProducto;
};

export const getById = async (usuario_id, producto_id) => {
  const result = await repo.getProducto(usuario_id, producto_id);
  return result.Item;
};

export const list = async (usuario_id) => {
  const result = await repo.listProductos(usuario_id);
  return result.Items;
};

export const update = async (usuario_id, producto_id, body) => {
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

  const result = await repo.updateProducto(usuario_id, producto_id, updateExpression, names, values);
  return result.Attributes;
};

export const remove = async (usuario_id, producto_id) => {
  await repo.deleteProducto(usuario_id, producto_id);
};
