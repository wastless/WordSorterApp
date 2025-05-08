# Word Sorter Application

## Развертывание на Render.com

1. Создайте аккаунт на [Render.com](https://render.com)
2. Создайте новый Web Service
3. Подключите GitHub репозиторий
4. Настройки:
   - Environment: Docker
   - Branch: main
   - Port: 5000
   - Plan: Free

## Подключение клиента

После деплоя сервера, вы получите URL вида: `https://your-app-name.onrender.com`

Для подключения клиента к серверу, используйте полученный URL и порт 5000.

## Локальный запуск

Для локального запуска:
1. Соберите и запустите сервер: `javac WordSorterServer.java && java WordSorterServer`
2. Запустите клиент: `javac WordSorterClient.java && java WordSorterClient` 