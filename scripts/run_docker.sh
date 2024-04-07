#!/bin/bash
docker pull yerimsw/cukz:latest
docker run -d -p 80:8080 yerimsw/cukz:latest