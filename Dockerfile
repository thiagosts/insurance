# Etapa 1: Build da aplicação
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Copia o pom.xml e baixa dependências (cache para build mais rápido)
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copia o código-fonte e roda o build
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final (runtime) baseada em Java 17 Slim
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia apenas o JAR gerado na etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Expondo a porta (opcional, útil para documentação)
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java","-jar","app.jar"]