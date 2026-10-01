Create and configure the MySQL database for this Spring Boot project

Steps (Windows PowerShell):

1. Verify MySQL is installed and running in the devie
   - Make sure the MySQL server is installed and the service is running.
   - You can check the service with:

     Get-Service -Name MySQL\*

2. Edit the SQL script if you need different credentials
   - Open `scripts/create_database.sql` and change the username and password from
     'bookuser' / 'changeme' to values you prefer. If your MySQL instance requires
     a different host (not localhost), update the SQL accordingly.

3. Run the script using the MySQL client (PowerShell example)
   - If `mysql` is in your PATH:

     mysql -u root -p < .\scripts\create_database.sql

   - If you need to provide the root password on the command line (not recommended):

     mysql -u root -proot_password < .\scripts\create_database.sql

   - If your MySQL binary is not in PATH, provide the full path, e.g.:

     "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p < .\scripts\create_database.sql

4. Update the Spring Boot configuration
   - Edit `src/main/resources/application.properties` and set the datasource properties:

     spring.datasource.url=jdbc:mysql://localhost:3306/bookstore?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
     spring.datasource.username=bookuser
     spring.datasource.password=changeme
     spring.jpa.hibernate.ddl-auto=update

   - For production, do not use `ddl-auto=update`; use `validate` or manage schema via migrations.

5. Test running the application
   - Build and run the Spring Boot app from the project root (PowerShell):

     .\mvnw.cmd -DskipTests=true package ; java -jar .\target\bookstore-0.0.1-SNAPSHOT.jar

   - Or run from your IDE.

Notes

- If you prefer to use a different RDBMS (Postgres, H2), adjust the JDBC URL and driver dependency in `pom.xml` accordingly.
- If you are using Docker, you can run MySQL quickly with:

  docker run --name bookstore-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=bookstore -e MYSQL_USER=bookuser -e MYSQL_PASSWORD=changeme -p 3306:3306 -d mysql:8.0

- After running the script, you can connect using any MySQL client and verify the `book` table and sample rows exist.
