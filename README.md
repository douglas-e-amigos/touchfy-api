# Touchfy API

## Start

Criar e subir container:

```commandline
docker compose up --build -d
```

## Banco de dados

O projeto utiliza o flyway para gerenciar as migrações. Uma migração é um arquivo sql com a sintaxe `V<versão>__<descrição>.sql`, que deve conter **somente** comandos **DDLs**.

Uma vez tendo criado uma migração, será preciso deletar o volume do container:

````commandline
docker down -v
````

Feito isso, rode o comando de start novamente.

## Testes

Os testes unitários podem ser rodados com o comando abaixo:

````commandline
docker exec -it touchfy_app ./mvnw test
````

No momento de elaborar os testes, não precisa testar todas as classes do projeto. Obrigatoriamente, teste: casos de uso, models, adapters e classes auxiliares/helpers.

Coisas como: controllers, interfaces ou records não devem ser testadas.

