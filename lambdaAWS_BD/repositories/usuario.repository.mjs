import { PutCommand, GetCommand, ScanCommand, UpdateCommand, DeleteCommand } from "@aws-sdk/lib-dynamodb";
import { ddb } from "../config/dynamo.mjs";
  
const TABLE = process.env.USUARIOS_TABLE;

export const createUsuario = (item) =>
  ddb.send(new PutCommand({
    TableName: TABLE,
    Item: item,
  }));

export const getUsuario = (usuario_id) =>
  ddb.send(new GetCommand({
    TableName: TABLE,
    Key: { usuario_id }, // Solo PK, sin SK
  }));

  export const getUsuarioByCorreo = (correo) =>
    ddb.send(new ScanCommand({
      TableName: TABLE,
      FilterExpression: "correo = :c",
      ExpressionAttributeValues: { ":c": correo }
    }));

  export const updateUsuario = (usuario_id, updateExpression, names, values) =>
    ddb.send(new UpdateCommand({
      TableName: TABLE,
      Key: { usuario_id }, // Solo usamos PK
      UpdateExpression: updateExpression,
      ExpressionAttributeNames: names,
      ExpressionAttributeValues: values,
      ConditionExpression: "attribute_exists(usuario_id)",
      ReturnValues: "ALL_NEW",
    }));
  
  export const deleteUsuario = (usuario_id) =>
    ddb.send(new DeleteCommand({
      TableName: TABLE,
      Key: { usuario_id },
      ConditionExpression: "attribute_exists(usuario_id)"
    }));