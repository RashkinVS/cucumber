#language:ru

Функционал: Обновление созданного тикета с прикреплением файла

  Предыстория:
    Когда Откроем браузер chrome
    И Переходим на сайт "https://at-sandbox.workbench.lanit.ru/"

  Сценарий: Создадим тикет, прикрепим файл и обновим тикет
    И Переходим на страницу авторизации
    И Создаем тикет
    И Проверим созданный тикет
    Тогда Обновим тикет
    Затем Закроем браузер chrome


  Сценарий: Создадим тикет, найдем его через поиск и сохраним историю поиска
    И Переходим на страницу авторизации
    И Создаем тикет
    И Проверим созданный тикет
    И Найдем созданный тикет
    И Сохраним историю поиска
    Затем Закроем браузер chrome