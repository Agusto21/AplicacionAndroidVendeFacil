import { z } from "zod";

export const productoSchema = z.object({
  nombre: z.string().min(1),
  categoria: z.string().min(1).optional(),
  precio: z.number().min(0),
  stock: z.number().int().min(0)
});