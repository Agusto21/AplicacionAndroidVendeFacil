import { randomUUID } from "crypto";
import * as repo from "../repositories/usuario.repository.mjs";

export const create = async (data) => {
  const newUsuario = { 
    usuario_id: randomUUID(), 
    ...data 
  };
  await repo.createUsuario(newUsuario);
  return newUsuario;
};

export const getById = async (usuario_id) => {
  const result = await repo.getUsuario(usuario_id);
  return result.Item;
};

export const login = async (correo, contrasena) => {
  const result = await repo.getUsuarioByCorreo(correo);
  
  if (!result.Items || result.Items.length === 0) {
    throw new Error("Usuario no encontrado");
  }

  const usuario = result.Items[0];


  if (usuario.contrasena !== contrasena) {
    throw new Error("Contraseña incorrecta");
  }

  return { 
    usuario_id: usuario.usuario_id,
    nombre: usuario.nombre,
    negocio: usuario.negocio,
    correo: usuario.correo,
    telefono: usuario.telefono
  };
};

export const update = async (usuario_id, body) => {
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

  const result = await repo.updateUsuario(usuario_id, updateExpression, names, values);
  return result.Attributes;
};

export const remove = async (usuario_id) => {
  await repo.deleteUsuario(usuario_id);
};
