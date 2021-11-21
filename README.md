# IP-API

IP-API es una API Rest que brinda información acerca del país de una dirección de IP

## Endpoints

### Endpoint para obtener información acerca del país de una dirección de IP.

```bash
GET /ip/{ip}
```

Response ejemplo:

```bash
{
    "countryName": "United States",
    "countryIsoCode": "US",
    "exchange": {
        "currency": "USD",
        "rate": 1.12803,
        "baseCurrency": "EUR"
    }
}
```

### Endpoint para bloquear una dirección de IP.

```bash
POST /ban/{ip}
```

Response ejemplo:

```bash
{
    "status": "BANNED",
    "message": "Ip 47.246.137.166 has been banned"
}
```

## Instalación

- Clonar este repositorio.
- Crear una imagen Docker utilizando el Dockerfile provisto.

```bash
docker run --name ip-api -d -p 8080:8080 ip-api:1.0.0
```

- Ejecutar la API ip-api desde la imagen Docker.

```bash
docker run --name ip-api -d -p 8080:8080 ip-api:1.0.0
```

## Documentación

- Ejecutar_en_Contenedor_Docker.txt
- Pruebas_Funcionales.pdf

La documentación se encuentra en la carpeta /docs
