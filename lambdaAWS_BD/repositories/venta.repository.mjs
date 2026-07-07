import { PutCommand, GetCommand, QueryCommand, UpdateCommand, DeleteCommand } from "@aws-sdk/lib-dynamodb";
import { ddb } from "../config/dynamo.mjs";
  
const TABLE = process.env.VENTAS_TABLE;

export const createVenta = (item) =>
  ddb.send(new PutCommand({
    TableName: TABLE,
    Item: item,
  }));

export const getVenta = (usuario_id, venta_id) =>
  ddb.send(new GetCommand({
    TableName: TABLE,
    Key: { usuario_id, venta_id },
  }));

export const listVentas = (usuario_id) =>
  ddb.send(new QueryCommand({
    TableName: TABLE,
    KeyConditionExpression: "usuario_id = :uid",
    ExpressionAttributeValues: {
      ":uid": usuario_id
    }
  }));

export const updateVenta = (usuario_id, venta_id, updateExpression, names, values) =>
  ddb.send(new UpdateCommand({
    TableName: TABLE,
    Key: { usuario_id, venta_id },
    UpdateExpression: updateExpression,
    ExpressionAttributeNames: names,
    ExpressionAttributeValues: values,
    ConditionExpression: "attribute_exists(venta_id)",
    ReturnValues: "ALL_NEW",
  }));

export const deleteVenta = (usuario_id, venta_id) =>
  ddb.send(new DeleteCommand({
    TableName: TABLE,
    Key: { usuario_id, venta_id },
    ConditionExpression: "attribute_exists(venta_id)"
  }));