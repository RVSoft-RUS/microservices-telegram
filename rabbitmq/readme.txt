docker образ
rabbitmq:3.11.0-management
docker volume
docker volume create rabbitmq_data
docker run -d --hostname rabbitmq --name rabbitmq -p 5672:5672 -p 15672:15672 -v rabbitmq_data:/var/lib/rabbitmq --restart=unless-stopped rabbitmq:3.11.0-management
docker exec -it rabbitmq /bin/bash
root@rabbitmq:/# rabbitmqctl add_user userok p@ssw0rd
root@rabbitmq:/# rabbitmqctl set_user_tags userok administrator
root@rabbitmq:/# rabbitmqctl set_permissions -p / userok ".*" ".*" ".*"