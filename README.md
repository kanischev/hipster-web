# Hipster-Web-2018 [Not Yet Ready]
Starter project for play-scala-react web development. The most actual and hipster technologies for web development glued together:

Backend:
 * Play Framework 2.6.12
 * Scala 2.12
 * sbt 1.1.1
 * Java 8 Time API with slick-pg
 * Postgres for prod + h2 in PG mode for dev + db evolutions applied
 * [Swagger plugin](https://github.com/swagger-api/swagger-play) v. 1.6.0 (Run application and go to http://localhost:9000/docs/)
 * Security by [Silhouette](https://github.com/mohiva/play-silhouette) - Added tables for users / roles / auth tokens. Everything is kept in Postgres but can be changed easily (implementation injected via guice)
 * ORM - Slick + Opinionated slick codegeneration integrated (goto codegen.GenTables class). For now should be run manually every time db structure changes.

Frontend:
 * Go to ui dir and do 'npm install'
 * Run frontend with 'npm start'
 * React 0.16, react-redux, redux-saga
 * Front end security
 * [Ant-design](https://ant.design/) for react [v. 3](https://github.com/ant-design/ant-design/)


The application contains signup | signin forms, user management (edit, block, etc), SWAGGER UI and full-featured security
During creating this application, used:
* [scala-play-react-seed](https://github.com/yohangz/scala-play-react-seed) 