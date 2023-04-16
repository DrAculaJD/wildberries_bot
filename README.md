# Бот для Wildberries
<a href="https://codeclimate.com/github/DrAculaJD/wildberries_bot/maintainability"><img src="https://api.codeclimate.com/v1/badges/ec282165f37500a9dfba/maintainability" /></a>
[![Java CI](https://github.com/DrAculaJD/wildberries_bot/actions/workflows/main.yml/badge.svg)](https://github.com/DrAculaJD/wildberries_bot/actions/workflows/main.yml)
***
## Описание
На данный момент бот умеет позазывать заказы и продажи за текущий день при нажатии на кнопку.
Далее планируется постепенно развивать бот, добавляя к нему новый функционал: отчеты за выбранный период, информация об отменах заказа, автоматическое оповещение о заказах и продажах. Для ознакомления прилагаю [ссылку](https://openapi.wb.ru) на Wildberries API
***
***Имя бота в Telegram:*** @polezhaevTestBot. API ключ для тестирования, я могу предоставить по вашей просьбе.
***
## Состояние бота на 16.04.2023
На данный момент требуется решить проблему связанную с обновлениями бота: сейчас, если я перезапускаю бот на сервере, пользователь не получает никаких оповещений и он не сможет пользоваться ботом, пока сам не перезапустит диалог в чате. Это совсем неочевидно и очень неудобно каждый раз при обновлениях осуществлять перезапуск бота и снова вводить API ключ. В связи с этим принято решение создать базу данных SQL для хранения ID беседы с пользоватлем и его ключа API.
***
##  Обновление бота 04.04.2023
Сегодня бот научился показывать продажи за текущий день!
[Пример 1](https://github.com/DrAculaJD/wildberries_bot/blob/4f15699153fc8da1722f0fa125ac92a89b33240b/app/src/main/resources/tb1.jpg)
[Пример 2](https://github.com/DrAculaJD/wildberries_bot/blob/4f15699153fc8da1722f0fa125ac92a89b33240b/app/src/main/resources/tb2.jpg)
***
##  Первые результаты 03.04.2023
Запустил бота на хостинге timeweb.cloud! Сейчас он умеет показывать заказы за текущий день.
