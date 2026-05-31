@echo off

chcp 65001 >nul

echo Esperando Garage iniciar...

:wait_garage
docker exec garage /garage status >nul 2>&1
if %errorlevel% neq 0 (
    timeout /t 2 /nobreak >nul
    goto wait_garage
)

echo Garage disponível.

set "NODE_ID="
for /f "tokens=1" %%a in ('docker exec garage /garage status 2^>nul ^| findstr "NO ROLE ASSIGNED"') do (
    set "NODE_ID=%%a"
)

if defined NODE_ID (
    echo Configurando layout...
    
    docker exec garage /garage layout assign -z dc1 -c 10G %NODE_ID%
    
    docker exec garage /garage layout apply --version 1
)

echo Criando bucket...
docker exec garage /garage bucket create touchfy-uploads 2>nul

echo Importando key...
docker exec garage /garage key import --yes GKb31da6b5db60b2d3f566e8f7 d33618b7c7922a30373b8051258393fabf6a805fa16caa9c9dd49e4b828513a7 2>nul

echo Renomeando Key...
docker exec garage /garage key rename GKb31da6b5db60b2d3f566e8f7 touchfy-key 2>nul

echo Permissões...
docker exec garage /garage bucket allow --read --write --owner touchfy-uploads --key GKb31da6b5db60b2d3f566e8f7 2>nul

echo Garage configurado.