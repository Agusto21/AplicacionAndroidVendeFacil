import { PutCommand, GetCommand, QueryCommand, UpdateCommand, DeleteCommand } from "@aws-sdk/lib-dynamodb";
import { ddb } from "../config/dynamo.mjs";
  
const TABLE = process.env.PRODUCTOS_TABLE;

export const createProducto = (item) =>
  ddb.send(new PutCommand({
    TableName: TABLE,
    Item: item,
  }));

export const getProducto = (usuario_id, producto_id) =>
  ddb.send(new GetCommand({
    TableName: TABLE,
    Key: { usuario_id, producto_id }, 
  }));


export const listProductos = (usuario_id) =>
  ddb.send(new QueryCommand({
    TableName: TABLE,
    KeyConditionExpression: "usuario_id = :uid",
    ExpressionAttributeValues: {
      ":uid": usuario_id
    }
  }));

  export const updateProducto = (usuario_id, producto_id, updateExpression, names, values) =>
    ddb.send(new UpdateCommand({
      TableName: TABLE,
      Key: { usuario_id, producto_id }, 
      UpdateExpression: updateExpression,
      ExpressionAttributeNames: names,
      ExpressionAttributeValues: values,
      ConditionExpression: "attribute_exists(producto_id)", 
      ReturnValues: "ALL_NEW",
    }));
  

  export const deleteProducto = (usuario_id, producto_id) =>
    ddb.send(new DeleteCommand({
      TableName: TABLE,
      Key: { usuario_id, producto_id },
      ConditionExpression: "attribute_exists(producto_id)"
    }));
