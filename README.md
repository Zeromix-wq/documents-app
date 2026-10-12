## Технологии
- Java 17
- Java Swing
- ООП (наследование, полиморфизм, инкапсуляция)

## Запуск

### Windows (PowerShell)

# Создать папку для скомпилированных файлов
if (!(Test-Path build)) { mkdir build }

# Скомпилировать исходники
javac -encoding UTF-8 -d build src/com/example/documents/*.java

# Запустить приложение
java "-Dfile.encoding=UTF-8" -cp build com.example.documents.MainApp