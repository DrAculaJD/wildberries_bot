# Бот для Wildberries
<a href="https://codeclimate.com/github/DrAculaJD/wildberries_bot/maintainability"><img src="https://api.codeclimate.com/v1/badges/ec282165f37500a9dfba/maintainability" /></a>
[![Java CI](https://github.com/DrAculaJD/wildberries_bot/actions/workflows/main.yml/badge.svg)](https://github.com/DrAculaJD/wildberries_bot/actions/workflows/main.yml)
***
## Описание
Telegram бот для поставщиков на маркетплейсе Wildberries. Работает с Wildberries API, отправляя запросы на сервер и обрабатывая приходящие в ответ данные. Для функционала пользователю необходимо предоставить API ключи "Статистика" и "Вопросы и отзывы", которые пользователь может сгенерировать в своем личном кабинете.

 Бот умеет показывать заказы и продажи за текущий день, а так же отзывы и вопросы, на которые еще нет ответа. Кроме того в последнем обновлении добавлена функция ответа на отзывы и вопросы прямо из бота и возможность получить заказы или продажи с начала текущего года в Excel файле.
 
 Для ознакомления прилагаю [ссылку](https://youtu.be/lVWAXFmKVhc) на видеоролик с демонстрацией работы бота.

### Стек проекта
- Java Core
- Collections
- PostgreSQL
- OkHttp
- Jackson
- Telegram Bots API
- Wildberries API
- Apache POI
- Checkstyle
***
***Имя бота в Telegram:*** @polezhaevTestBot
***
## Обновление бота 23.11.2023
В связи с тем, что Wildberries обновили свой API введены следующие изменения:
- Изменены правила проверки ключей клиента при его регистрации или обновлении старых ключей: так как у поставщиков Вайлдберриз появилась выбирать возможность изменять данные в личном кабинете по созданному ключу, длина ключа будет разной в зависимости от выбранной опции;
- Вместо данных о том был ли отменен заказ теперь указывается тип заказа: клиентский или возвратны. Если это возврат, то указывается причина возврата.
- В информации о продажах так же теперь указывается тип продажи, что является более корректным, так как в один момент Вайлдберриз перестал разделять клиентские продажи и возвраты.
***
## Обновление бота 07.07.2023
Добавлена команда для удаления данных пользователя из бота, которая удаляет строку с записью пользователя из базы SQL. 

Добавлена возможность отменить ввод ответа на отзыв или вопрос.
***
## Обновление бота 22.06.2023
Добавлена возможность получить заказы или продажи поставщика с начала текущего года. Но есть один нюанс: Wildberries API предоставляет данные не ранее той даты, когда был создан первый ключ API Статистика.
***
## Обновление бота 17.06.2023
Добавлена документация к коду. Устранена критическая ошибка, из-за которой новый пользователь не мог зарегистрироваться в боте.
***
## Обновление бота 15.06.2023
Бот научился отправлять ответы пользователя на отзывы и вопросы клиентов. Решены проблемы, связанные с работой с несколькими пользователями одновременно. 
***
## Состояние бота на 17.05.2023
Сегодня мне удалось научить бот показывать отзывы и вопросы без ответов. Чтобы убедиться, что все действительно работает, необходимо длительное время пользоваться ботом и смотреть на результаты. Я вижу проблемы в коде которые, скорее всего, будут влиять работу бота с несколькими пользователями одновременно, но пока решение этой проблемы я отложил. 
***
## Состояние бота на 16.04.2023
На данный момент требуется решить проблему связанную с обновлениями бота: сейчас, если я перезапускаю бот на сервере, пользователь не получает никаких оповещений и он не сможет пользоваться ботом, пока сам не перезапустит диалог в чате. Это совсем неочевидно и очень неудобно каждый раз при обновлениях осуществлять перезапуск бота и снова вводить API ключ. В связи с этим принято решение создать базу данных SQL для хранения ID беседы с пользоватлем и его ключа API.
***
##  Обновление бота 04.04.2023
Сегодня бот научился показывать продажи за текущий день!
***
##  Первые результаты 03.04.2023
Запустил бота на хостинге timeweb.cloud! Сейчас он умеет показывать заказы за текущий день.
