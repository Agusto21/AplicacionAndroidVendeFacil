import { z } from "zod";

export const usuarioSchema = z.object({
  correo: z.string().email(),
  nombre: z.string().min(1),
  negocio: z.string().min(0),
  telefono: z.string().min(1),
  contrasena: z.string().min(1)
});