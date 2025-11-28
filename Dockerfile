# Stage 1: Build
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

# Copiar arquivos do Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Dar permissão de execução ao mvnw (importante no Windows)
RUN chmod +x mvnw

# Baixar dependências (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte
COPY src src

# Compilar aplicação (pular testes para build mais rápido)
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Criar usuário não-root por segurança
RUN groupadd -r spring && useradd -r -g spring spring

# Criar diretório de logs
RUN mkdir -p /app/logs

# Copiar apenas o JAR compilado do stage anterior
COPY --from=builder /app/target/*.jar app.jar

# Mudar ownership para usuário não-root
RUN chown -R spring:spring /app

# Usar usuário não-root
USER spring:spring

# Expor porta da aplicação
EXPOSE 8080

# Variáveis de ambiente padrão (podem ser sobrescritas)
ENV JAVA_OPTS="-Xmx512m -Xms256m" \
    SPRING_PROFILES_ACTIVE="prod"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/health || exit 1

# Executar aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
