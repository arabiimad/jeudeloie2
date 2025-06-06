# 🐦 Jeu de l’Oie – Java 22

[![Maven](https://img.shields.io/badge/build-Maven_3.9-blue)](https://maven.apache.org/)  [![Java](https://img.shields.io/badge/java-22-green)](https://openjdk.org/projects/jdk/22/)  [![License](https://img.shields.io/badge/license-MIT-lightgrey)](LICENSE)

> Implémentation complète du **Jeu de l’Oie** en Java, orientée pédagogie et bonnes pratiques : JPA/Hibernate, inversion de dépendance, tests unitaires (JUnit 5), couverture Jacoco, mutation testing PIT, analyse SonarQube, intégration continue GitLab/GitHub, et exécution multiback‑end (mémoire • JDBC • JPA).

---

## Sommaire

1. [Fonctionnalités](#fonctionnalités)
2. [Architecture](#architecture)
3. [Pré‑requis](#pré‑requis)
4. [Configuration](#configuration)
5. [Compilation & tests](#compilation--tests)
6. [Exécution](#exécution)
7. [Qualité de code](#qualité-de-code)
8. [CI/CD](#cicd)
9. [Licence](#licence)

---

## Fonctionnalités

* **Plateau paramétrable** (25 cases par défaut).
* Effets : avancée, recul, question, retour à la case départ…
* Choix du **moteur de persistance** à chaud via *configuration.properties* :
  `memory`, `memoryWithInit`, `jdbc`, `jpa` (Hibernate + HSQLDB).
* **DAO Factory** : inversion de dépendance, facile à étendre.
* **Tests couvrant > 60 %** des lignes + mutation score > 70 %.

---

## Architecture

```
org.example
├─ config/          # Singleton ConfigurationManager
├─ controller/      # GameController / GameSession
├─ data/            # POJO @Entity + convertisseurs
├─ model/           # business & technical règles
├─ stockage/        # DAO + factory (IM / JDBC / JPA)
└─ view/            # ConsoleView (MVC)
```

> Voir *docs/architecture.adoc* pour le diagramme complet.

---

## Pré‑requis

| Logiciel                   | Version min.      | Vérification       |
| -------------------------- | ----------------- | ------------------ |
| **JDK**                    | 21 (22 conseillé) | `javac -version`   |
| **Maven**                  | 3.9               | `mvn -v`           |
| **Docker** *(option JDBC)* | 24.x              | `docker --version` |

---

## Configuration

Le fichier `src/main/resources/configuration.properties` pilote le back‑end.

```properties
# Example (JPA)
dao_type=jpa             # memory|memoryWithInit|jdbc|jpa
initDatabase=false       # true = exécute le script schema
populateDatabase=false   # true = insère les questions par défaut

# JDBC only
# db_url=jdbc:postgresql://localhost:5432/oie
# db_username=postgres
# db_password=postgres
```

---

## Compilation & tests

```bash
# build complet + tests + rapports Jacoco & PIT
git clone https://github.com/<you>/jeudeloie.git
cd jeudeloie
mvn clean verify
```

* Rapport Jacoco : `target/site/jacoco/index.html`
* Rapport PIT : `target/pit-reports/index.html`

> Tests instables ? `mvn clean package -DskipTests`

---

## Exécution

### 1. Mode mémoire (données exemple)

```
# configuration.properties
 dao_type=memoryWithInit

mvn exec:java -Dexec.mainClass=org.example.EntryPoint
```

### 2. Mode JPA (HSQLDB in‑memory)

```
dao_type=jpa
mvn exec:java -Dexec.mainClass=org.example.EntryPoint
```

### 3. Mode JDBC (PostgreSQL Docker)

```
# lancer la BDD
docker run -d --rm --name oie -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:16
psql -h localhost -U postgres -c "CREATE DATABASE oie;"

# config
 dao_type=jdbc
 db_url=jdbc:postgresql://localhost:5432/oie
 db_username=postgres
 db_password=postgres
 initDatabase=true
 populateDatabase=true

mvn exec:java -Dexec.mainClass=org.example.EntryPoint
```

---

## Qualité de code

* **Jacoco** : >= 60 % lignes couvertes
* **PIT** : >= 70 % mutations tuées
* **SonarQube** : Quality Gate "A" (0 bug, 0 vulnérabilité)

Lancer manuellement :

```bash
export SONAR_TOKEN=<token>
mvn sonar:sonar -Dsonar.projectKey=jeudeloie -Dsonar.host.url=https://sonarcloud.io
```

---

## CI/CD

* **GitLab CI** : `.gitlab-ci.yml` (build + sonar + package).
* **GitHub Actions** : utilisez `.github/workflows/maven.yml` (template fourni).

---

## Licence

Distributed under the **MIT License**. See [`LICENSE`](LICENSE) for more information.
