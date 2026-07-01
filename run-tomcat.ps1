$ErrorActionPreference = "Stop"

$Project = Split-Path -Parent $MyInvocation.MyCommand.Path
$Tomcat = "D:\apache-tomcat-10.1.55"
$JavaHome = "C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
$Repo = "$env:USERPROFILE\.m2\repository"

$Libs = @(
  "$Repo\org\springframework\spring-webmvc\6.0.22\spring-webmvc-6.0.22.jar",
  "$Repo\org\springframework\spring-web\6.0.22\spring-web-6.0.22.jar",
  "$Repo\org\springframework\spring-context\6.0.22\spring-context-6.0.22.jar",
  "$Repo\org\springframework\spring-beans\6.0.22\spring-beans-6.0.22.jar",
  "$Repo\org\springframework\spring-core\6.0.22\spring-core-6.0.22.jar",
  "$Repo\org\springframework\spring-jcl\6.0.22\spring-jcl-6.0.22.jar",
  "$Repo\org\springframework\spring-expression\6.0.22\spring-expression-6.0.22.jar",
  "$Repo\org\springframework\spring-aop\6.0.22\spring-aop-6.0.22.jar",
  "$Repo\org\springframework\spring-jdbc\6.0.22\spring-jdbc-6.0.22.jar",
  "$Repo\org\springframework\spring-tx\6.0.22\spring-tx-6.0.22.jar",
  "$Repo\com\mysql\mysql-connector-j\8.4.0\mysql-connector-j-8.4.0.jar",
  "$Repo\io\micrometer\micrometer-observation\1.10.13\micrometer-observation-1.10.13.jar",
  "$Repo\io\micrometer\micrometer-commons\1.10.13\micrometer-commons-1.10.13.jar",
  "$Repo\jakarta\servlet\jsp\jstl\jakarta.servlet.jsp.jstl-api\3.0.0\jakarta.servlet.jsp.jstl-api-3.0.0.jar",
  "$Repo\org\glassfish\web\jakarta.servlet.jsp.jstl\3.0.1\jakarta.servlet.jsp.jstl-3.0.1.jar"
)

$CompileLibs = $Libs + @(
  "$Repo\jakarta\servlet\jakarta.servlet-api\6.0.0\jakarta.servlet-api-6.0.0.jar",
  "$Repo\jakarta\servlet\jsp\jakarta.servlet.jsp-api\3.1.0\jakarta.servlet.jsp-api-3.1.0.jar"
)

foreach ($Jar in $CompileLibs) {
  if (!(Test-Path $Jar)) {
    throw "Missing dependency: $Jar"
  }
}

$env:JAVA_HOME = $JavaHome
$env:CATALINA_HOME = $Tomcat
$env:CATALINA_BASE = $Tomcat
Remove-Item Env:\JRE_HOME -ErrorAction SilentlyContinue

Set-Location $Project
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue target\classes, target\bookinghotel
New-Item -ItemType Directory -Force target\classes, target\bookinghotel | Out-Null

$Sources = Get-ChildItem -Recurse src\main\java -Filter *.java | ForEach-Object FullName
javac -encoding UTF-8 -source 17 -target 17 -cp ($CompileLibs -join ';') -d target\classes $Sources

Copy-Item -Recurse -Force src\main\webapp\* target\bookinghotel\
New-Item -ItemType Directory -Force target\bookinghotel\WEB-INF\classes, target\bookinghotel\WEB-INF\lib | Out-Null
Copy-Item -Recurse -Force target\classes\* target\bookinghotel\WEB-INF\classes\
foreach ($Jar in $Libs) {
  Copy-Item -Force $Jar target\bookinghotel\WEB-INF\lib\
}

Start-Process -FilePath "$Tomcat\bin\shutdown.bat" -WorkingDirectory "$Tomcat\bin" -WindowStyle Hidden -Wait
Start-Sleep -Seconds 3
Remove-Item -Force -ErrorAction SilentlyContinue "$Tomcat\conf\Catalina\localhost\bookinghotel.xml"
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue "$Tomcat\webapps\bookinghotel", "$Tomcat\work\Catalina\localhost\bookinghotel"
Copy-Item -Recurse -Force target\bookinghotel "$Tomcat\webapps\bookinghotel"
Start-Process -FilePath "$Tomcat\bin\startup.bat" -WorkingDirectory "$Tomcat\bin" -WindowStyle Hidden
Start-Sleep -Seconds 10

Invoke-WebRequest -UseBasicParsing "http://localhost:8080/bookinghotel/dashboard" -TimeoutSec 20 | Select-Object StatusCode
Write-Host "Running: http://localhost:8080/bookinghotel/dashboard"
