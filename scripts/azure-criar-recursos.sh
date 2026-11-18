RESOURCE_GROUP_NAME="rg-GLOBAL-mobile-rm561857"
WEBAPP_NAME="app-global-mobile-rm561859"
APP_SERVICE_PLAN="plan-API-mobile-rm561857"
LOCATION="brazilsouth"
RUNTIME="JAVA:21-java21"
USERNAME=
PASSWORD=
URL=


# Registrar os Serviços utilizados
az provider register --namespace Microsoft.Web
az provider register --namespace Microsoft.ServiceLinker

# Criação do Grupo de Recursos
az group create \
  --name $RESOURCE_GROUP_NAME \
  --location "$LOCATION"


# Criar o Plano de Serviço
az appservice plan create \
  --name $APP_SERVICE_PLAN \
  --resource-group $RESOURCE_GROUP_NAME \
  --location "$LOCATION" \
  --sku F1 \
  --is-linux


# Criar o Web App Java 21
az webapp create \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --plan "$APP_SERVICE_PLAN" \
  --runtime "$RUNTIME"

# Verificar o Web App
az webapp show \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --query "{Nome:name,Estado:state,URL:defaultHostName}" \
  --output table