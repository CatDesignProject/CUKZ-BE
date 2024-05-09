#!/bin/bash
sudo docker stop cukz
sudo docker rm cukz
sudo docker pull yerimsw/cukz:latest
sudo docker run -d -p 8080:8080 --env-file /home/ubuntu/cukz/cukz.env --name cukz yerimsw/cukz:latest
