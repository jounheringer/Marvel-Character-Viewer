# Marvel Character Viewer


Este é um aplicativo destinado ao **teste de uso da API da Marvel** ([Marvel Developer Portal](https://developer.marvel.com)).  
Com ele você consegue:

- Selecionar o seu personagem da Marvel favorito
- Listar todos os quadrinhos relacionados ao personagem
- Visualizar também os criadores de cada quadrinho

## Descrição Técnica

- **UI:** Compose
- **Dados:** PagingSource para paginação, DataStore para armazenamento de configurações
- **Navegação:** Intents para transitar entre Activities
- **API:** Retrofit para chamadas à API da Marvel
- **Injeção de Dependências:** Dagger Hilt
- **Teste:** Unit Tests focados no ViewModel
- **Logging:** Implementado pelo `LogCat`
## Requisitos

Ao clonar o repositório, é preciso inserir suas chaves **pública** e **privada** da API da Marvel no arquivo `local.properties`:

```propertie
MARVEL_PUBLIC_KEY=*******
MARVEL_PRIVATE_KEY=*******
```
