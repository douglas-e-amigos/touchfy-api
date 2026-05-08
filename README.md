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

### PgAdmin

A aplicação possui um cliente web para o banco de dados, que pode ser acessado no endpoint:

````
http://localhost:5050
````

As credenciais são:

````
Email: admin@touchfy.com
Senha: 123456
````

## Testes

Os testes unitários podem ser rodados com o comando abaixo:

````commandline
docker exec -it touchfy_app ./mvnw test
````

No momento de elaborar os testes, não precisa testar todas as classes do projeto. Obrigatoriamente, teste: casos de uso, models, adapters e classes auxiliares/helpers.

Coisas como: controllers, interfaces ou records não devem ser testadas.

## Swagger

Todos os endpoints e DTOs devem estar documentados usando as anotações do Swagger para java. Isso permite com que 
cada endpoint seja acessável através do endpoint a baixo:

````
swagger-ui/index.html
````

### Acessando a UI

Com os containers rodando, acesse:

````
http://localhost:3909
````

### Criando um bucket

1. Acesse a UI do Garage
2. Vá na seção **Keys** e crie uma chave de acesso — guarde o `Access Key ID` e o `Secret Access Key`
3. Vá na seção **Buckets** e clique em **Create bucket**
4. Dê o nome `touchfy-uploads` ao bucket
5. Na página do bucket, vá em **Allow key** e adicione a chave criada com permissão de leitura e escrita

### Configurando as credenciais

Após criar a chave, adicione as credenciais no `application.yaml`:

```yaml
storage:
  endpoint: http://garage:3900
  access-key: SUA_ACCESS_KEY
  secret-key: SUA_SECRET_KEY
  bucket-name: touchfy-uploads
  bucket-url: http://garage:3900/touchfy-uploads
```

### Deletando um bucket

O Garage não permite deletar um bucket com arquivos. Esvazie o bucket primeiro pela UI e depois clique em **Delete** na página do bucket.

## Referências

O repositório possui uma aba de wiki, onde alguns diretrizes de desenvolvimento estão melhor detalhadas. Leia os tópicos nessa aba.
