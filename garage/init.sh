#!/bin/bash

set -e

echo "Esperando Garage iniciar..."

until docker exec garage /garage status >/dev/null 2>&1
do
  sleep 2
done

echo "Garage disponível."

STATUS=$(docker exec garage /garage status)

if echo "$STATUS" | grep -q "NO ROLE ASSIGNED"; then

  NODE_ID=$(echo "$STATUS" \
    | grep "NO ROLE ASSIGNED" \
    | awk '{print $1}')

  echo "Configurando layout..."

  docker exec garage /garage layout assign \
    -z dc1 \
    -c 10G \
    "$NODE_ID"

  docker exec garage /garage layout apply --version 1
fi

echo "Criando bucket..."

docker exec garage /garage bucket create touchfy-uploads || true

echo "Importando key..."

docker exec garage /garage key import --yes \
  GKb31da6b5db60b2d3f566e8f7 \
  d33618b7c7922a30373b8051258393fabf6a805fa16caa9c9dd49e4b828513a7 || true

echo "Renomeando Key..."

docker exec garage /garage key rename \
  GKb31da6b5db60b2d3f566e8f7 touchfy-key || true

echo "Permissões..."

docker exec garage /garage bucket allow \
  --read \
  --write \
  --owner \
  touchfy-uploads \
  --key GKb31da6b5db60b2d3f566e8f7 || true

echo "Garage configurado."