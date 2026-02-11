# Configuração do Git - Take Water

## 📋 Arquivos Incluídos no Repositório

### ✅ Código Fonte
- Todo código Kotlin (`.kt`)
- Arquivos de recursos (`.xml`)
- Gradle build files
- ProGuard rules

### ✅ Documentação Pública
- `README.md` - Documentação principal do projeto
- `PRIVACY_POLICY.md` - Política de privacidade
- `LICENSE` - Licença MIT

### ✅ Configuração
- `.gitignore` - Arquivos ignorados
- `local.properties.example` - Template de configuração

### ❌ Arquivos Excluídos (Privados)
- `local.properties` - Configurações locais e senhas
- `*.jks`, `*.keystore` - Keystores de assinatura
- `release-keystore.jks` - Keystore de release
- `keystore.properties` - Propriedades do keystore
- Arquivos de documentação interna:
  - `LAUNCH_SUMMARY.md`
  - `PLAY_STORE_GUIDE.md`
  - `RELEASE_CHECKLIST.md`
  - `QUICK_COMMANDS.md`
  - `README_LAUNCH.md`
  - `ARCHITECTURE.md`
  - `store-listing/` - Textos da loja
  - `scripts/` - Scripts de build

## 🚀 Primeiro Commit

### 1. Inicializar Git
```bash
git init
```

### 2. Adicionar Arquivos
```bash
# Adicionar todos os arquivos (respeitando .gitignore)
git add .

# Verificar o que será commitado
git status
```

### 3. Verificar Arquivos Sensíveis
**IMPORTANTE:** Antes de commitar, verifique que estes arquivos NÃO estão sendo adicionados:
```bash
# Estes NÃO devem aparecer no git status:
# - local.properties
# - release-keystore.jks
# - *.keystore
# - keystore.properties
# - LAUNCH_SUMMARY.md
# - PLAY_STORE_GUIDE.md
# - etc.
```

### 4. Fazer Commit
```bash
git commit -m "Initial commit: Take Water app"
```

### 5. Adicionar Remote (GitHub/GitLab)
```bash
# GitHub
git remote add origin https://github.com/seu-usuario/take-water.git

# Ou GitLab
git remote add origin https://gitlab.com/seu-usuario/take-water.git
```

### 6. Push
```bash
git branch -M main
git push -u origin main
```

## 🔐 Segurança

### Arquivos que NUNCA devem ser commitados
1. **Keystores** (`.jks`, `.keystore`)
   - Contém chaves de assinatura do app
   - Se vazarem, qualquer um pode assinar apps como você

2. **local.properties**
   - Contém senhas do keystore
   - Contém caminhos locais

3. **Senhas e Credenciais**
   - Nunca commite senhas em texto plano
   - Use variáveis de ambiente ou arquivos locais

### Verificar Antes de Push
```bash
# Ver o que será enviado
git diff origin/main

# Verificar histórico
git log --oneline

# Verificar arquivos rastreados
git ls-files
```

## 🔄 Workflow Recomendado

### Branches
```bash
# Criar branch para feature
git checkout -b feature/nova-funcionalidade

# Trabalhar na feature
git add .
git commit -m "feat: adiciona nova funcionalidade"

# Voltar para main e fazer merge
git checkout main
git merge feature/nova-funcionalidade

# Push
git push origin main
```

### Commits Semânticos
```bash
# Formato: tipo(escopo): mensagem

# Tipos:
feat: Nova funcionalidade
fix: Correção de bug
docs: Documentação
style: Formatação
refactor: Refatoração
test: Testes
chore: Manutenção

# Exemplos:
git commit -m "feat(home): adiciona animação no copo"
git commit -m "fix(notifications): corrige horário dos lembretes"
git commit -m "docs: atualiza README com instruções"
```

## 📦 Releases

### Criar Tag de Versão
```bash
# Tag anotada
git tag -a v1.0.0 -m "Release 1.0.0"

# Push da tag
git push origin v1.0.0

# Listar tags
git tag -l
```

### GitHub Release
1. Acesse: https://github.com/seu-usuario/take-water/releases
2. Clique em "Create a new release"
3. Selecione a tag (v1.0.0)
4. Adicione release notes
5. Anexe o AAB (opcional, mas não recomendado por segurança)
6. Publique

## 🔍 Verificação de Segurança

### Antes de Tornar Público
```bash
# Verificar histórico completo
git log --all --full-history --pretty=format:"%H %s" | grep -i "password\|key\|secret"

# Verificar arquivos rastreados
git ls-files | grep -E "\.jks$|\.keystore$|local\.properties$"

# Se encontrar algo sensível, limpar histórico:
# (CUIDADO: reescreve histórico!)
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch caminho/arquivo-sensivel" \
  --prune-empty --tag-name-filter cat -- --all
```

### Ferramentas de Verificação
```bash
# Instalar git-secrets (previne commits de senhas)
brew install git-secrets  # macOS
# ou
apt-get install git-secrets  # Linux

# Configurar
git secrets --install
git secrets --register-aws
```

## 📝 .gitignore Explicado

```gitignore
# Keystores - CRÍTICO!
*.jks
*.keystore
release-keystore.jks

# Configurações locais
local.properties
keystore.properties

# Documentação privada
LAUNCH_SUMMARY.md
PLAY_STORE_GUIDE.md
RELEASE_CHECKLIST.md
# ... etc

# Build files
build/
*.apk
*.aab

# IDE
.idea/
*.iml
```

## 🤝 Colaboração

### Para Colaboradores
1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/take-water.git
cd take-water
```

2. Copie o template de configuração
```bash
cp local.properties.example local.properties
```

3. Configure seu próprio keystore para testes
```bash
cd scripts
./generate-keystore.sh
```

4. Nunca commite seu keystore ou local.properties!

### Pull Requests
1. Fork o repositório
2. Crie uma branch
3. Faça suas alterações
4. Teste localmente
5. Abra Pull Request
6. Aguarde revisão

## ⚠️ Problemas Comuns

### "Arquivo sensível foi commitado"
```bash
# Remover do último commit (se ainda não fez push)
git rm --cached arquivo-sensivel
git commit --amend

# Se já fez push, precisa reescrever histórico
# (Cuidado: afeta todos os colaboradores!)
```

### "Esqueci de adicionar ao .gitignore"
```bash
# Adicionar ao .gitignore
echo "arquivo-esquecido" >> .gitignore

# Remover do Git (mas manter localmente)
git rm --cached arquivo-esquecido

# Commit
git add .gitignore
git commit -m "chore: adiciona arquivo ao gitignore"
```

## 📚 Recursos

- [Git Documentation](https://git-scm.com/doc)
- [GitHub Guides](https://guides.github.com/)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Git Secrets](https://github.com/awslabs/git-secrets)

---

**Lembre-se:** Nunca commite senhas, keystores ou informações sensíveis! 🔐
