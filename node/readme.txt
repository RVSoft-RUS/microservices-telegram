

docker run -d --hostname pogreb --name pogreb -p 5432:5432 -e POSTGRES_USER=userok -e POSTGRES_PASSWORD=p@ssw0rd -e POSTGRES_DB=pogreb -v /data:/var/lib/postgresql/data --restart=unless-stopped postgres:14.5

Флаги:
--detach , -d   запустит контейнер в фоновом режиме и вернет идентификатор контейнера;
--hostname   адрес контейнера для подключения к нему внутри docker из других контейнеров;
--name   имя контейнера;
-p    порты: первый порт — тот, который мы увидим снаружи docker, а второй — тот, который внутри контейнера;
-e  задает переменную окружения в контейнере;
-v   примонтировать volume (том);
--restart=unless-stopped   контейнер будет подниматься заново при каждом перезапуске системы (точнее, при запуске docker);