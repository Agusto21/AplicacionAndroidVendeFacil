import { z } from "zod";

export const ventaSchema = z.object({
  cliente: z.string().min(1),
  metodopago: z.string().min(1),
  total: z.number().min(0),
  detalle: z.array(
    z.object({
      producto_id: z.string().min(1),
      producto: z.string().min(1),
      cantidad: z.number().int().min(1),
      subtotal: z.number().min(0)
    })
  ).min(1) 
});
