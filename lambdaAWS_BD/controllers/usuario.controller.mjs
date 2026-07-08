import * as service from "../services/usuario.service.mjs";
import { usuarioSchema } from "../validators/usuario.schema.mjs";
import { ok, created, badRequest, notFound, noContent } from "../utils/response.mjs";

export const createUsuario = async (event) => {
  const body = JSON.parse(event.body);
  const parsed = usuarioSchema.safeParse(body);

  if (!parsed.success) return badRequest(parsed.error);

  const usuario = await service.create(parsed.data);
  return created({ message: "Registro exitoso", data: usuario });
};

export const getUsuario = async (usuarioId) => {
  const usuario = await service.getById(usuarioId);
  if (!usuario) return notFound("Usuario no encontrado");
  return ok(usuario);
};

export const login = async (event) => {
  try {
    const { correo, contrasena } = JSON.parse(event.body);
    
    if (!correo || !contrasena) {
      return badRequest("Correo y contraseña son requeridos");
    }

    const authData = await service.login(correo, contrasena);
    return ok({ message: "Login exitoso", data: authData });

  } catch (error) {
    return { statusCode: 401, body: JSON.stringify({ message: error.message }) };
  }
};

export const updateUsuario = async (event) => {
  
  const usuarioId = event.pathParameters?.id;
  if (!usuarioId) return badRequest("Falta el ID del usuario");

  const body = JSON.parse(event.body);
  const usuarioActualizado = await service.update(usuarioId, body);
  return ok(usuarioActualizado);
};

export const deleteUsuario = async (event) => {
  const usuarioId = event.pathParameters?.id;
  if (!usuarioId) return badRequest("Falta el ID del usuario");

  await service.remove(usuarioId);
  return noContent();
};
